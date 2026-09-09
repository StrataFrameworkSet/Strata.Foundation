//////////////////////////////////////////////////////////////////////////////
// OpenFailedException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.resource;

/**
 * <p>
 * {@link ResourceException} thrown when an {@link IResource} could not be
 * opened, such as when {@link OpenExtent} attempts to open a resource on
 * construction.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * throw new OpenFailedException(cause);
 * </pre>
 * </p>
 */
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

