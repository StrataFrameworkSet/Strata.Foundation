//////////////////////////////////////////////////////////////////////////////
// IMultiMap.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public
interface IMultiMap<K,V>
    extends Iterable<Entry<K,Collection<V>>>
{
    IMultiMap<K,V>
    put(K key,V value);

    IMultiMap<K,V>
    putAll(K key,Collection<V> values);

    IMultiMap<K,V>
    putAll(IMultiMap<K,V> other);

    IMultiMap<K,V>
    replace(K key,Collection<V> values);

    IMultiMap<K,V>
    replaceAll(IMultiMap<K,V> other);

    IMultiMap<K,V>
    remove(K key);

    IMultiMap<K,V>
    remove(K key,V value);

    IMultiMap<K,V>
    removeAll(K key,Collection<V> values);

    IMultiMap<K,V>
    removeAll(IMultiMap<K,V> other);

    IMultiMap<K,V>
    clear();

    int
    size();

    Set<K>
    keySet();

    Collection<Collection<V>>
    values();

    Collection<Entry<K,Collection<V>>>
    entrySet();

    Stream<Entry<K,Collection<V>>>
    stream();

    List<Entry<K,V>>
    flatten();

    Collection<V>
    get(K key);

    Collection<V>
    getOrDefault(K key,Collection<V> defaultValue);

    boolean
    isEmpty();

    boolean
    containsKey(K key);

    boolean
    containsValue(V value);

    void
    forEach(BiConsumer<? super K,? super Collection<V>> action);

    IMultiMap<K,V>
    makeUnionWith(IMultiMap<K,V> other);

    IMultiMap<K,V>
    makeIntersectionWith(IMultiMap<K,V> other);

    IMultiMap<K,V>
    makeSymmetricDifferenceWith(IMultiMap<K,V> other);
}

//////////////////////////////////////////////////////////////////////////////