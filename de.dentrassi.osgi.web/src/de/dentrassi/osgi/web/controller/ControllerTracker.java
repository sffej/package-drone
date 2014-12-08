package de.dentrassi.osgi.web.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dentrassi.osgi.web.Controller;
import de.dentrassi.osgi.web.RequestHandler;
import de.dentrassi.osgi.web.RequestHandlerFactory;

public class ControllerTracker implements RequestHandlerFactory
{

    private final static Logger logger = LoggerFactory.getLogger ( ControllerTracker.class );

    private final BundleContext context;

    private final ServiceListener listener = new ServiceListener () {

        @Override
        public void serviceChanged ( final ServiceEvent event )
        {
            switch ( event.getType () )
            {
                case ServiceEvent.REGISTERED:
                    handleAddingService ( event.getServiceReference () );
                    break;
                case ServiceEvent.UNREGISTERING:
                    handleRemovedService ( event.getServiceReference () );
                    break;
            }
        }
    };

    public ControllerTracker ( final BundleContext context )
    {
        this.context = context;

        this.context.addServiceListener ( this.listener );
        try
        {
            final ServiceReference<?>[] refs = context.getServiceReferences ( (String)null, (String)null );
            if ( refs != null )
            {
                for ( final ServiceReference<?> ref : refs )
                {
                    handleAddingService ( ref );
                }
            }
        }
        catch ( final InvalidSyntaxException e )
        {
            // this should never happen
        }
    }

    @Override
    public void close ()
    {
        this.context.removeServiceListener ( this.listener );
    }

    protected Object handleAddingService ( final ServiceReference<?> ref )
    {
        Object service = this.context.getService ( ref );

        if ( service != null && checkAddService ( ref, service ) == null )
        {
            this.context.ungetService ( ref );
            service = null;
        }

        return service;
    }

    protected Object checkAddService ( final ServiceReference<?> ref, final Object service )
    {
        logger.debug ( "Check add service - ref: {}", ref );

        final Controller controller = service.getClass ().getAnnotation ( Controller.class );
        if ( controller == null )
        {
            return null;
        }

        addController ( ref, new ControllerEntry ( service ) );

        return controller;
    }

    private final Map<ServiceReference<?>, ControllerEntry> controllers = new ConcurrentHashMap<> ();

    protected void addController ( final ServiceReference<?> ref, final ControllerEntry entry )
    {
        logger.info ( "Added controller - {}", ref );

        this.controllers.put ( ref, entry );
    }

    protected void handleRemovedService ( final ServiceReference<?> ref )
    {
        if ( this.controllers.remove ( ref ) != null )
        {
            this.context.ungetService ( ref );
        }
    }

    @Override
    public RequestHandler handleRequest ( final HttpServletRequest request, final HttpServletResponse response )
    {
        for ( final ControllerEntry entry : this.controllers.values () )
        {
            final RequestHandler result = entry.findHandler ( request, response );

            logger.debug ( "Result from: {} -> {}", entry, result );

            if ( result != null )
            {
                return result;
            }
        }
        return null;
    }
}
