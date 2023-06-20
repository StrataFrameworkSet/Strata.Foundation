//////////////////////////////////////////////////////////////////////////////
// IMultiSet.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.container;

import java.util.Set;
import java.util.stream.Stream;

public
interface IMultiSet<T extends Comparable<T>>
    extends Iterable<Pair<T,Long>>, Comparable<IMultiSet<T>>
{
    IMultiSet<T>
    add(T element);

    IMultiSet<T>
    add(T element,Long multiplicity);

    IMultiSet<T>
    remove(T element);

    IMultiSet<T>
    remove(T element,Long multiplicity);

    IMultiSet<T>
    removeFromUnderlying(T element);

    Set<T>
    getUnderlying();

    long
    getUnderlyingSize();

    long
    getCardinality();

    long
    getMultiplicity(T element);

    IMultiSet<T>
    getUnion(IMultiSet<T> other);

    IMultiSet<T>
    getIntersection(IMultiSet<T> other);

    IMultiSet<T>
    getSymmetricDifference(IMultiSet<T> other);

    long
    getDistance(IMultiSet<T> other);

    boolean
    isEmpty();

    Stream<Pair<T,Long>>
    stream();

}

//////////////////////////////////////////////////////////////////////////////