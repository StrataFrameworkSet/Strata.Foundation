/// ///////////////////////////////////////////////////////////////////////////
// ISequencedCollection.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.util.SequencedCollection;

/**
 * <p>
 * Serializable extension of {@link java.util.SequencedCollection} that
 * combines sequenced element access with the Strata {@link ICollection}
 * hierarchy.
 * </p>
 * <p>
 * <h4>Type Parameter</h4>
 * {@code <T>} - element type
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Declaration via concrete type
 * ISequencedCollection&lt;String&gt; items = new SerializableList&lt;&gt;();
 * items.add("first");
 * items.add("second");
 *
 * // Sequenced access
 * String first = items.getFirst();
 * String last  = items.getLast();
 * </pre>
 * </p>
 */
public
interface ISequencedCollection<T>
    extends ICollection<T>, SequencedCollection<T> {}

//////////////////////////////////////////////////////////////////////////////