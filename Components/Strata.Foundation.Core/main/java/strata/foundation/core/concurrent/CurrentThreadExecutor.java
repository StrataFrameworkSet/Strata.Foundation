//////////////////////////////////////////////////////////////////////////////
// CurrentThreadExecutor.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.concurrent.Executor;

/**
 * <p>
 * An {@link java.util.concurrent.Executor} that runs commands
 * synchronously on the calling thread, useful for testing or
 * single-threaded execution contexts.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Synchronous execution on current thread
 * Executor executor = new CurrentThreadExecutor();
 * executor.execute(() -&gt; System.out.println("runs immediately"));
 * </pre>
 * </p>
 */
public
class CurrentThreadExecutor
    implements Executor
{
    @Override
    public void
    execute(Runnable command)
    {
        command.run();
    }
}

//////////////////////////////////////////////////////////////////////////////
