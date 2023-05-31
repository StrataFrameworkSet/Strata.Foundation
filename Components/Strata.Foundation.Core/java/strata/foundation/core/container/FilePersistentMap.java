//////////////////////////////////////////////////////////////////////////////
// FilePersistentMap.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.container;

import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public
class FilePersistentMap<K,V>
    implements IPersistentMap<K,V>
{
    private final RandomAccessFile       persistentStore;
    private final Map<K,Pair<Long,Long>> index;

    public
    FilePersistentMap(RandomAccessFile ps)
    {
        persistentStore = ps;
        index = buildIndex();
    }

    @Override
    public IPersistentMap<K,V>
    put(K key,V value)
    {
        return this;
    }

    @Override
    public IPersistentMap<K,V>
    remove(K key)
    {
        return this;
    }

    @Override
    public Optional<V>
    get(K key)
    {
        return Optional.empty();
    }

    @Override
    public Collection<K>
    getKeys()
    {
        return null;
    }

    @Override
    public Collection<V>
    getValues()
    {
        return null;
    }

    @Override
    public Stream<K>
    getKeysAsStream()
    {
        return null;
    }

    @Override
    public Stream<V>
    getValuesAsStream()
    {
        return null;
    }

    @Override
    public boolean
    containsKey(K key)
    {
        return false;
    }

    @Override
    public boolean
    containsValue(K key,V value)
    {
        return false;
    }

    protected Map<K,Pair<Long,Long>>
    buildIndex()
    {
        Map<K,Pair<Long,Long>> scan =
            new HashMap<>();

        return scan;
    }
}

//////////////////////////////////////////////////////////////////////////////
