/// ///////////////////////////////////////////////////////////////////////////
// StoppedException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

public
class StoppedException
    extends RuntimeException
{
    public
    StoppedException(String message)
    {
        super(message);
    }

    public
    StoppedException(String message,Throwable cause)
    {
        super(message,cause);
    }

    public
    StoppedException(Throwable cause)
    {
        super(cause);
    }
}

//////////////////////////////////////////////////////////////////////////////

