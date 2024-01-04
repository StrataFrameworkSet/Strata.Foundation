//////////////////////////////////////////////////////////////////////////////
// SupplierBasedCompletableSendResult.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import strata.foundation.core.concurrent.SupplierBasedCompletableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

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
