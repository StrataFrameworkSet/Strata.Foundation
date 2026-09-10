//////////////////////////////////////////////////////////////////////////////
// IOperationProvider.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;


import jakarta.inject.Provider;

/**
 * <p>
 * A {@link jakarta.inject.Provider} of {@link Operation}, used to obtain a
 * new unit-of-work scope on demand.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * IOperationProvider provider = new TransientOperationProvider(injector);
 *
 * try (Operation operation = provider.get())
 * {
 *     IConfiguration configuration =
 *         operation.getInstance(IConfiguration.class);
 * }
 * </pre>
 */
public
interface IOperationProvider
    extends Provider<Operation>
{}

//////////////////////////////////////////////////////////////////////////////
