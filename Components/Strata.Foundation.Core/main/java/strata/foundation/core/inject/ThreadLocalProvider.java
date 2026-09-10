//////////////////////////////////////////////////////////////////////////////
// ThreadLocalProvider.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;

import jakarta.inject.Provider;

/**
 * <p>
 * A {@link jakarta.inject.Provider} that lazily creates and caches one
 * instance per thread using
 * <a href="https://en.wikipedia.org/wiki/Thread-local_storage">Thread-local
 * storage</a>, delegating creation to a wrapped source provider the first
 * time each thread requests a value.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * Provider&lt;Connection&gt; source = () -&gt; openConnection();
 * Provider&lt;Connection&gt; threadLocal = new ThreadLocalProvider&lt;&gt;(source);
 *
 * Connection connection = threadLocal.get();
 * </pre>
 *
 * @param <T> the type of value provided
 */
public
class ThreadLocalProvider<T>
    implements Provider<T>
{
    private final Provider<T> itsSource;
    private ThreadLocal<T>    itsInstances;
    
    public
    ThreadLocalProvider(Provider<T> source)
    {
        itsSource = source;
        itsInstances =
            ThreadLocal.withInitial(() -> itsSource.get());
    }

    @Override
    public T 
    get()
    {
        return itsInstances.get();
    }

}

//////////////////////////////////////////////////////////////////////////////
