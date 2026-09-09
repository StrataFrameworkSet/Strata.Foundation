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
 * </p>
 * <p>
 * <h4>Type Parameters</h4>
 * <ul>
 * <li>{@code <E>} - event type</li>
 * <li>{@code <L>} - listener type</li>
 * </ul>
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Register and start receiving events
 * IEventReceiver&lt;String,IEventListener&lt;String&gt;&gt; receiver = ...;
 * receiver.startListening(event -&gt; process(event));
 *
 * // Stop receiving
 * receiver.stopListening();
 * </pre>
 * </p>
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