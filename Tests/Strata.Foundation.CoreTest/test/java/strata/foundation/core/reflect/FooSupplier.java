/// ///////////////////////////////////////////////////////////////////////////
// FooSupplier.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.reflect;

public
class FooSupplier
    implements IStringSupplier
{
    static
    {
        try
        {
            IFactory factory = new SingletonFactorySupplier().get();

            factory.insertConstructor(
                IStringSupplier.class,
                FooSupplier.class.getConstructor());
        }
        catch (NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String
    get()
    {
        return "FOO";
    }
}

//////////////////////////////////////////////////////////////////////////////
