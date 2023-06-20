//////////////////////////////////////////////////////////////////////////////
// Awaiter.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.concurrent.CompletionStage;

public
class Awaiter
{
    public static <T> T
    await(CompletionStage<T> stage)
    {
        if (stage.toCompletableFuture().isCompletedExceptionally())
        {
            throw
                stage
                    .toCompletableFuture()
                    .handle((x,e) -> new RuntimeException(e))
                    .join();
        }

        return stage.toCompletableFuture().join();
    }
}

//////////////////////////////////////////////////////////////////////////////
