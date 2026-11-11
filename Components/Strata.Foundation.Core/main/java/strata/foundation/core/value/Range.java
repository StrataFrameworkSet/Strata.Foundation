//////////////////////////////////////////////////////////////////////////////
// Range.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import strata.foundation.core.utility.Require;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * <p>
 * Immutable, serializable range defined by a beginning and an end value
 * of the same type, where the beginning is not after the end according to
 * a {@link java.util.Comparator}.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * Range&lt;Integer&gt; range = Range.of(1,10);
 * Range&lt;String&gt; names = Range.of("a","m",Comparator.naturalOrder());
 *
 * // Selectors
 * Integer beginning = range.getBeginning();
 * Integer end = range.getEnd();
 * </pre>
 *
 * @param <T> type of range values
 */
public
class Range<T>
    implements Serializable
{
    private final T beginning;
    private final T end;

    public
    Range(T beginning,T end,Comparator<T> comparator)
        throws NullPointerException, IllegalArgumentException
    {
        Require.notNull(beginning,"beginning is null");
        Require.notNull(end,"end is null");
        Require.notNull(comparator,"comparator is null");
        Require.isTrue(
            beginning.getClass().equals(end.getClass()),
            () -> new IllegalArgumentException("beginning and end must be of the same type"));
        Require.isTrue(
            comparator.compare(beginning,end) <= 0,
            () -> new IllegalArgumentException("beginning must be before end"));

        this.beginning = beginning;
        this.end = end;
    }

    public T
    getBeginning() { return beginning; }

    public T
    getEnd() { return end; }

    @Override
    public boolean
    equals(Object other)
    {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;

        Range<?> range = (Range<?>)other;
        return
            Objects.equals(beginning,range.beginning) &&
            Objects.equals(end,range.end);
    }

    @Override
    public int
    hashCode()
    {
        return Objects.hash(beginning,end);
    }

    @Override
    public String
    toString()
    {
        return String.format("{beginning=%s,end=%s}",beginning,end);
    }

    public static <T extends Comparable<? super T>> Range<T>
    of(T beginning,T end)
        throws NullPointerException,IllegalArgumentException
    {
        return new  Range<T>(beginning,end,T::compareTo);
    }

    public static <T> Range<T>
    of(T beginning,T end,Comparator<T> comparator)
        throws NullPointerException,IllegalArgumentException
    {
        return new  Range<T>(beginning,end,comparator);
    }
}

//////////////////////////////////////////////////////////////////////////////
