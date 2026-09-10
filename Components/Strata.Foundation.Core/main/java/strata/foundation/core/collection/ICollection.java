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
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Declaration via concrete type
 * ICollection&lt;String&gt; items = new SerializableList&lt;&gt;();
 * items.add("foo");
 *
 * // Size and containment
 * int size = items.size();
 * boolean has = items.contains("foo");
 * </pre>
 *
 * @param <T> element type
 */
public
interface ICollection<T>
    extends Collection<T>, Serializable {}

//////////////////////////////////////////////////////////////////////////////
