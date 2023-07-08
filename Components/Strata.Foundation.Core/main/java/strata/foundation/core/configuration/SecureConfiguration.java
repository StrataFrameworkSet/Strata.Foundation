//////////////////////////////////////////////////////////////////////////////
// SecureConfiguration.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.configuration;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.properties.EncryptableProperties;
import strata.foundation.core.container.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Boolean
    getBooleanProperty(String key)
    {
        if (hasProperty(key))
        {
            if (hasBooleanProperty(key))
                return Boolean.valueOf(getProperty(key));

            throw new ClassCastException("property at '" + key + "' not boolean");
        }

        return null;
    }

    @Override
    public Long
    getLongProperty(String key)
    {
        if (hasProperty(key))
        {
            if (hasLongProperty(key))
                return Long.valueOf(getProperty(key));

            throw new ClassCastException("property at '" + key + "' not long");
        }

        return null;
    }

    @Override
    public Map<String,Object>
    getProperties(String... prefixes)
    {
        return
            stream()
                .filter(pair -> matchPrefixes(pair.getFirst(),prefixes))
                .collect(
                    Collectors.toMap(
                        pair -> pair.getFirst(),
                        pair -> pair.getSecond()));
    }

    @Override
    public boolean
    hasProperty(String key)
    {
        return itsProperties.containsKey(key);
    }

    @Override
    public boolean
    hasBooleanProperty(String key)
    {
        return hasProperty(key) && isBoolean(getProperty(key));
    }

    @Override
    public boolean
    hasLongProperty(String key)
    {
        return hasProperty(key) && isLong(getProperty(key));
    }

    @Override
    public Stream<Pair<String,Object>>
    stream()
    {
        return
            itsProperties
                .entrySet()
                .stream()
                .map(
                    entry ->
                        Pair.create(
                            entry
                                .getKey()
                                .toString(),
                            entry.getValue()));
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

    protected boolean
    matchPrefixes(String key,String... prefixes)
    {
        return
            Arrays
                .stream(prefixes)
                .anyMatch(prefix -> key.startsWith(prefix));

    }

    private boolean
    isBoolean(String value)
    {
        return
            value.equalsIgnoreCase("true") ||
            value.equalsIgnoreCase("false");
    }

    private boolean
    isLong(String value)
    {
        try
        {
            Long.parseLong(value);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

}

//////////////////////////////////////////////////////////////////////////////
