/// ///////////////////////////////////////////////////////////////////////////
// SimpleFactoryTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.reflect;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import strata.foundation.core.value.EmailAddress;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("CommitStage")
public
class SimpleFactoryTest
{
    private IFactory factory;

    @BeforeEach
    public void
    setUp() throws NoSuchMethodException
    {
        factory =
            new SimpleFactory()
                .insertDefaultConstructor(HashMap.class)
                .insertParameterizedConstructor(EmailAddress.class,String.class);
    }

    @Test
    public void
    testCreateWithParameterizedConstructor()
        throws InvocationTargetException, InstantiationException, IllegalAccessException
    {
        EmailAddress expected = new EmailAddress("test@test.com");
        EmailAddress actual = factory.create(EmailAddress.class,"test@test.com");

        assertEquals(expected, actual);
    }

    @Test
    public void
    testCreateWithDefaultConstructor()
        throws InvocationTargetException, InstantiationException, IllegalAccessException
    {
        HashMap<String,String> expected = new HashMap<>();
        HashMap<String,String> actual = factory.create(HashMap.class);

        assertEquals(expected.size(), actual.size());
    }
}

//////////////////////////////////////////////////////////////////////////////
