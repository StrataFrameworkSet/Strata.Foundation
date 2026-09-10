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
 * <br/>
 * <b>Type Parameters</b><br/>
 * <ul>
 * <li>{@code <E>} - event type</li>
 * <li>{@code <L>} - listener type</li>
 * </ul>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Supplier as lambda
 * IEventListenerSupplier&lt;String,IEventListener&lt;String&gt;&gt; supplier =
 *     () -&gt; event -&gt; System.out.println(event);
 *
 * IEventListener&lt;String&gt; listener = supplier.get();
 * </pre>
 */
public
interface IEventListenerSupplier<E,L extends IEventListener<E>>
    extends Supplier<L> {}

//////////////////////////////////////////////////////////////////////////////
