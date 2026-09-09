/// ///////////////////////////////////////////////////////////////////////////
// ICollection.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 * Serializable extension of {@link java.util.Collection} that serves
 * as the base interface for the Strata collection hierarchy.
 * </p>
 * <p>
 * <h4>Type Parameter</h4>
 * {@code <T>} - element type
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Declaration via concrete type
 * ICollection&lt;String&gt; items = new SerializableList&lt;&gt;();
 * items.add("foo");
 *
 * // Size and containment
 * int size = items.size();
 * boolean has = items.contains("foo");
 * </pre>
 * </p>
 */
public
interface ICollection<T>
    extends Collection<T>, Serializable {}

//////////////////////////////////////////////////////////////////////////////