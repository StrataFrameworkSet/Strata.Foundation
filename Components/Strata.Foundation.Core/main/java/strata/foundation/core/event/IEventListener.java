//////////////////////////////////////////////////////////////////////////////
// IEventListener.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.Collection;

/**
 * <p>
 * Functional listener that receives and handles events.
 * See: <a href="https://en.wikipedia.org/wiki/Observer_pattern">Observer pattern (Wikipedia)</a>
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Lambda-based listener
 * IEventListener&lt;String&gt; listener = event -&gt;
 *     System.out.println("Received: " + event);
 * </pre>
 *
 * @param <E> event type
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
