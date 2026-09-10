//////////////////////////////////////////////////////////////////////////////
// IFactorySupplier.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.reflect;

import java.util.function.Supplier;

/**
 * <p>
 * A {@link java.util.function.Supplier} specialization that supplies an
 * {@link IFactory} instance.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * IFactorySupplier supplier = new SingletonFactorySupplier();
 * IFactory factory = supplier.get();
 * </pre>
 */
public
interface IFactorySupplier
    extends Supplier<IFactory> {}

//////////////////////////////////////////////////////////////////////////////
