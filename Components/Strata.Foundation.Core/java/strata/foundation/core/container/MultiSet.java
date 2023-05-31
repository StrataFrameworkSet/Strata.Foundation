//////////////////////////////////////////////////////////////////////////////
// MultiSet.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.container;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public
class MultiSet<T extends Comparable<T>>
    implements IMultiSet<T>
{
    private final Map<T,AtomicLong> itsImplementation;

    /************************************************************************
     * {@inheritDoc}
     */
    public
    MultiSet()
    {
        itsImplementation = new TreeMap<>();
    }

    /************************************************************************
     * {@inheritDoc}
     */
    @Override
    public Iterator<Pair<T,Long>>
    iterator()
    {
        return stream().iterator();
    }

    /************************************************************************
     * {@inheritDoc}
     */
    @Override
    public Spliterator<Pair<T,Long>>
    spliterator()
    {
        return stream().spliterator();
    }

    /************************************************************************
     * {@inheritDoc}
     */
    @Override
    public int
    compareTo(IMultiSet<T> other)
    {
        return
            Long
                .valueOf(
                    getLexicalDirection(other)* getDistance(other))
                .intValue();
    }

    /************************************************************************
     * {@inheritDoc}
     */
    @Override
    public IMultiSet<T>
    add(T element)
    {
        return add(element,1L);
    }

    @Override
    public IMultiSet<T>
    add(T element,Long multiplicity)
    {
        if (itsImplementation.containsKey(element))
            itsImplementation
                .get(element)
                .addAndGet(multiplicity);
        else
            itsImplementation.put(element,new AtomicLong(multiplicity));

        return this;
    }

    /************************************************************************
     * {@inheritDoc}
     */
    @Override
    public IMultiSet<T>
    remove(T element)
    {
        return remove(element,1L);
    }

    @Override
    public IMultiSet<T>
    remove(T element,Long multiplicity)
    {
        if (itsImplementation.containsKey(element))
        {
            AtomicLong mult =
                itsImplementation.get(element);

            if (mult.get() >= multiplicity)
                mult.set(mult.get() - multiplicity);
        }

        return this;
    }

    /************************************************************************
     * {@inheritDoc}
     */
    @Override
    public IMultiSet<T>
    removeFromUnderlying(T element)
    {
        itsImplementation.remove(element);
        return this;
    }

    /************************************************************************
     * {@inheritDoc}
     */
    @Override
    public Set<T>
    getUnderlying()
    {
        return new TreeSet<>(itsImplementation.keySet());
    }

    /************************************************************************
     * {@inheritDoc}
     */
    @Override
    public long
    getUnderlyingSize()
    {
        return itsImplementation.size();
    }

    @Override
    public long
    getCardinality()
    {
        return
            itsImplementation
                .values()
                .stream()
                .map(m -> m.get())
                .reduce(0L,Long::sum);
    }

    /************************************************************************
     * {@inheritDoc}
     */
    @Override
    public long
    getMultiplicity(T element)
    {
        return
            itsImplementation
                .getOrDefault(
                    element,
                    new AtomicLong(0L))
                .get();
    }

    /************************************************************************
     * {@inheritDoc}
     */
    @Override
    public IMultiSet<T>
    getUnion(IMultiSet<T> other)
    {
        IMultiSet<T> union = new MultiSet<>();
        Set<T>       underlyingUnion = new TreeSet<>(getUnderlying());

        underlyingUnion.addAll(other.getUnderlying());

        underlyingUnion
            .stream()
            .forEach(
                e ->
                    union.add(
                        e,
                        Long.max(
                            getMultiplicity(e),
                            other.getMultiplicity(e))));

        return union;
    }

    /************************************************************************
     * {@inheritDoc}
     */
    @Override
    public IMultiSet<T>
    getIntersection(IMultiSet<T> other)
    {
        IMultiSet<T> intersection = new MultiSet<>();
        Set<T>       underlyingIntersection = new TreeSet<>(getUnderlying());

        underlyingIntersection.retainAll(other.getUnderlying());

        underlyingIntersection
            .stream()
            .forEach(
                e ->
                    intersection.add(
                        e,
                        Long.min(
                            getMultiplicity(e),
                            other.getMultiplicity(e))));

        return intersection;
    }

    /************************************************************************
     * {@inheritDoc}
     */
    @Override
    public IMultiSet<T>
    getSymmetricDifference(IMultiSet<T> other)
    {
        IMultiSet<T> symmetricDifference = new MultiSet<>();
        Set<T>       leftDifference = new TreeSet<>(getUnderlying());
        Set<T>       rightDifference = new TreeSet<>(other.getUnderlying());

        leftDifference.removeAll(other.getUnderlying());
        rightDifference.removeAll(getUnderlying());

        leftDifference
            .stream()
            .forEach(e -> symmetricDifference.add(e,getMultiplicity(e)));

        rightDifference
            .stream()
            .forEach(e -> symmetricDifference.add(e,other.getMultiplicity(e)));

        return symmetricDifference;
    }

    /************************************************************************
     * {@inheritDoc}
     */
    @Override
    public long
    getDistance(IMultiSet<T> other)
    {
        return
            getSymmetricDifference(other).getCardinality() +
                getIntersection(other)
                    .getUnderlying()
                    .stream()
                    .map(
                        e ->
                            Math.abs(
                                getMultiplicity(e) - other.getMultiplicity(e)))
                    .reduce(0L,Long::sum);
    }

    /************************************************************************
     * {@inheritDoc}
     */
    @Override
    public boolean
    isEmpty()
    {
        return itsImplementation.isEmpty();
    }

    /************************************************************************
     * {@inheritDoc}
     */
    @Override
    public Stream<Pair<T,Long>>
    stream()
    {
        return
            itsImplementation
                .entrySet()
                .stream()
                .map(e -> Pair.create(e.getKey(),e.getValue().get()));
    }

    /************************************************************************
     * Finds the lexical direction of a comparison between two multisets.
     *
     * @param other multiset being lexically compared to this multiset
     * @return -1 if this multiset is lexically < the other multiset
     *          1 if this multiset is lexically >= the other multiset
     *
     */
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
}

//////////////////////////////////////////////////////////////////////////////
