/// ///////////////////////////////////////////////////////////////////////////
// StartError.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

public
class StartError
    extends Error
{
    public
    StartError(String message)
    {
        super(message);
    }

    public
    StartError(String message,Throwable cause)
    {
        super(message,cause);
    }

    public
    StartError(Throwable cause)
    {
        super(cause);
    }
}

//////////////////////////////////////////////////////////////////////////////

