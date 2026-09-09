//////////////////////////////////////////////////////////////////////////////
// ThreadLocalMap.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * <p>
 * Implementation of {@link IThreadLocalMap} using
 * {@link java.util.concurrent.ConcurrentHashMap} keyed by thread id
 * for thread-safe per-thread value storage with cross-thread visibility.
 * </p>
 * <p>
 * <h4>Type Parameter</h4>
 * {@code <T>} - value type
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Per-thread value storage
 * IThreadLocalMap&lt;String&gt; map = new ThreadLocalMap&lt;&gt;();
 * map.insert("thread-specific-value");
 *
 * // Retrieval
 * Optional&lt;String&gt; value = map.get();
 * Collection&lt;String&gt; all  = map.getAll();
 * </pre>
 * </p>
 */
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
