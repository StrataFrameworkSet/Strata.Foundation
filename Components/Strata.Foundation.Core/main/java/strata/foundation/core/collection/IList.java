//////////////////////////////////////////////////////////////////////////////
// IList.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.util.List;

/**
 * <p>
 * Serializable extension of {@link java.util.List} that combines
 * indexed list operations with the Strata {@link ISequencedCollection}
 * hierarchy.
 * </p>
 * <p>
 * <h4>Type Parameter</h4>
 * {@code <T>} - element type
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Creation
 * IList&lt;String&gt; list = SerializableList.of(List.of("a","b","c"));
 *
 * // Indexed access
 * String item = list.get(0);
 * int index   = list.indexOf("b");
 * </pre>
 * </p>
 */
public
interface IList<T>
    extends ISequencedCollection<T>, List<T> {}

//////////////////////////////////////////////////////////////////////////////