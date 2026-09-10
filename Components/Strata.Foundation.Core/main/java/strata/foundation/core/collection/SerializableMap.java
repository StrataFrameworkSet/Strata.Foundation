/// ///////////////////////////////////////////////////////////////////////////
// SerializableMap.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Serializable implementation of {@link IMap} that extends
 * {@link java.util.HashMap} to combine standard map behavior
 * with Strata collection serialization support.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation from existing map
 * IMap&lt;String,Integer&gt; map = SerializableMap.of(Map.of("a",1,"b",2));
 *
 * // Creation from iterable of entries
 * IMap&lt;String,Integer&gt; map = SerializableMap.ofIterable(entries);
 *
 * // Direct construction
 * SerializableMap&lt;String,Integer&gt; map = new SerializableMap&lt;&gt;();
 * map.put("key",42);
 * </pre>
 *
 * @param <K> - key type
 * @param <V> - value type
 */
public
class SerializableMap<K,V>
    extends HashMap<K,V>
    implements IMap<K,V>
{
    public
    SerializableMap()
    {
        super();
    }

    public
    SerializableMap(int initialCapacity,float loadFactor)
    {
        super(initialCapacity,loadFactor);
    }

    public
    SerializableMap(int initialCapacity)
    {
        super(initialCapacity);
    }

    public
    SerializableMap(Map<? extends K,? extends V> m)
    {
        super(m);
    }

    public static <K,V> SerializableMap<K,V>
    of(Map<? extends K,? extends V> m)
    {
        return new SerializableMap<>(m);
    }

    public static <K,V> SerializableMap<K,V>
    ofIterable(Iterable<? extends Entry<? extends K,? extends V>> iterable)
    {
        SerializableMap<K,V> map = new SerializableMap<>();

        for (Entry<? extends K,? extends V> entry : iterable)
            map.put(entry.getKey(),entry.getValue());

        return map;
    }

    @Serial
    private void
    writeObject(ObjectOutputStream out)
        throws IOException
    {
        out.writeInt(size());

        for (Entry<K,V> entry : entrySet())
        {
            out.writeObject(entry.getKey());
            out.writeObject(entry.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    @Serial
    private void
    readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        int size = in.readInt();

        for (int i = 0;i < size;i++)
        {
            K key = (K)in.readObject();
            V value = (V)in.readObject();

            put(key,value);
        }
    }
}

//////////////////////////////////////////////////////////////////////////////
