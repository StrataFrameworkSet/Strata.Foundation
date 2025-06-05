/// ///////////////////////////////////////////////////////////////////////////
// StartFailedException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

public
class StartFailedException
    extends RuntimeException
{
    public
    StartFailedException(String message)
    {
        super(message);
    }

    public
    StartFailedException(String message,Throwable cause)
    {
        super(message,cause);
    }

    public
    StartFailedException(Throwable cause)
    {
        super(cause);
    }
}

//////////////////////////////////////////////////////////////////////////////

