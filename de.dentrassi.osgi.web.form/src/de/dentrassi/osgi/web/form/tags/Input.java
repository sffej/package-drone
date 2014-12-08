package de.dentrassi.osgi.web.form.tags;

import javax.servlet.jsp.JspException;

public class Input extends FormValueTagSupport
{
    private static final long serialVersionUID = 1L;

    private String type;

    private boolean disabled;

    @Override
    public int doStartTag () throws JspException
    {
        final WriterHelper writer = new WriterHelper ( this.pageContext );

        writer.write ( "<input" );
        writer.writeAttribute ( "id", this.path );
        writer.writeAttribute ( "name", this.path );
        writer.writeOptionalAttribute ( "value", getPathValue ( this.path ) );
        writer.writeOptionalAttribute ( "type", this.type );
        writer.writeFlagAttribute ( "disabled", this.disabled );
        writeDefaultAttributes ( writer );
        writer.write ( " />" );

        return SKIP_BODY;
    }

    public void setType ( final String type )
    {
        this.type = type;
    }

    public void setDisabled ( final boolean disabled )
    {
        this.disabled = disabled;
    }

}
