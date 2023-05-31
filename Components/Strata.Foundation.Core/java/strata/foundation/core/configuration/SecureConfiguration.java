//////////////////////////////////////////////////////////////////////////////
// SecureConfiguration.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.configuration;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.properties.EncryptableProperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public
class SecureConfiguration
    implements IConfiguration
{
    private final Properties itsProperties;

    public
    SecureConfiguration(InputStream input)
        throws IOException
    {
        itsProperties = new EncryptableProperties(createEncrypter());
        itsProperties.load(input);
    }

    @Override
    public String
    getProperty(String key)
    {
        return itsProperties.getProperty(key);
    }

    @Override
    public boolean
    hasProperty(String key)
    {
        return itsProperties.containsKey(key);
    }

    protected StandardPBEStringEncryptor
    createEncrypter()
    {
        StandardPBEStringEncryptor encrypter = new StandardPBEStringEncryptor();

        encrypter.setPassword(getPropertiesEncryptionKey());
        encrypter.setAlgorithm("PBEWithHMACSHA512AndAES_256");
        encrypter.setIvGenerator(new RandomIvGenerator());

        return encrypter;
    }

    protected String
    getPropertiesEncryptionKey()
    {
        return System.getenv("PROPERTIES_ENCRYPTION_KEY");
    }
}

//////////////////////////////////////////////////////////////////////////////
