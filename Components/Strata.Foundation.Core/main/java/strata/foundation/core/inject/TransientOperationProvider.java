//////////////////////////////////////////////////////////////////////////////
// TransientOperationProvider.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;


import jakarta.inject.Inject;

/**
 * <p>
 * An {@link IOperationProvider} that creates a new {@link Operation} on
 * every call to {@code get()}, backed by an injected {@link IInjector}.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * IOperationProvider provider = new TransientOperationProvider(injector);
 *
 * try (Operation operation = provider.get())
 * {
 *     IConfiguration configuration =
 *         operation.getInstance(IConfiguration.class);
 * }
 * </pre>
 * </p>
 */
public
class TransientOperationProvider
    implements IOperationProvider
{
    private final IInjector itsInjector;

    @Inject
    public
    TransientOperationProvider(IInjector injector)
    {
        itsInjector = injector;
    }

    @Override
    public Operation
    get()
    {
        return new Operation(itsInjector);
    }
}

//////////////////////////////////////////////////////////////////////////////
