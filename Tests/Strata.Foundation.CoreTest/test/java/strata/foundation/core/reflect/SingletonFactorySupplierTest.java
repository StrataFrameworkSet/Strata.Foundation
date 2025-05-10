/// ///////////////////////////////////////////////////////////////////////////
// SingletonFactorySupplierTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.reflect;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("CommitStage")
public
class SingletonFactorySupplierTest
{
    private IFactory factory;

    @BeforeEach
    public void
    setUp() throws ClassNotFoundException
    {
        factory = new SingletonFactorySupplier().get();
        Class.forName(FooSupplier.class.getName());
    }

    @Test
    public void
    testInitialization()
        throws InvocationTargetException, InstantiationException, IllegalAccessException
    {
        IStringSupplier foo = factory.create(IStringSupplier.class);
        //IStringSupplier bar = factory.create(BarSupplier.class);

        assertEquals("FOO", foo.get());
        //assertEquals("BAR", bar.get());
    }
}

//////////////////////////////////////////////////////////////////////////////
