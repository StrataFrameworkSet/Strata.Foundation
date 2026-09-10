//////////////////////////////////////////////////////////////////////////////
// IEventProcessorSupplier.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.function.Supplier;

/**
 * <p>
 * A {@link java.util.function.Supplier} of {@link IEventProcessor}
 * instances, enabling deferred or factory-based processor creation.
 * </p>
 * <br/>
 * <b>Type Parameters</b><br/>
 * <ul>
 * <li>{@code <E>} - event type</li>
 * <li>{@code <P>} - processor type</li>
 * </ul>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Supplier as lambda
 * IEventProcessorSupplier&lt;String,IEventProcessor&lt;String&gt;&gt; supplier =
 *     () -&gt; new MyEventProcessor();
 *
 * IEventProcessor&lt;String&gt; processor = supplier.get();
 * </pre>
 */
public
interface IEventProcessorSupplier<E,P extends IEventProcessor<E>>
    extends Supplier<P> {}

//////////////////////////////////////////////////////////////////////////////
