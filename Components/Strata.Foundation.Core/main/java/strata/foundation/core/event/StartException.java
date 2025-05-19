/// ///////////////////////////////////////////////////////////////////////////
// StartException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

public
class StartException
    extends RuntimeException
{
    public
    StartException(String message)
    {
        super(message);
    }

    public
    StartException(String message,Throwable cause)
    {
        super(message,cause);
    }

    public
    StartException(Throwable cause)
    {
        super(cause);
    }
}

//////////////////////////////////////////////////////////////////////////////

