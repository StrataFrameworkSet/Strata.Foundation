//////////////////////////////////////////////////////////////////////////////
// IMultiSet.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Stream;

/**
 * <p>
 * A multiset (bag) that tracks the multiplicity of each element,
 * supporting set-algebraic operations such as union, intersection,
 * and symmetric difference, along with distance computation.
 * See: <a href="https://en.wikipedia.org/wiki/Multiset">Multiset (Wikipedia)</a>
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * IMultiSet&lt;String&gt; bag = new MultiSet&lt;&gt;();
 *
 * // Adding elements with multiplicity
 * bag.add("apple").add("apple",3L);
 *
 * // Querying
 * long count     = bag.getMultiplicity("apple");
 * long totalSize = bag.getCardinality();
 *
 * // Set-algebraic operations
 * IMultiSet&lt;String&gt; union = bag.makeUnionWith(other);
 * </pre>
 *
 * @param <T> - element type, must be {@link java.lang.Comparable}
 */
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
