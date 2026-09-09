//////////////////////////////////////////////////////////////////////////////
// SingletonFactorySupplier.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.reflect;

/**
 * <p>
 * {@link IFactorySupplier} that always supplies the same, process-wide
 * {@link SimpleFactory} instance.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * IFactory factory = new SingletonFactorySupplier().get();
 * </pre>
 * </p>
 */
public
class SingletonFactorySupplier
    implements IFactorySupplier
{
    private static final IFactory instance = new SimpleFactory();

    public
    SingletonFactorySupplier() {}

    @Override
    public IFactory
    get()
    {
        return instance;
    }
}

//////////////////////////////////////////////////////////////////////////////
