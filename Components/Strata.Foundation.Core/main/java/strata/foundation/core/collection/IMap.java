//////////////////////////////////////////////////////////////////////////////
// IMap.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * Serializable extension of {@link java.util.Map} for the Strata
 * collection hierarchy.
 * </p>
 * <br/>
 * <b>Type Parameters</b><br/>
 * <ul>
 * <li>{@code <K>} - key type</li>
 * <li>{@code <V>} - value type</li>
 * </ul>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * IMap&lt;String,Integer&gt; map = SerializableMap.of(Map.of("a",1,"b",2));
 *
 * // Map operations
 * Integer value = map.get("a");
 * boolean has   = map.containsKey("b");
 * </pre>
 */
public
interface IMap<K,V>
    extends Map<K,V>, Serializable {}

//////////////////////////////////////////////////////////////////////////////
