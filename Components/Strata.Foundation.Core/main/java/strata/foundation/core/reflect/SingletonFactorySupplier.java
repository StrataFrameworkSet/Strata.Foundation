//////////////////////////////////////////////////////////////////////////////
// SingletonFactorySupplier.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.reflect;

/**
 * <p>
 * {@link IFactorySupplier} that always supplies the same, process-wide
 * {@link SimpleFactory} instance.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * IFactory factory = new SingletonFactorySupplier().get();
 * </pre>
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
