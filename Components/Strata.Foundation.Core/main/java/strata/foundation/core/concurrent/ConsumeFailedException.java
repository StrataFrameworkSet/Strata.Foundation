//////////////////////////////////////////////////////////////////////////////
// ConsumeFailedException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

/**
 * <p>
 * Thrown when an {@link IReceiver} encounters an error while
 * consuming a message from its source.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Caught during receiver operation
 * try
 * {
 *     receiver.startConsuming(consumer);
 * }
 * catch (ConsumeFailedException e)
 * {
 *     Throwable cause = e.getCause();
 * }
 * </pre>
 * </p>
 */
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

