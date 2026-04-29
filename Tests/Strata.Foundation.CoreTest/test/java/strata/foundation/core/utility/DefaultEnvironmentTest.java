//////////////////////////////////////////////////////////////////////////////
// DefaultEnvironmentTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class DefaultEnvironmentTest
{
    private DefaultEnvironment target;

    @BeforeEach
    public void
    setUp()
    {
        target = new DefaultEnvironment();
    }

    @Test
    public void
    testGetReturnsSystemEnvVariable()
    {
        String result = target.get("PATH");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void
    testGetReturnsDefaultWhenNotInEnv()
    {
        target.setDefault("MY_CUSTOM_VAR_NOT_IN_ENV","defaultValue");

        String result = target.get("MY_CUSTOM_VAR_NOT_IN_ENV");

        assertEquals("defaultValue",result);
    }

    @Test
    public void
    testGetThrowsWhenVariableNotFound()
    {
        assertThrows(
            NoSuchElementException.class,
            () -> target.get("VARIABLE_THAT_DOES_NOT_EXIST_ANYWHERE"));
    }

    @Test
    public void
    testSetDefault()
    {
        target.setDefault("FOO","bar");

        assertEquals("bar",target.get("FOO"));
    }

    @Test
    public void
    testSetDefaults()
    {
        target.setDefault("OLD_KEY","oldValue");
        target.setDefaults(Map.of("NEW_KEY","newValue"));

        assertEquals("newValue",target.get("NEW_KEY"));
        assertThrows(
            NoSuchElementException.class,
            () -> target.get("OLD_KEY"));
    }

    @Test
    public void
    testClearDefault()
    {
        target.setDefault("KEY_TO_CLEAR","value");
        target.setDefault("KEY_TO_KEEP","keepValue");

        target.clearDefault("KEY_TO_CLEAR");

        assertThrows(
            NoSuchElementException.class,
            () -> target.get("KEY_TO_CLEAR"));
        assertEquals("keepValue",target.get("KEY_TO_KEEP"));
    }

    @Test
    public void
    testClearDefaults()
    {
        target.setDefault("A","1");
        target.setDefault("B","2");

        target.clearDefaults();

        assertThrows(
            NoSuchElementException.class,
            () -> target.get("A"));
        assertThrows(
            NoSuchElementException.class,
            () -> target.get("B"));
    }

    @Test
    public void
    testConstructorWithDefaults()
    {
        DefaultEnvironment env =
            new DefaultEnvironment(Map.of("X","xValue","Y","yValue"));

        assertEquals("xValue",env.get("X"));
        assertEquals("yValue",env.get("Y"));
    }

    @Test
    public void
    testFluentApi()
    {
        DefaultEnvironment result =
            target
                .setDefault("A","1")
                .setDefault("B","2");

        assertSame(target,result);
        assertEquals("1",target.get("A"));
        assertEquals("2",target.get("B"));
    }

    @Test
    public void
    testOfFactory()
    {
        DefaultEnvironment env =
            DefaultEnvironment.of(Map.of("KEY","value"));

        assertEquals("value",env.get("KEY"));
    }
}

//////////////////////////////////////////////////////////////////////////////
