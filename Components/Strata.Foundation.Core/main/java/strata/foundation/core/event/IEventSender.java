//////////////////////////////////////////////////////////////////////////////
// IEventSender.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

/**
 * <p>
 * Sends events asynchronously, returning an {@link ICompletableSendResult}
 * to track the outcome.
 * See: <a href="https://en.wikipedia.org/wiki/Publish%E2%80%93subscribe_pattern">Publish-subscribe pattern (Wikipedia)</a>
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Send an event
 * IEventSender&lt;String&gt; sender = ...;
 * ICompletableSendResult&lt;String&gt; result = sender.send("event-data");
 * </pre>
 *
 * @param <E> event type
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
