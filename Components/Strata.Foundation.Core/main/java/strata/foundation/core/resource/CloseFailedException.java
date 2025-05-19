/// ///////////////////////////////////////////////////////////////////////////
// CloseFailedException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.resource;

public
class CloseFailedException
    extends ResourceException
{
    public
    CloseFailedException(String message)
    {
        super(message);
    }

    public
    CloseFailedException(String message,Throwable cause)
    {
        super(message,cause);
    }

    public
    CloseFailedException(Throwable cause)
    {
        super(cause);
    }
}

//////////////////////////////////////////////////////////////////////////////

