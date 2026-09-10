//////////////////////////////////////////////////////////////////////////////
// ProcessingException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

/**
 * <p>
 * Thrown when an {@link IEventProcessor} encounters an error
 * while processing an event.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
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

