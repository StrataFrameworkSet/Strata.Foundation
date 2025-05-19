//////////////////////////////////////////////////////////////////////////////
// ResourceException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.resource;

public
class ResourceException
    extends RuntimeException
{
    public
    ResourceException(String message)
    {
        super(message);
    }

    public
    ResourceException(String message,Throwable cause)
    {
        super(message,cause);
    }

    public
    ResourceException(Throwable cause)
    {
        super(cause);
    }
}

//////////////////////////////////////////////////////////////////////////////

