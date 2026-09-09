//////////////////////////////////////////////////////////////////////////////
// SecureConfiguration.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.configuration;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.properties.EncryptableProperties;
import strata.foundation.core.collection.Pair;
import strata.foundation.core.inject.EnvironmentValueProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * Implementation of {@link IConfiguration} that loads properties from
 * {@link java.io.InputStream} sources using Jasypt
 * {@link java.util.Properties} encryption for transparent decryption
 * of {@code ENC(...)} values. Provides typed property access,
 * prefix-based filtering, existence checks, and streaming inherited
 * from {@link IConfiguration}. Encryption uses
 * PBEWithHMACSHA512AndAES_256 with a key sourced from the
 * {@code PROPERTIES_ENCRYPTION_KEY} environment variable.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Loading from classpath resource
 * InputStream input = getClass().getResourceAsStream("/application.properties");
 * IConfiguration config = new SecureConfiguration(input);
 *
 * // Transparent decryption of ENC(...) values
 * String password = config.getProperty("db.password");
 *
 * // Multiple input sources (merged)
 * IConfiguration config = new SecureConfiguration(baseInput,overrideInput);
 * </pre>
 * </p>
 */
public
class SecureConfiguration
    implements IConfiguration
{
    private final Properties itsProperties;

    public
    SecureConfiguration(InputStream... inputs)
        throws IOException
    {
        itsProperties = new EncryptableProperties(createEncrypter());

        for (InputStream input : inputs)
            if (input != null)
                itsProperties.load(input);
    }

    @Override
    public String
    getProperty(String key)
    {
        return itsProperties.getProperty(key);
    }

    @Override
    public String
    getProperty(String key,String defaultValue)
    {
        return itsProperties.getProperty(key,defaultValue);
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
                        Pair::getFirst,
                        Pair::getSecond));
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
                .keySet()
                .stream()
                .map(key -> Pair.of(key.toString(),itsProperties.get(key)));
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
        return
            EnvironmentValueProvider
                .ofVariable("PROPERTIES_ENCRYPTION_KEY")
                .get()
                .orElseThrow(() -> new RuntimeException("missing environment variable 'PROPERTIES_ENCRYPTION_KEY'"));
    }

    protected boolean
    matchPrefixes(String key,String... prefixes)
    {
        return
            Arrays
                .stream(prefixes)
                .anyMatch(key::startsWith);

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
