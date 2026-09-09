/// ///////////////////////////////////////////////////////////////////////////
// ISortedSet.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.util.NavigableSet;
import java.util.SortedSet;

/**
 * <p>
 * Serializable extension of {@link java.util.SortedSet} and
 * {@link java.util.NavigableSet} that combines sorted set operations
 * with the Strata {@link ISequencedCollection} hierarchy.
 * </p>
 * <p>
 * <h4>Type Parameter</h4>
 * {@code <T>} - element type, must be {@link java.lang.Comparable}
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Creation
 * ISortedSet&lt;String&gt; sorted = SerializableSortedSet.of(List.of("c","a","b"));
 *
 * // Sorted access
 * String first = sorted.first();
 * String last  = sorted.last();
 * </pre>
 * </p>
 */
public
interface ISortedSet<T extends Comparable<? super T>>
    extends ISequencedCollection<T>, SortedSet<T>, NavigableSet<T> {}

//////////////////////////////////////////////////////////////////////////////