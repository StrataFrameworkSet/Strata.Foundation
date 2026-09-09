//////////////////////////////////////////////////////////////////////////////
// IThreadLocalMap.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * <p>
 * Thread-local value store that associates one value per thread,
 * with access to all stored values across threads.
 * See: <a href="https://en.wikipedia.org/wiki/Thread-local_storage">Thread-local storage (Wikipedia)</a>
 * </p>
 * <p>
 * <h4>Type Parameter</h4>
 * {@code <T>} - value type
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Creation and thread-local storage
 * IThreadLocalMap&lt;String&gt; map = new ThreadLocalMap&lt;&gt;();
 * map.insert("value-for-this-thread");
 *
 * // Retrieval
 * Optional&lt;String&gt; value = map.get();
 * Collection&lt;String&gt; all  = map.getAll();
 * </pre>
 * </p>
 */
public
interface IThreadLocalMap<T>
{
    IThreadLocalMap<T>
    insert(T value);

    IThreadLocalMap<T>
    remove(T value);

    IThreadLocalMap<T>
    remove();

    Optional<T>
    get();

    Collection<T>
    getAll();

    Stream<T>
    getAllAsStream();
}

//////////////////////////////////////////////////////////////////////////////