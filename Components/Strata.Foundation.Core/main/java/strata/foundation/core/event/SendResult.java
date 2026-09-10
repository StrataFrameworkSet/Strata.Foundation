//////////////////////////////////////////////////////////////////////////////
// SendResult.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.Optional;

/**
 * <p>
 * Result container for an event send operation, holding the
 * original event and either a success indicator or an exception.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Inspect send outcome
 * SendResult&lt;String&gt; result = ...;
 * String event = result.getEvent();
 * boolean success = result.isSuccessful();
 *
 * // Check for failure
 * if (result.hasException())
 *     result.getException().printStackTrace();
 * </pre>
 *
 * @param <E> event type
 */
public
class SendResult<E>
{
    private Optional<E>         sentEvent;
    private Optional<Throwable> exception;

    public
    SendResult(E event)
    {
        sentEvent = Optional.of(event);
        exception = Optional.empty();
    }

    public
    SendResult(Throwable e)
    {
        sentEvent = Optional.empty();
        exception = Optional.of(e);
    }

    public boolean
    isSuccess() { return sentEvent.isPresent(); }

    public Optional<E>
    getSentEvent() { return sentEvent; }

    public Optional<Throwable>
    getException() { return exception; }
}

//////////////////////////////////////////////////////////////////////////////
