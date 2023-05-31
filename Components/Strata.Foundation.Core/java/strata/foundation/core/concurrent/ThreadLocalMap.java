//////////////////////////////////////////////////////////////////////////////
// ThreadLocalMap.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public
class ThreadLocalMap<T>
    implements IThreadLocalMap<T>
{
    private final ConcurrentHashMap<Long,T> values;

    public
    ThreadLocalMap()
    {
        values = new ConcurrentHashMap<>();
    }

    @Override
    public IThreadLocalMap<T>
    insert(T value)
    {
        values.put(Thread.currentThread().getId(),value);
        return this;
    }

    @Override
    public IThreadLocalMap<T>
    remove(T value)
    {
        values.remove(Thread.currentThread().getId(),value);
        return this;
    }

    @Override
    public IThreadLocalMap<T>
    remove()
    {
        values.remove(Thread.currentThread().getId());
        return this;
    }

    @Override
    public Optional<T>
    get()
    {
        return Optional.of(values.get(Thread.currentThread().getId()));
    }

    @Override
    public Collection<T>
    getAll()
    {
        return values.values();
    }

    @Override
    public Stream<T>
    getAllAsStream()
    {
        return getAll().stream();
    }
}

//////////////////////////////////////////////////////////////////////////////
