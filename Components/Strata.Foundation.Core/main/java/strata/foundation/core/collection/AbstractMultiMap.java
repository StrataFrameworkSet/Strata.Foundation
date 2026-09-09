/// ///////////////////////////////////////////////////////////////////////////
// AbstractMultiMap.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import java.util.Map.Entry;

/**
 * <p>
 * Abstract base implementation of {@link IMultiMap} backed by a
 * {@link java.util.Map} of keys to value collections, providing
 * common multimap operations while delegating value-collection
 * creation to subclasses via template methods.
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
 * // Extend to provide list-valued collections
 * IMultiMap&lt;String,Integer&gt; map = new ListValuedMultiMap&lt;&gt;();
 *
 * // Extend to provide set-valued collections
 * IMultiMap&lt;String,Integer&gt; map = new SetValuedMultiMap&lt;&gt;();
 * </pre>
 * </p>
 */
public abstract
class AbstractMultiMap<K,V>
    implements IMultiMap<K,V>
{
    private final Map<K,Collection<V>> mappings;

    protected
    AbstractMultiMap()
    {
        mappings = new HashMap<>();
    }

    protected
    AbstractMultiMap(Map<K,? extends Collection<V>> source)
    {
        mappings = new HashMap<>(source);
    }

    @Override
    public Iterator<Entry<K,Collection<V>>>
    iterator()
    {
        return mappings.entrySet().iterator();
    }

    @Override
    public IMultiMap<K,V>
    put(K key,V value)
    {
        mappings.merge(
            key,
            collectionOf(value),
            (x,y) -> {x.addAll(y); return x;});
        return this;
    }

    @Override
    public IMultiMap<K,V>
    putAll(K key,Collection<V> values)
    {
        mappings.merge(
            key,
            collectionOf(values),
            (x,y) -> {x.addAll(y); return x;});
        return this;
    }

    @Override
    public IMultiMap<K,V>
    putAll(IMultiMap<K,V> other)
    {
        other.forEach((k,v)-> putAll(k,v));
        return this;
    }

    @Override
    public IMultiMap<K,V>
    replace(K key,Collection<V> values)
    {
        mappings.replace(key,values);
        return this;
    }

    @Override
    public IMultiMap<K,V>
    replaceAll(IMultiMap<K,V> other)
    {
        other.forEach((k,v)-> replace(k,v));
        return this;
    }

    @Override
    public IMultiMap<K,V>
    remove(K key)
    {
        mappings.remove(key);
        return this;
    }

    @Override
    public IMultiMap<K,V>
    remove(K key,V value)
    {
        if (mappings.containsKey(key))
            mappings
                .get(key)
                .remove(value);

        return this;
    }

    @Override
    public IMultiMap<K,V>
    removeAll(K key,Collection<V> values)
    {
        if (mappings.containsKey(key))
            mappings
                .get(key)
                .removeAll(values);

        return this;
    }

    @Override
    public IMultiMap<K,V>
    removeAll(IMultiMap<K,V> other)
    {
        other.forEach((k,v) -> removeAll(k,v));
        return this;
    }

    @Override
    public IMultiMap<K,V>
    clear()
    {
        mappings.clear();
        return this;
    }

    @Override
    public int
    size()
    {
        return mappings.size();
    }

    @Override
    public Set<K>
    keySet()
    {
        return mappings.keySet();
    }

    @Override
    public Collection<Collection<V>>
    values()
    {
        return mappings.values();
    }

    @Override
    public Collection<Entry<K,Collection<V>>>
    entrySet()
    {
        return mappings.entrySet();
    }

    @Override
    public Stream<Entry<K,Collection<V>>>
    stream()
    {
        return
            mappings
                .entrySet()
                .stream();
    }

    @Override
    public List<Entry<K,V>>
    flatten()
    {
        List<Entry<K,V>> result = new ArrayList<>();

        mappings
            .forEach(
                (key,values) ->
                    values.forEach(value -> result.add(Map.entry(key,value))));

        return result;
    }

    @Override
    public Collection<V>
    get(K key)
    {
        return mappings.get(key);
    }

    @Override
    public Collection<V>
    getOrDefault(K key,Collection<V> defaultValue)
    {
        return mappings.getOrDefault(key,collectionOf(defaultValue));
    }

    @Override
    public boolean
    isEmpty()
    {
        return mappings.isEmpty();
    }

    @Override
    public boolean
    containsKey(K key)
    {
        return mappings.containsKey(key);
    }

    @Override
    public boolean
    containsValue(V value)
    {
        return mappings.containsValue(value);
    }

    @Override
    public void
    forEach(BiConsumer<? super K,? super Collection<V>> action)
    {
        mappings.forEach(action);
    }

    @Override
    public IMultiMap<K,V>
    makeUnionWith(IMultiMap<K,V> other)
    {
        IMultiMap<K,V> result = copyOf(this);

        return result.putAll(other);
    }

    @Override
    public IMultiMap<K,V>
    makeIntersectionWith(IMultiMap<K,V> other)
    {
        IMultiMap<K,V> result = createEmpty();

        findIntersectingKeys(keySet(),other.keySet())
            .forEach(
                k->
                {
                    Collection<V> intersectingValues = get(k);

                    if (intersectingValues != null)
                        intersectingValues.retainAll(other.get(k));

                    result.putAll(k,intersectingValues);
                });

        return result;
    }

    @Override
    public IMultiMap<K,V>
    makeSymmetricDifferenceWith(IMultiMap<K,V> other)
    {
        IMultiMap<K,V> result = createEmpty();

        findNonIntersectingKeys(keySet(),other.keySet())
            .forEach(
                k->
                {
                    if (containsKey(k))
                        result.putAll(k,get(k));
                    else if (other.containsKey(k))
                        result.putAll(k,other.get(k));

                });

        return result;
    }

    protected abstract Collection<V>
    collectionOf(V value);

    protected abstract Collection<V>
    collectionOf(Collection<V> values);

    protected abstract IMultiMap<K,V>
    copyOf(IMultiMap<K,V> other);

    protected abstract IMultiMap<K,V>
    createEmpty();

    private Set<K>
    findIntersectingKeys(Set<K> x,Set<K> y)
    {
        Set<K> intersectingKeys = new HashSet<>(x);

        intersectingKeys.retainAll(y);
        return intersectingKeys;
    }

    private Set<K>
    findNonIntersectingKeys(Set<K> x,Set<K> y)
    {
        Set<K> intersectingKeys = new HashSet<>(x);
        Set<K> nonIntersectingKeys = new HashSet<>(x);

        intersectingKeys.retainAll(y);
        nonIntersectingKeys.addAll(y);
        nonIntersectingKeys.removeAll(intersectingKeys);

        return nonIntersectingKeys;
    }
}

//////////////////////////////////////////////////////////////////////////////
