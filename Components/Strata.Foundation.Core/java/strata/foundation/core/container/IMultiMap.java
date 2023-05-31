// ##########################################################################
// # File Name:	IMultiMap.java
// ##########################################################################

package strata.foundation.core.container;

import java.util.Collection;
import java.util.List;

public 
interface IMultiMap<K,V>
{
    IMultiMap<K,V>
    put(K key,V value);
    
    IMultiMap<K,V>
    remove(K key);
    
    IMultiMap<K,V>
    remove(K key,V value);
    
    Collection<V>
    get(K key);
    
    V
    get(K key,int index);
    
    int
    getCardinality(K key);
    
    Collection<K>
    getKeys();
    
    Collection<List<V>>
    getValues();
    
    boolean
    containsKey(K key);
    
    boolean
    containsValue(K key,V value);
}

// ##########################################################################
