//////////////////////////////////////////////////////////////////////////////
// IMultiSet.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Stream;

public
interface IMultiSet<T extends Comparable<T>>
    extends Iterable<Pair<T,Long>>, Comparable<IMultiSet<T>>, Serializable
{
    IMultiSet<T>
    add(T element);

    IMultiSet<T>
    add(T element,Long multiplicity);

    IMultiSet<T>
    addAll(IMultiSet<T> other);

    IMultiSet<T>
    remove(T element);

    IMultiSet<T>
    remove(T element,Long multiplicity);

    IMultiSet<T>
    replace(T element,Long multiplicity);

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

    long
    getDistance(IMultiSet<T> other);

    IMultiSet<T>
    makeUnionWith(IMultiSet<T> other);

    IMultiSet<T>
    makeIntersectionWith(IMultiSet<T> other);

    IMultiSet<T>
    makeSymmetricDifferenceWith(IMultiSet<T> other);

    boolean
    isEmpty();

    Stream<Pair<T,Long>>
    stream();

}

//////////////////////////////////////////////////////////////////////////////