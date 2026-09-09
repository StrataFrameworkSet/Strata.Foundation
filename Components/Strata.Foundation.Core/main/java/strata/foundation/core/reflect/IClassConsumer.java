//////////////////////////////////////////////////////////////////////////////
// IClassConsumer.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.reflect;

import java.util.function.Consumer;

/**
 * <p>
 * A {@link java.util.function.Consumer} specialization that accepts the
 * {@link Class} object for a given type.
 * </p>
 * <h4>Type Parameter</h4>
 * {@code <T>} - the type whose {@link Class} is consumed
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * IClassConsumer&lt;Widget&gt; consumer = type -&gt; System.out.println(type.getName());
 * consumer.accept(Widget.class);
 * </pre>
 * </p>
 */
public
interface IClassConsumer<T>
    extends Consumer<Class<T>> {}

//////////////////////////////////////////////////////////////////////////////