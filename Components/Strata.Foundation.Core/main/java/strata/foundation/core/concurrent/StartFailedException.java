//////////////////////////////////////////////////////////////////////////////
// StartFailedException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

/**
 * <p>
 * Thrown when an {@link IReceiver} fails to start its
 * consumption loop.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
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

