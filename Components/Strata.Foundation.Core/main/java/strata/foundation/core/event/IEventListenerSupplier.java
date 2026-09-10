//////////////////////////////////////////////////////////////////////////////
// IEventListenerSupplier.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.function.Supplier;

/**
 * <p>
 * A {@link java.util.function.Supplier} of {@link IEventListener}
 * instances, enabling deferred or factory-based listener creation.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Supplier as lambda
 * IEventListenerSupplier&lt;String,IEventListener&lt;String&gt;&gt; supplier =
 *     () -&gt; event -&gt; System.out.println(event);
 *
 * IEventListener&lt;String&gt; listener = supplier.get();
 * </pre>
 *
 * @param <E> event type
 * @param <L> listener type
 */
public
interface IEventListenerSupplier<E,L extends IEventListener<E>>
    extends Supplier<L> {}

//////////////////////////////////////////////////////////////////////////////
