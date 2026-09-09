//////////////////////////////////////////////////////////////////////////////
// ProcessingException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

/**
 * <p>
 * Thrown when an {@link IEventProcessor} encounters an error
 * while processing an event.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Caught during event processing
 * try
 * {
 *     processor.onEvent(event);
 * }
 * catch (ProcessingException e)
 * {
 *     Throwable cause = e.getCause();
 * }
 * </pre>
 * </p>
 */
public
class ProcessingException
    extends RuntimeException
{
    public
    ProcessingException(String message)
    {
        super(message);
    }

    public
    ProcessingException(String message,Throwable cause)
    {
        super(message,cause);
    }

    public
    ProcessingException(Throwable cause)
    {
        super(cause);
    }
}

//////////////////////////////////////////////////////////////////////////////

