package de.dentrassi.osgi.web.form.tags;

import javax.servlet.jsp.JspException;

import de.dentrassi.osgi.web.controller.binding.BindingResult;

public class Form extends FormTagSupport
{
    private static final long serialVersionUID = 1L;

    private String method;

    private String action;

    private String command = "command";

    private Object commandValue;

    public void setMethod ( final String method )
    {
        this.method = method;
    }

    public void setAction ( final String action )
    {
        this.action = action;
    }

    public void setCommand ( final String command )
    {
        this.command = command;
    }

    @Override
    public int doStartTag () throws JspException
    {
        final WriterHelper writer = new WriterHelper ( this.pageContext );

        writer.write ( "<form" );
        writer.writeOptionalAttribute ( "id", this.command );
        writer.writeOptionalAttribute ( "action", this.action );
        writer.writeOptionalAttribute ( "method", this.method );
        writeDefaultAttributes ( writer );
        writer.write ( ">" );

        this.commandValue = this.pageContext.getRequest ().getAttribute ( this.command );

        return EVAL_BODY_INCLUDE;
    }

    public BindingResult getBindingResult ()
    {
        final Object br = this.pageContext.getRequest ().getAttribute ( BindingResult.ATTRIBUTE_NAME );
        if ( br instanceof BindingResult )
        {
            return ( (BindingResult)br ).getChild ( this.command );
        }
        return null;
    }

    public Object getCommandValue ()
    {
        return this.commandValue;
    }

    @Override
    public int doEndTag () throws JspException
    {
        final WriterHelper writer = new WriterHelper ( this.pageContext );
        writer.write ( "</form>" );
        return EVAL_PAGE;
    }

}
