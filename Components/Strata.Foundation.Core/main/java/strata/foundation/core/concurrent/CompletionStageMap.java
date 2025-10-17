//////////////////////////////////////////////////////////////////////////////
// CompletionStageMap.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;

public
class CompletionStageMap<K,V>
    implements Serializable
{
    private Map<K,CompletionStage<V>> pending;

    public
    CompletionStageMap()
    {
        pending = new ConcurrentHashMap<>();
    }

    public CompletionStage<V>
    put(K key,CompletionStage<V> stage)
    {
        if (isPending(stage))
        {
            stage.whenComplete((x,e) -> remove(key));
            pending.put(key,stage);
        }

        return stage;
    }

    public CompletionStage<V>
    remove(K key)
    {
        if (containsKey(key) && isDone(pending.get(key)))
            return pending.remove(key);

        return null;
    }

    public CompletionStage<V>
    get(K key)
    {
        return pending.get(key);
    }

    public boolean
    containsKey(K key)
    {
        return pending.containsKey(key);
    }

    public boolean
    isPending(K key)
    {
        return pending.containsKey(key) && isPending(pending.get(key));
    }

    public boolean
    isEmpty()
    {
        return pending.isEmpty();
    }

    public Map<K,V>
    joinAll()
    {
        return
            pending
                .entrySet()
                .stream()
                .map(entry -> Map.entry(entry.getKey(),entry.getValue().toCompletableFuture()))
                .map(entry -> Map.entry(entry.getKey(),entry.getValue().join()))
                .collect(
                    ConcurrentHashMap::new,
                    (m,e) -> m.put(e.getKey(),e.getValue()),
                    Map::putAll);
    }

    private boolean
    isPending(CompletionStage<V> stage)
    {
        return !stage.toCompletableFuture().isDone();
    }

    public boolean
    isDone(CompletionStage<V> stage)
    {
        return stage.toCompletableFuture().isDone();
    }

    private void
    writeObject(ObjectOutputStream out)
    {
    }

    private void
    readObject(ObjectInputStream in)
    {
        pending = new ConcurrentHashMap<>();
    }
}

//////////////////////////////////////////////////////////////////////////////
