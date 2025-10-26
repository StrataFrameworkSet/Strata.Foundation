//////////////////////////////////////////////////////////////////////////////
// ImmutableMultiSet.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.util.*;
import java.util.stream.Stream;

public
class ImmutableMultiSet<T extends Comparable<T>>
    implements IMultiSet<T>
{
    private final IMultiSet<T> source;

    public
    ImmutableMultiSet(IMultiSet<T> source)
    {
        if (source == null) {
            throw new NullPointerException("source cannot be null");
        }
        this.source = source;
    }

    @Override
    public IMultiSet<T>
    add(T element)
    {
        throw new UnsupportedOperationException("ImmutableMultiSet cannot be modified");
    }

    @Override
    public IMultiSet<T>
    add(T element, Long multiplicity)
    {
        throw new UnsupportedOperationException("ImmutableMultiSet cannot be modified");
    }

    @Override
    public IMultiSet<T>
    addAll(IMultiSet<T> other)
    {
        throw new UnsupportedOperationException("ImmutableMultiSet cannot be modified");
    }

    @Override
    public IMultiSet<T>
    remove(T element)
    {
        throw new UnsupportedOperationException("ImmutableMultiSet cannot be modified");
    }

    @Override
    public IMultiSet<T>
    remove(T element, Long multiplicity)
    {
        throw new UnsupportedOperationException("ImmutableMultiSet cannot be modified");
    }

    @Override
    public IMultiSet<T>
    replace(T element, Long multiplicity)
    {
        throw new UnsupportedOperationException("ImmutableMultiSet cannot be modified");
    }

    @Override
    public IMultiSet<T>
    removeFromUnderlying(T element)
    {
        throw new UnsupportedOperationException("ImmutableMultiSet cannot be modified");
    }

    @Override
    public Set<T>
    getUnderlying()
    {
        return Collections.unmodifiableSet(source.getUnderlying());
    }

    @Override
    public long
    getUnderlyingSize()
    {
        return source.getUnderlyingSize();
    }

    @Override
    public long
    getCardinality()
    {
        return source.getCardinality();
    }

    @Override
    public long
    getMultiplicity(T element)
    {
        return source.getMultiplicity(element);
    }

    @Override
    public long
    getDistance(IMultiSet<T> other)
    {
        return source.getDistance(other);
    }

    @Override
    public IMultiSet<T>
    makeUnionWith(IMultiSet<T> other)
    {
        return new ImmutableMultiSet<>(source.makeUnionWith(other));
    }

    @Override
    public IMultiSet<T>
    makeIntersectionWith(IMultiSet<T> other)
    {
        return new ImmutableMultiSet<>(source.makeIntersectionWith(other));
    }

    @Override
    public IMultiSet<T>
    makeSymmetricDifferenceWith(IMultiSet<T> other)
    {
        return new ImmutableMultiSet<>(source.makeSymmetricDifferenceWith(other));
    }

    @Override
    public boolean
    isEmpty()
    {
        return source.isEmpty();
    }

    @Override
    public Stream<Pair<T,Long>>
    stream()
    {
        return source.stream();
    }

    @Override
    public Iterator<Pair<T,Long>>
    iterator()
    {
        return stream().iterator();
    }

    @Override
    public int
    compareTo(IMultiSet<T> other)
    {
        return source.compareTo(other);
    }

    @Override
    public boolean
    equals(Object obj)
    {
        return source.equals(obj);
    }

    @Override
    public int
    hashCode()
    {
        return source.hashCode();
    }

    @Override
    public String
    toString()
    {
        return source.toString();
    }

    public static <T extends Comparable<T>> ImmutableMultiSet<T>
    of(IMultiSet<T> source)
    {
        return new ImmutableMultiSet<>(source);
    }
}

//////////////////////////////////////////////////////////////////////////////
