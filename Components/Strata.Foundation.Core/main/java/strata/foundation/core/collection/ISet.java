/// ///////////////////////////////////////////////////////////////////////////
// ISet.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.util.Set;

/**
 * <p>
 * Serializable extension of {@link java.util.Set} that combines
 * set operations with the Strata {@link ICollection} hierarchy.
 * </p>
 * <p>
 * <h4>Type Parameter</h4>
 * {@code <T>} - element type
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Creation
 * ISet&lt;String&gt; set = SerializableSet.of(Set.of("a","b","c"));
 *
 * // Set operations
 * boolean has = set.contains("a");
 * int size    = set.size();
 * </pre>
 * </p>
 */
public
interface ISet<T>
    extends Set<T>, ICollection<T> {}

//////////////////////////////////////////////////////////////////////////////