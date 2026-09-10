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
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * IClassConsumer&lt;Widget&gt; consumer = type -&gt; System.out.println(type.getName());
 * consumer.accept(Widget.class);
 * </pre>
 *
 * @param <T> - the type whose {@link Class} is consumed
 */
public
interface IClassConsumer<T>
    extends Consumer<Class<T>> {}

//////////////////////////////////////////////////////////////////////////////
