//////////////////////////////////////////////////////////////////////////////
// StartFailedException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

/**
 * <p>
 * Thrown when an {@link IReceiver} fails to start its
 * consumption loop.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Caught during receiver startup
 * try
 * {
 *     receiver.startConsuming(consumer);
 * }
 * catch (StartFailedException e)
 * {
 *     Throwable cause = e.getCause();
 * }
 * </pre>
 * </p>
 */
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

