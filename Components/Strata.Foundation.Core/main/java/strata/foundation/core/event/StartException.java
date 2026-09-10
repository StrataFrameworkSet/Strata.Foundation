//////////////////////////////////////////////////////////////////////////////
// StartException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

/**
 * <p>
 * Thrown when an {@link IEventReceiver} fails to start
 * its event listening lifecycle.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Caught during receiver startup
 * try
 * {
 *     receiver.startListening(listener);
 * }
 * catch (StartException e)
 * {
 *     Throwable cause = e.getCause();
 * }
 * </pre>
 */
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

