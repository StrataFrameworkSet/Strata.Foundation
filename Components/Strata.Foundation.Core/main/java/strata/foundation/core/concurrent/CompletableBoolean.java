/// ///////////////////////////////////////////////////////////////////////////
// CompletableBoolean.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public
class CompletableBoolean
    extends CompletableResult<Boolean,CompletedResult<Boolean>>
    implements ICompletableBoolean
{
    public
    CompletableBoolean(CompletableFuture<CompletedResult<Boolean>> future)
    {
        super(future);
    }

    public static CompletableBoolean
    supplyAsync(Supplier<CompletedResult<Boolean>> supplier)
    {
        return
            CompletableResult.supplyAsync(
                CompletableBoolean::new,supplier);
    }

    public static CompletableBoolean
    supplyAsync(Supplier<CompletedResult<Boolean>> supplier,Executor executor)
    {
        return
            CompletableResult.supplyAsync(
                CompletableBoolean::new,supplier,executor);
    }
}

//////////////////////////////////////////////////////////////////////////////
