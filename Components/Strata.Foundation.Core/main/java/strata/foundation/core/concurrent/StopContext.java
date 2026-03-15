/// ///////////////////////////////////////////////////////////////////////////
// StopContext.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.Objects;

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
