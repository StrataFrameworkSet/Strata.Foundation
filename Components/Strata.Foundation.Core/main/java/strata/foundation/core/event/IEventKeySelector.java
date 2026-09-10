//////////////////////////////////////////////////////////////////////////////
// IEventKeySelector.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

/**
 * <p>
 * Extracts a routing key from an event for partitioning or
 * topic-based dispatch.
 * </p>
 * <br/>
 * <b>Type Parameters</b><br/>
 * <ul>
 * <li>{@code <K>} - key type</li>
 * <li>{@code <E>} - event type</li>
 * </ul>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Extract a key from an event
 * IEventKeySelector&lt;String,OrderEvent&gt; selector =
 *     event -&gt; event.getOrderId();
 * String key = selector.getKey(orderEvent);
 * </pre>
 */
public
interface IEventKeySelector<K,E>
{
    K
    get(E event);
}

//////////////////////////////////////////////////////////////////////////////
