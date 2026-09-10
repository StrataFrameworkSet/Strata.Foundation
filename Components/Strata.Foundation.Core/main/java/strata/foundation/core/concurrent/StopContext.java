//////////////////////////////////////////////////////////////////////////////
// StopContext.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.Objects;

/**
 * <p>
 * Wrapper that pairs an element with a stop indicator for
 * poison-pill based graceful shutdown of blocking queues.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Normal element
 * StopContext&lt;String&gt; normal = StopContext.of("data");
 * String value = normal.getElement();
 * boolean stop = normal.mustStop(); // false
 *
 * // Poison pill (stop signal)
 * StopContext&lt;String&gt; poison = StopContext.stop();
 * boolean stop = poison.mustStop(); // true
 * </pre>
 *
 * @param <T> element type
 */
public
class StopContext<T>
{
    private final T       element;
    private final boolean stopIndicator;

    public
    StopContext(T element)
    {
        this(element, false);
    }

    public
    StopContext()
    {
        this(null, true);
    }

    public
    StopContext(T element,boolean stopIndicator)
    {
        this.element       = element;
        this.stopIndicator = stopIndicator;
    }

    @Override
    public int
    hashCode()
    {
        return Objects.hash(element,stopIndicator);
    }

    @Override
    public boolean
    equals(Object other)
    {
        return
            other instanceof StopContext<?> context &&
            Objects.equals(element, context.element) &&
            Objects.equals(stopIndicator, context.stopIndicator);
    }

    public T
    getElement() { return element; }

    public boolean
    mustStop() { return stopIndicator;}

    public static <T> StopContext<T>
    of(T element) { return new StopContext<>(element, false); }

    public static <T> StopContext<T>
    stop() { return new StopContext<>(null, true);}
}

//////////////////////////////////////////////////////////////////////////////
