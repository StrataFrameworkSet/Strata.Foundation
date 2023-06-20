//////////////////////////////////////////////////////////////////////////////
// IPersistentMap.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.container;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

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