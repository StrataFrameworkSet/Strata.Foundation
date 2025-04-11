/// ///////////////////////////////////////////////////////////////////////////
// SetValuedMultiMap.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

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
}

//////////////////////////////////////////////////////////////////////////////
