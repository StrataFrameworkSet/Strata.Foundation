/// ///////////////////////////////////////////////////////////////////////////
// MultiSet.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public
class MultiSet<T extends Comparable<T>>
    implements IMultiSet<T>
{
    private final Map<T,AtomicLong> mappings;

    public
    MultiSet()
    {
        mappings = new TreeMap<>();
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
        return
            Long
                .valueOf(getLexicalDirection(other)*getDistance(other))
                .intValue();
    }

    @Override
    public IMultiSet<T>
    add(T element)
    {
        add(element, 1L);
        return this;
    }

    @Override
    public IMultiSet<T>
    add(T element,Long multiplicity)
    {
        AtomicLong m =
            mappings.merge(
                element,
                new AtomicLong(multiplicity),
                (x,y) -> {x.addAndGet(y.get()); return x;});

        if (m.get() <= 0L)
            removeFromUnderlying(element);

        return this;
    }

    @Override
    public IMultiSet<T>
    remove(T element)
    {
        add(element, -1L);
        return this;
    }

    @Override
    public IMultiSet<T>
    remove(T element,Long multiplicity)
    {
        add(element, -multiplicity);
        return this;
    }

    @Override
    public IMultiSet<T>
    replace(T element,Long multiplicity)
    {
        if (multiplicity > 0L)
            mappings.replace(element, new AtomicLong(multiplicity));
        else
            removeFromUnderlying(element);

        return this;
    }

    @Override
    public IMultiSet<T>
    removeFromUnderlying(T element)
    {
        mappings.remove(element);
        return this;
    }

    @Override
    public Set<T>
    getUnderlying()
    {
        return new TreeSet<>(mappings.keySet());
    }

    @Override
    public long
    getUnderlyingSize()
    {
        return getUnderlying().size();
    }

    @Override
    public long
    getCardinality()
    {
        return
            mappings
                .values()
                .stream()
                .map(m -> m.get())
                .reduce(0L,Long::sum);
    }

    @Override
    public long
    getMultiplicity(T element)
    {
        return
            mappings
                .getOrDefault(element, new AtomicLong(0L))
                .get();
    }

    @Override
    public long
    getDistance(IMultiSet<T> other)
    {
        return
            makeSymmetricDifferenceWith(other).getCardinality() +
                makeIntersectionWith(other)
                    .getUnderlying()
                    .stream()
                    .map(
                        e ->
                            Math.abs(
                                getMultiplicity(e) - other.getMultiplicity(e)))
                    .reduce(0L,Long::sum);
    }

    @Override
    public IMultiSet<T>
    makeUnionWith(IMultiSet<T> other)
    {
        IMultiSet<T> union = new MultiSet<>();
        Set<T>       underlyingUnion = new TreeSet<>(getUnderlying());

        underlyingUnion.addAll(other.getUnderlying());

        underlyingUnion
            .forEach(
                e ->
                    union.add(
                        e,
                        Long.max(
                            getMultiplicity(e),
                            other.getMultiplicity(e))));

        return union;
    }

    @Override
    public IMultiSet<T>
    makeIntersectionWith(IMultiSet<T> other)
    {
        IMultiSet<T> intersection = new MultiSet<>();
        Set<T>       underlyingIntersection = new TreeSet<>(getUnderlying());

        underlyingIntersection.retainAll(other.getUnderlying());

        underlyingIntersection
            .forEach(
                e ->
                    intersection.add(
                        e,
                        Long.min(
                            getMultiplicity(e),
                            other.getMultiplicity(e))));

        return intersection;
    }

    @Override
    public IMultiSet<T>
    makeSymmetricDifferenceWith(IMultiSet<T> other)
    {
        IMultiSet<T> symmetricDifference = new MultiSet<>();
        Set<T>       leftDifference = new TreeSet<>(getUnderlying());
        Set<T>       rightDifference = new TreeSet<>(other.getUnderlying());

        leftDifference.removeAll(other.getUnderlying());
        rightDifference.removeAll(getUnderlying());

        leftDifference
            .forEach(e -> symmetricDifference.add(e,getMultiplicity(e)));

        rightDifference
            .forEach(e -> symmetricDifference.add(e,other.getMultiplicity(e)));

        return symmetricDifference;
    }

    @Override
    public boolean
    isEmpty()
    {
        return mappings.isEmpty();
    }

    @Override
    public Stream<Pair<T,Long>>
    stream()
    {
        return
            mappings
                .entrySet()
                .stream()
                .map(entry -> Pair.create(entry.getKey(), entry.getValue().get()));
    }

    private long
    getLexicalDirection(IMultiSet<T> other)
    {
        SortedSet<T> thisUnderlying = (SortedSet<T>)getUnderlying();
        SortedSet<T> otherUnderlying = (SortedSet<T>)other.getUnderlying();
        Iterator<T>  thisIterator = thisUnderlying.iterator();
        Iterator<T>  otherIterator = otherUnderlying.iterator();

        while (thisIterator.hasNext() && otherIterator.hasNext())
        {
            long comparison = thisIterator.next().compareTo(otherIterator.next());

            if (comparison != 0)
                return comparison > 0 ? 1L : -1L;
        }

        if (!thisIterator.hasNext() && !otherIterator.hasNext())
            return 1L;

        return otherIterator.hasNext() ? -1L : 1L;

    }

    @Serial
    private void
    writeObject(ObjectOutputStream out)
        throws IOException
    {
        out.writeInt(mappings.size());
        for (Map.Entry<T,AtomicLong> entry: mappings.entrySet())
        {
            out.writeObject(entry.getKey());
            out.writeLong(entry.getValue().get());
        }
    }

    @SuppressWarnings("unchecked")
    @Serial
    private void
    readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        int size = in.readInt();

        mappings.clear();

        for (int i = 0;i < size;++i)
        {
            T    key = (T)in.readObject();
            long value = in.readLong();
            mappings.put(key,new AtomicLong(value));
        }
    }
}

//////////////////////////////////////////////////////////////////////////////
