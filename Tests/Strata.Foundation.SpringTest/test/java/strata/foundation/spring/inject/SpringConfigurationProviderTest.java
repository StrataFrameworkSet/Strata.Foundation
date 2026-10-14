//////////////////////////////////////////////////////////////////////////////
// SpringConfigurationProviderTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.StandardEnvironment;
import strata.foundation.core.configuration.IConfiguration;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

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

    @Test
    public void
    testResolverResolvesPlaceholder()
    {
        assumeNoDeployEnv();

        IConfiguration config =
            new SpringConfigurationProvider(
                "placeholder",
                true,
                createEnvironment(placeholderValues()))
                .get();

        assertEquals("resolved",config.getProperty("placeholder.property"));
    }

    @Test
    public void
    testResolverAppliesPlaceholderDefault()
    {
        assumeNoDeployEnv();

        IConfiguration config =
            new SpringConfigurationProvider(
                "placeholder",
                true,
                createEnvironment(placeholderValues()))
                .get();

        assertEquals("fallback",config.getProperty("placeholder.defaulted"));
    }

    @Test
    public void
    testResolverThrowsOnUnresolvedPlaceholder()
    {
        assumeNoDeployEnv();

        SpringConfigurationProvider provider =
            new SpringConfigurationProvider(
                "unresolved",
                true,
                createEnvironment(Map.of()));

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class,provider::get);

        StringBuilder chain = new StringBuilder();

        for (Throwable t = exception; t != null; t = t.getCause())
            chain.append(t).append('\n');

        assertTrue(chain.toString().contains("missing.key"));
        assertFalse(chain.toString().contains("sibling-secret-value"));
    }

    @Test
    public void
    testResolverReadsDeployEnvFromPropertyResolver()
    {
        assumeNoDeployEnv();

        IConfiguration config =
            new SpringConfigurationProvider(
                "development",
                true,
                createEnvironment(
                    Map.of(
                        "DEPLOY_ENV","placeholder",
                        "test.placeholder","x",
                        "test.special","y")))
                .get();

        assertEquals("x",config.getProperty("placeholder.property"));
        assertFalse(config.hasProperty("env.property"));
    }

    @Test
    public void
    testResolverResolvesBaseProperties()
    {
        assumeNoDeployEnv();

        IConfiguration config =
            new SpringConfigurationProvider(
                "placeholder",
                true,
                createEnvironment(placeholderValues()))
                .get();

        assertEquals("base-value",config.getProperty("base.property"));
        assertEquals(42L,config.getLongProperty("base.long"));
    }

    @Test
    public void
    testLegacyModeLeavesPlaceholdersLiteral()
    {
        assumeNoDeployEnv();

        IConfiguration config =
            new SpringConfigurationProvider("placeholder",true).get();

        assertEquals(
            "${test.placeholder}",
            config.getProperty("placeholder.property"));
    }

    @Test
    public void
    testResolverMissingProfileFileSucceedsWhenNotRequired()
    {
        assumeNoDeployEnv();

        IConfiguration config =
            new SpringConfigurationProvider(
                "nonexistent",
                false,
                createEnvironment(Map.of()))
                .get();

        assertNotNull(config);
        assertEquals("base-value",config.getProperty("base.property"));
        assertEquals("base-shared-value",config.getProperty("shared.property"));
        assertFalse(config.hasProperty("env.property"));
    }

    @Test
    public void
    testResolverPreservesSpecialCharactersWithoutInjection()
    {
        assumeNoDeployEnv();

        String special = "pa\\ss\ninjected.key=evil#x";

        IConfiguration config =
            new SpringConfigurationProvider(
                "placeholder",
                true,
                createEnvironment(
                    Map.of(
                        "test.placeholder","resolved",
                        "test.special",special)))
                .get();

        assertEquals(special,config.getProperty("placeholder.special"));
        assertFalse(config.hasProperty("injected.key"));
    }

    @Test
    public void
    testInvalidProfileIsRejected()
    {
        assumeNoDeployEnv();

        SpringConfigurationProvider provider =
            new SpringConfigurationProvider("../development",true);

        assertThrows(IllegalArgumentException.class,provider::get);
    }

    @Test
    public void
    testResolverConstructorRejectsNull()
    {
        assertThrows(
            NullPointerException.class,
            () -> new SpringConfigurationProvider((PropertyResolver)null));
    }

    private void
    assumeNoDeployEnv()
    {
        assumeTrue(System.getenv("DEPLOY_ENV") == null);
    }

    private Map<String,Object>
    placeholderValues()
    {
        return
            Map.of(
                "test.placeholder","resolved",
                "test.special","special");
    }

    private StandardEnvironment
    createEnvironment(Map<String,Object> values)
    {
        StandardEnvironment environment = new StandardEnvironment();

        environment
            .getPropertySources()
            .addFirst(new MapPropertySource("test",values));

        return environment;
    }
}

//////////////////////////////////////////////////////////////////////////////
