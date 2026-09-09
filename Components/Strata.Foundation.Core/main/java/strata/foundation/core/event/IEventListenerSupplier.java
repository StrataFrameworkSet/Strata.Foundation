//////////////////////////////////////////////////////////////////////////////
// IEventListenerSupplier.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.function.Supplier;

/**
 * <p>
 * A {@link java.util.function.Supplier} of {@link IEventListener}
 * instances, enabling deferred or factory-based listener creation.
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
 * // Supplier as lambda
 * IEventListenerSupplier&lt;String,IEventListener&lt;String&gt;&gt; supplier =
 *     () -&gt; event -&gt; System.out.println(event);
 *
 * IEventListener&lt;String&gt; listener = supplier.get();
 * </pre>
 * </p>
 */
public
interface IEventListenerSupplier<E,L extends IEventListener<E>>
    extends Supplier<L> {}

//////////////////////////////////////////////////////////////////////////////
