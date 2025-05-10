/// ///////////////////////////////////////////////////////////////////////////
// BarSupplier.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.reflect;

public
class BarSupplier
    implements IStringSupplier
{
    static
    {
        try
        {
            new FactoryInitializer(
                IStringSupplier.class,
                BarSupplier.class.getConstructor());
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
        return "BAR";
    }
}

//////////////////////////////////////////////////////////////////////////////
