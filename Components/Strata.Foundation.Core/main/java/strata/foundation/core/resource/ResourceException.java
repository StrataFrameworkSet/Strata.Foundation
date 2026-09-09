//////////////////////////////////////////////////////////////////////////////
// ResourceException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.resource;

/**
 * <p>
 * Signals that an operation on an {@link IResource} or
 * {@link ICompletableResource} failed.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * throw new ResourceException("failed to open resource", cause);
 * </pre>
 * </p>
 */
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

