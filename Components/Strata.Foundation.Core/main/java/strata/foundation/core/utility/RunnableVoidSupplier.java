//////////////////////////////////////////////////////////////////////////////
// RunnableVoidSupplier.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.function.Supplier;

/**
 * <p>
 * Adapts a {@link Runnable} action into a {@link java.util.function.Supplier}
 * of {@link Void}, allowing a void, no-argument action to be used anywhere a
 * {@code Supplier<Void>} is required. Calling {@link #get()} runs the
 * wrapped {@code Runnable} and always returns {@code null}.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * Supplier&lt;Void&gt; supplier =
 *     new RunnableVoidSupplier(() -&gt; System.out.println("running"));
 * supplier.get();
 * </pre>
 */
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
