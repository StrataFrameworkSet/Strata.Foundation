//////////////////////////////////////////////////////////////////////////////
// Awaiter.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.concurrent.CompletionStage;

/**
 * <p>
 * Utility for synchronously blocking on a
 * {@link java.util.concurrent.CompletionStage} and returning
 * its result.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Block until CompletionStage completes
 * CompletionStage&lt;String&gt; stage = CompletableFuture.supplyAsync(() -&gt; "done");
 * String result = Awaiter.await(stage);
 * </pre>
 */
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
