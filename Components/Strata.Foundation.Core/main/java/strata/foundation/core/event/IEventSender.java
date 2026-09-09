//////////////////////////////////////////////////////////////////////////////
// IEventSender.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

/**
 * <p>
 * Sends events asynchronously, returning an {@link ICompletableSendResult}
 * to track the outcome.
 * See: <a href="https://en.wikipedia.org/wiki/Publish%E2%80%93subscribe_pattern">Publish-subscribe pattern (Wikipedia)</a>
 * </p>
 * <p>
 * <h4>Type Parameter</h4>
 * {@code <E>} - event type
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Send an event
 * IEventSender&lt;String&gt; sender = ...;
 * ICompletableSendResult&lt;String&gt; result = sender.send("event-data");
 * </pre>
 * </p>
 */
public
interface IEventSender<E>
{
    IEventSender<E>
    open() throws Exception;

    IEventSender<E>
    close() throws Exception;

    ICompletableSendResult<E>
    send(E event);

    boolean
    isOpen();

    boolean
    isClosed();
}


//////////////////////////////////////////////////////////////////////////////