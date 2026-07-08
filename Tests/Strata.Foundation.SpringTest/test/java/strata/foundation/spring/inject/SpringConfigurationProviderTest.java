//////////////////////////////////////////////////////////////////////////////
// SpringConfigurationProviderTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import strata.foundation.core.configuration.IConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class SpringConfigurationProviderTest
{
    private SpringConfigurationProvider itsTarget;

    @BeforeEach
    public void
    setUp()
    {
        itsTarget = new SpringConfigurationProvider();
    }

    @Test
    public void
    testGetReturnsConfiguration()
    {
        IConfiguration config = itsTarget.get();

        assertNotNull(config);
    }

    @Test
    public void
    testGetBaseProperty()
    {
        IConfiguration config = itsTarget.get();

        assertEquals("base-value",config.getProperty("base.property"));
    }

    @Test
    public void
    testGetEnvironmentProperty()
    {
        IConfiguration config = itsTarget.get();

        assertEquals("development-value",config.getProperty("env.property"));
    }

    @Test
    public void
    testOverlayOverridesBaseProperty()
    {
        IConfiguration config = itsTarget.get();

        assertEquals(
            "development-shared-value",
            config.getProperty("shared.property"));
    }

    @Test
    public void
    testHasPropertyReturnsFalseForMissing()
    {
        IConfiguration config = itsTarget.get();

        assertFalse(config.hasProperty("nonexistent.property"));
    }

    @Test
    public void
    testGetBooleanProperty()
    {
        IConfiguration config = itsTarget.get();

        assertTrue(config.hasBooleanProperty("base.boolean"));
        assertEquals(true,config.getBooleanProperty("base.boolean"));
    }

    @Test
    public void
    testGetLongProperty()
    {
        IConfiguration config = itsTarget.get();

        assertTrue(config.hasLongProperty("base.long"));
        assertEquals(42L,config.getLongProperty("base.long"));
    }

    @Test
    public void
    testStreamContainsAllProperties()
    {
        IConfiguration config = itsTarget.get();

        long count =
            config
                .stream()
                .count();

        assertTrue(count >= 5);
        assertTrue(config.hasProperty("base.property"));
        assertTrue(config.hasProperty("env.property"));
        assertTrue(config.hasProperty("shared.property"));
        assertTrue(config.hasProperty("base.boolean"));
        assertTrue(config.hasProperty("base.long"));
    }

    @Test
    public void
    testMissingEnvironmentFileSucceedsWhenNotRequired()
    {
        SpringConfigurationProvider provider =
            new SpringConfigurationProvider("nonexistent",false);

        IConfiguration config = provider.get();

        assertNotNull(config);
        assertEquals("base-value",config.getProperty("base.property"));
        assertEquals("base-shared-value",config.getProperty("shared.property"));
        assertFalse(config.hasProperty("env.property"));
    }

    @Test
    public void
    testMissingEnvironmentFileThrowsWhenRequired()
    {
        SpringConfigurationProvider provider =
            new SpringConfigurationProvider("nonexistent",true);

        RuntimeException exception =
            assertThrows(RuntimeException.class,provider::get);

        assertTrue(
            exception
                .getMessage()
                .contains("application-nonexistent.properties not found on classpath"));
    }
}

//////////////////////////////////////////////////////////////////////////////
