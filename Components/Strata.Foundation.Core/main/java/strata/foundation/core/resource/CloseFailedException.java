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
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * throw new CloseFailedException(cause);
 * </pre>
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

