/// ///////////////////////////////////////////////////////////////////////////
// CompletableVoid.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

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
