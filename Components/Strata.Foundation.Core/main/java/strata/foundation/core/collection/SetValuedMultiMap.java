/// ///////////////////////////////////////////////////////////////////////////
// SetValuedMultiMap.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Implementation of {@link IMultiMap} that stores values in
 * {@link java.util.Set} collections, ensuring unique values
 * per key.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * IMultiMap&lt;String,Integer&gt; map = new SetValuedMultiMap&lt;&gt;();
 *
 * // Adding values (duplicates ignored)
 * map.put("a",1).put("a",2).put("a",1);
 * Collection&lt;Integer&gt; values = map.get("a"); // [1, 2]
 *
 * // Copy construction
 * IMultiMap&lt;String,Integer&gt; copy = new SetValuedMultiMap&lt;&gt;(map);
 * </pre>
 *
 * @param <K> key type
 * @param <V> value type
 */
public
class SetValuedMultiMap<K,V>
    extends AbstractMultiMap<K,V>
{
    public
    SetValuedMultiMap() {}

    public
    SetValuedMultiMap(Map<K,? extends Collection<V>> source)
    {
        super(source);
    }

    public
    SetValuedMultiMap(IMultiMap<K,V> other)
    {
        super();
        putAll(other);
    }

    @Override
    protected Set<V>
    collectionOf(V value)
    {
        Set<V> output = new HashSet<>(1);

        output.add(value);
        return output;
    }

    @Override
    protected Set<V>
    collectionOf(Collection<V> values)
    {
        return new HashSet<>(values);
    }

    @Override
    protected IMultiMap<K,V>
    copyOf(IMultiMap<K,V> other)
    {
        return new SetValuedMultiMap<>(other);
    }

    @Override
    protected IMultiMap<K,V>
    createEmpty()
    {
        return new SetValuedMultiMap<>();
    }

    @Serial
    private void
    writeObject(ObjectOutputStream out)
        throws IOException
    {
        out.writeInt(size());

        for (Map.Entry<K, Collection<V>> entry : entrySet())
        {
            out.writeObject(entry.getKey());
            Collection<V> values = entry.getValue();
            out.writeInt(values.size());
            for (V value : values)
                out.writeObject(value);
        }
    }

    @SuppressWarnings("unchecked")
    @Serial
    private void
    readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        int mapSize = in.readInt();

        clear();

        for (int i = 0; i < mapSize; i++)
        {
            K      key = (K)in.readObject();
            int    valuesSize = in.readInt();
            Set<V> values = new HashSet<>(valuesSize);

            for (int j = 0; j < valuesSize; j++)
            {
                V value = (V)in.readObject();
                values.add(value);
            }

            putAll(key, values);
        }
    }
}

//////////////////////////////////////////////////////////////////////////////
