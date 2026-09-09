//////////////////////////////////////////////////////////////////////////////
// IEventKeySelector.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

/**
 * <p>
 * Extracts a routing key from an event for partitioning or
 * topic-based dispatch.
 * </p>
 * <p>
 * <h4>Type Parameters</h4>
 * <ul>
 * <li>{@code <K>} - key type</li>
 * <li>{@code <E>} - event type</li>
 * </ul>
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Extract a key from an event
 * IEventKeySelector&lt;String,OrderEvent&gt; selector =
 *     event -&gt; event.getOrderId();
 * String key = selector.getKey(orderEvent);
 * </pre>
 * </p>
 */
public
interface IEventKeySelector<K,E>
{
    K
    get(E event);
}

//////////////////////////////////////////////////////////////////////////////