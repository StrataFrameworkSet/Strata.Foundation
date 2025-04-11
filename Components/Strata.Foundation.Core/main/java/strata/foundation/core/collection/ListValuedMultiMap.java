//////////////////////////////////////////////////////////////////////////////
// ListValuedMultiMap.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.util.*;

public
class ListValuedMultiMap<K,V>
    extends AbstractMultiMap<K,V>
{
    public
    ListValuedMultiMap() {}

    public
    ListValuedMultiMap(Map<K,? extends Collection<V>> source)
    {
        super(source);
    }

    public
    ListValuedMultiMap(IMultiMap<K,V> other)
    {
        super();
        putAll(other);
    }

    @Override
    protected List<V>
    collectionOf(V value)
    {
        List<V> output = new ArrayList<>(1);

        output.add(value);
        return output;
    }

    @Override
    protected List<V>
    collectionOf(Collection<V> values)
    {
        return new ArrayList<>(values);
    }

    @Override
    protected IMultiMap<K,V>
    copyOf(IMultiMap<K,V> other)
    {
        return new ListValuedMultiMap<>(other);
    }

    @Override
    protected IMultiMap<K,V>
    createEmpty()
    {
        return new ListValuedMultiMap<>();
    }
}

//////////////////////////////////////////////////////////////////////////////
