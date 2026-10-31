//////////////////////////////////////////////////////////////////////////////
// Range.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import strata.foundation.core.utility.Validate;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

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
        Validate.requireNonNull(beginning,"beginning is null");
        Validate.requireNonNull(end,"end is null");
        Validate.requireNonNull(comparator,"comparator is null");
        Validate.require(
            beginning.getClass().equals(end.getClass()),
            () -> new IllegalArgumentException("beginning and end must be of the same type"));
        Validate.require(
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
