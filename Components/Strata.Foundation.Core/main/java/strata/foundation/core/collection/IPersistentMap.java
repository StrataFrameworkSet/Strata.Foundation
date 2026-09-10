//////////////////////////////////////////////////////////////////////////////
// IPersistentMap.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * <p>
 * A persistent map that preserves previous versions on mutation,
 * returning a new map instance for each modification.
 * See: <a href="https://en.wikipedia.org/wiki/Persistent_data_structure">Persistent data structure (Wikipedia)</a>
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation and mutation
 * IPersistentMap&lt;String,Integer&gt; map1 = new FilePersistentMap&lt;&gt;(file);
 * IPersistentMap&lt;String,Integer&gt; map2 = map1.put("key",42);
 *
 * // Retrieval
 * Optional&lt;Integer&gt; value = map2.get("key");
 * boolean has = map2.containsKey("key");
 * </pre>
 *
 * @param <K> key type
 * @param <V> value type
 */
public
interface IPersistentMap<K,V>
{
    IPersistentMap<K,V>
    put(K key,V value);

    IPersistentMap<K,V>
    remove(K key);

    Optional<V>
    get(K key);

    Collection<K>
    getKeys();

    Collection<V>
    getValues();

    Stream<K>
    getKeysAsStream();

    Stream<V>
    getValuesAsStream();

    boolean
    containsKey(K key);

    boolean
    containsValue(K key,V value);

}

//////////////////////////////////////////////////////////////////////////////
