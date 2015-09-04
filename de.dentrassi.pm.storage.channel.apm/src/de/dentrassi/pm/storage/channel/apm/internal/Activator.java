package de.dentrassi.pm.storage.channel.apm.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.dentrassi.pm.aspect.ChannelAspectProcessor;
import de.dentrassi.pm.generator.GeneratorProcessor;

public class Activator implements BundleActivator
{

    private static Activator INSTANCE;

    private ChannelAspectProcessor processor;

    private GeneratorProcessor generatorProcessor;

    @Override
    public void start ( final BundleContext context ) throws Exception
    {
        INSTANCE = this;

        this.processor = new ChannelAspectProcessor ( context );

        this.generatorProcessor = new GeneratorProcessor ( context );
        this.generatorProcessor.open ();
    }

    @Override
    public void stop ( final BundleContext context ) throws Exception
    {
        INSTANCE = null;

        if ( this.processor != null )
        {
            this.processor.close ();
            this.processor = null;
        }

        if ( this.generatorProcessor != null )
        {
            this.generatorProcessor.close ();
            this.generatorProcessor = null;
        }
    }

    public static ChannelAspectProcessor getProcessor ()
    {
        return INSTANCE.processor;
    }

    public static GeneratorProcessor getGeneratorProcessor ()
    {
        return INSTANCE.generatorProcessor;
    }

}
