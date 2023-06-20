//////////////////////////////////////////////////////////////////////////////
// SecureConfigurationTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("CommitStage")
public
class SecureConfigurationTest
{
    private IConfiguration itsTarget;

    @BeforeEach
    public void
    setUp()
        throws Exception
    {
        itsTarget = new SecureConfiguration(getPropertiesStream());
    }

    @Test
    public void
    testGetProperty()
    {
        String unencryptedProperty1 =
            itsTarget.getProperty("unencrypted.property1");
        String encryptedProperty1 =
            itsTarget.getProperty("encrypted.property1");
        String unencryptedProperty2 =
            itsTarget.getProperty("unencrypted.property2");
        String encryptedProperty2 =
            itsTarget.getProperty("encrypted.property2");

        Assertions.assertNotNull(unencryptedProperty1);
        Assertions.assertNotNull(encryptedProperty1);
        Assertions.assertNotNull(unencryptedProperty2);
        Assertions.assertNotNull(encryptedProperty2);
    }

    protected InputStream
    getPropertiesStream()
    {
        return
            ClassLoader
                .getSystemResourceAsStream(
                    "configurationtest.properties");
    }
}

//////////////////////////////////////////////////////////////////////////////
