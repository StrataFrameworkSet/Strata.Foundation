//////////////////////////////////////////////////////////////////////////////
// CloseFailedException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.resource;

/**
 * <p>
 * {@link ResourceException} thrown when an {@link IResource} could not be
 * closed, such as when {@link OpenExtent} attempts to close a resource on
 * completion of a try-with-resources block.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * throw new CloseFailedException(cause);
 * </pre>
 * </p>
 */
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

