//////////////////////////////////////////////////////////////////////////////
// IEventReceiver.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.Optional;

/**
 * <p>
 * Manages registration and lifecycle of {@link IEventListener} instances
 * for receiving events.
 * See: <a href="https://en.wikipedia.org/wiki/Observer_pattern">Observer pattern (Wikipedia)</a>
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Register and start receiving events
 * IEventReceiver&lt;String,IEventListener&lt;String&gt;&gt; receiver = ...;
 * receiver.startListening(event -&gt; process(event));
 *
 * // Stop receiving
 * receiver.stopListening();
 * </pre>
 *
 * @param <E> event type
 * @param <L> listener type
 */
public
interface IEventReceiver<E,L extends IEventListener<E>>
{
    IEventReceiver<E,L>
    setListener(L listener);

    Optional<L>
    getListener();

    boolean
    hasListener();

    void
    startListening(L listener);

    void
    startListening();

    void
    stopListening();

    boolean
    isListening();
}

//////////////////////////////////////////////////////////////////////////////
