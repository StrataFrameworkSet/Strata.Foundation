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
