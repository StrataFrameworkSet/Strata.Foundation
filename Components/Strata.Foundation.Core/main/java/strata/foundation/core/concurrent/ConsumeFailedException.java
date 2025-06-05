/// ///////////////////////////////////////////////////////////////////////////
// ConsumeFailedException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

public
class ConsumeFailedException
    extends RuntimeException
{
    public
    ConsumeFailedException(String message)
    {
        super(message);
    }

    public
    ConsumeFailedException(String message,Throwable cause)
    {
        super(message,cause);
    }

    public
    ConsumeFailedException(Throwable cause)
    {
        super(cause);
    }
}

//////////////////////////////////////////////////////////////////////////////

