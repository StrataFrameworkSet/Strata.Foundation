/// ///////////////////////////////////////////////////////////////////////////
// OpenFailedException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.resource;

public
class OpenFailedException
    extends ResourceException
{
    public
    OpenFailedException(String message)
    {
        super(message);
    }

    public
    OpenFailedException(String message,Throwable cause)
    {
        super(message,cause);
    }

    public
    OpenFailedException(Throwable cause)
    {
        super(cause);
    }
}

//////////////////////////////////////////////////////////////////////////////

