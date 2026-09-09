//////////////////////////////////////////////////////////////////////////////
// IEventListener.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.Collection;

/**
 * <p>
 * Functional listener that receives and handles events.
 * See: <a href="https://en.wikipedia.org/wiki/Observer_pattern">Observer pattern (Wikipedia)</a>
 * </p>
 * <p>
 * <h4>Type Parameter</h4>
 * {@code <E>} - event type
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Lambda-based listener
 * IEventListener&lt;String&gt; listener = event -&gt;
 *     System.out.println("Received: " + event);
 * </pre>
 * </p>
 */
public
interface IEventListener<E>
{
    default void
    onStart() throws StartException {}

    default void
    onStop() {}

    void
    onEvents(Collection<E> events);

    void
    onException(Exception exception);
}

//////////////////////////////////////////////////////////////////////////////