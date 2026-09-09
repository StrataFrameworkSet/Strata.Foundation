//////////////////////////////////////////////////////////////////////////////
// ImmutableMultiMap.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * <p>
 * Immutable decorator for {@link IMultiMap} that delegates read
 * operations to the wrapped source and throws
 * {@link UnsupportedOperationException} on all mutating operations.
 * See: <a href="https://en.wikipedia.org/wiki/Immutable_object">Immutable object (Wikipedia)</a>
 * </p>
 * <p>
 * <h4>Type Parameters</h4>
 * <ul>
 * <li>{@code <K>} - key type</li>
 * <li>{@code <V>} - value type</li>
 * </ul>
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Creation from mutable source
 * IMultiMap&lt;String,Integer&gt; mutable = new ListValuedMultiMap&lt;&gt;();
 * mutable.put("a",1).put("b",2);
 * IMultiMap&lt;String,Integer&gt; immutable = ImmutableMultiMap.of(mutable);
 *
 * // Read operations succeed
 * Collection&lt;Integer&gt; values = immutable.get("a");
 *
 * // Mutating operations throw UnsupportedOperationException
 * // immutable.put("c",3); // throws
 * </pre>
 * </p>
 */
public
class ImmutableMultiMap<K,V>
    implements IMultiMap<K,V>
{
    private final IMultiMap<K,V> source;

    public
    ImmutableMultiMap(IMultiMap<K,V> source)
    {
        Objects.requireNonNull(source,"source must not be null");
        this.source = new ListValuedMultiMap<>(source);
    }

    @Override
    public IMultiMap<K,V>
    put(K key, V value)
    {
        throw new UnsupportedOperationException("ImmutableMultiMap cannot be modified");
    }

    @Override
    public IMultiMap<K,V>
    putAll(K key, Collection<V> values)
    {
        throw new UnsupportedOperationException("ImmutableMultiMap cannot be modified");
    }

    @Override
    public IMultiMap<K,V>
    putAll(IMultiMap<K,V> other)
    {
        throw new UnsupportedOperationException("ImmutableMultiMap cannot be modified");
    }

    @Override
    public IMultiMap<K,V>
    replace(K key, Collection<V> values)
    {
        throw new UnsupportedOperationException("ImmutableMultiMap cannot be modified");
    }

    @Override
    public IMultiMap<K,V>
    replaceAll(IMultiMap<K,V> other)
    {
        throw new UnsupportedOperationException("ImmutableMultiMap cannot be modified");
    }

    @Override
    public IMultiMap<K,V>
    remove(K key)
    {
        throw new UnsupportedOperationException("ImmutableMultiMap cannot be modified");
    }

    @Override
    public IMultiMap<K,V>
    remove(K key, V value)
    {
        throw new UnsupportedOperationException("ImmutableMultiMap cannot be modified");
    }

    @Override
    public IMultiMap<K,V>
    removeAll(K key, Collection<V> values)
    {
        throw new UnsupportedOperationException("ImmutableMultiMap cannot be modified");
    }

    @Override
    public IMultiMap<K,V>
    removeAll(IMultiMap<K,V> other)
    {
        throw new UnsupportedOperationException("ImmutableMultiMap cannot be modified");
    }

    @Override
    public IMultiMap<K,V>
    clear()
    {
        throw new UnsupportedOperationException("ImmutableMultiMap cannot be modified");
    }

    @Override
    public int
    size()
    {
        return source.size();
    }

    @Override
    public Set<K>
    keySet()
    {
        return Collections.unmodifiableSet(source.keySet());
    }

    @Override
    public Collection<Collection<V>>
    values()
    {
        return Collections.unmodifiableCollection(source.values());
    }

    @Override
    public Collection<Map.Entry<K,Collection<V>>>
    entrySet()
    {
        return Collections.unmodifiableCollection(source.entrySet());
    }

    @Override
    public Stream<Entry<K,Collection<V>>>
    stream()
    {
        return source.stream();
    }

    @Override
    public List<Entry<K,V>>
    flatten()
    {
        return Collections.unmodifiableList(source.flatten());
    }

    @Override
    public Collection<V>
    get(K key)
    {
        Collection<V> values = source.get(key);
        return values != null ? Collections.unmodifiableCollection(values) : null;
    }

    @Override
    public Collection<V>
    getOrDefault(K key, Collection<V> defaultValue)
    {
        Collection<V> values = source.getOrDefault(key, defaultValue);
        return values != null ? Collections.unmodifiableCollection(values) : null;
    }

    @Override
    public boolean
    isEmpty()
    {
        return source.isEmpty();
    }

    @Override
    public boolean
    containsKey(K key)
    {
        return source.containsKey(key);
    }

    @Override
    public boolean
    containsValue(V value)
    {
        return source.containsValue(value);
    }

    @Override
    public void
    forEach(BiConsumer<? super K, ? super Collection<V>> action)
    {
        source.forEach(action);
    }

    @Override
    public IMultiMap<K,V>
    makeUnionWith(IMultiMap<K,V> other)
    {
        return new ImmutableMultiMap<>(source.makeUnionWith(other));
    }

    @Override
    public IMultiMap<K,V>
    makeIntersectionWith(IMultiMap<K,V> other)
    {
        return new ImmutableMultiMap<>(source.makeIntersectionWith(other));
    }

    @Override
    public IMultiMap<K,V>
    makeSymmetricDifferenceWith(IMultiMap<K,V> other)
    {
        return new ImmutableMultiMap<>(source.makeSymmetricDifferenceWith(other));
    }

    @Override
    public Iterator<Entry<K,Collection<V>>>
    iterator()
    {
        return entrySet().iterator();
    }

    @Override
    public boolean
    equals(Object obj)
    {
        return source.equals(obj);
    }

    @Override
    public int
    hashCode()
    {
        return source.hashCode();
    }

    @Override
    public String
    toString()
    {
        return source.toString();
    }

    public static <K,V> ImmutableMultiMap<K,V>
    of(IMultiMap<K,V> source)
    {
        return new ImmutableMultiMap<>(source);
    }
}

//////////////////////////////////////////////////////////////////////////////
