//////////////////////////////////////////////////////////////////////////////
// SupplierBasedCompletableSendResult.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import strata.foundation.core.concurrent.SupplierBasedCompletableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * <p>
 * Extension of {@link strata.foundation.core.concurrent.SupplierBasedCompletableFuture}
 * that defers creation of an {@link ICompletableSendResult} via
 * a {@link java.util.function.Supplier}.
 * </p>
 * <br/>
 * <b>Type Parameter</b><br/>
 * {@code <E>} - event type
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Deferred send result
 * SupplierBasedCompletableSendResult&lt;String&gt; deferred =
 *     SupplierBasedCompletableSendResult.supplyAsync(
 *         () -&gt; () -&gt; SendResult.of("event"));
 * </pre>
 */
public
class SupplierBasedCompletableSendResult<E>
    extends SupplierBasedCompletableFuture<ICompletableSendResult<E>>
{
    public
    SupplierBasedCompletableSendResult(CompletableFuture<Supplier<ICompletableSendResult<E>>> source)
    {
        super(source);
    }
}

//////////////////////////////////////////////////////////////////////////////
