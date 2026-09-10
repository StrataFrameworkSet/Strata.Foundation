//////////////////////////////////////////////////////////////////////////////
// CompletableVoid.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * <p>
 * Specialized {@link CompletableResult} for asynchronous operations
 * that produce no return value, implementing {@link ICompletableVoid}.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Fire-and-forget async operation
 * CompletableVoid result =
 *     CompletableVoid.runAsync(() -&gt; System.out.println("done"));
 *
 * // Wait for completion
 * result.join();
 * </pre>
 */
public
class CompletableVoid
    extends CompletableResult<Void,CompletedResult<Void>>
    implements ICompletableVoid
{
    public
    CompletableVoid(CompletableFuture<CompletedResult<Void>> future)
    {
        super(future);
    }

    public static CompletableVoid
    runAsync(Runnable runnable)
    {
        return
            CompletableResult
                .supplyAsync(
                    CompletableVoid::new,
                    () -> {
                    runnable.run();
                    return null;
                    });
    }

    public static CompletableVoid
    runAsync(Runnable runnable,Executor executor)
    {
        return
            CompletableResult
                .supplyAsync(
                    CompletableVoid::new,
                    () ->
                        {
                            runnable.run();
                            return null;
                        },
                    executor);
    }
}

//////////////////////////////////////////////////////////////////////////////
