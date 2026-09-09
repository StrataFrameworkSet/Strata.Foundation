//////////////////////////////////////////////////////////////////////////////
// IBlockingBuffer.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <p>
 * Single-element blocking buffer that combines
 * {@link java.util.function.Consumer} and {@link java.util.function.Supplier}
 * for synchronous producer-consumer handoff.
 * </p>
 * <p>
 * <h4>Type Parameter</h4>
 * {@code <T>} - element type
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Creation
 * IBlockingBuffer&lt;String&gt; buffer = new BlockingBuffer&lt;&gt;();
 *
 * // Producer accepts, consumer gets (blocks until available)
 * buffer.accept("hello");
 * String value = buffer.get();
 * </pre>
 * </p>
 */
public
interface IBlockingBuffer<T>
    extends Consumer<T>, Supplier<T>
{
    void
    clear();
}

//////////////////////////////////////////////////////////////////////////////
