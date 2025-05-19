/// ///////////////////////////////////////////////////////////////////////////
// RunnableVoidSupplier.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.function.Supplier;

public
class RunnableVoidSupplier
    implements Supplier<Void>
{
    private final Runnable runnable;

    public
    RunnableVoidSupplier(Runnable runnable)
    {
        this.runnable = runnable;
    }

    @Override
    public Void
    get()
    {
        runnable.run();
        return null;
    }
}

//////////////////////////////////////////////////////////////////////////////
