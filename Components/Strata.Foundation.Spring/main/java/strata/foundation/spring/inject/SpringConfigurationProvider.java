//////////////////////////////////////////////////////////////////////////////
// SpringConfigurationProvider.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import jakarta.inject.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.PropertyResolver;
import strata.foundation.core.configuration.IConfiguration;
import strata.foundation.core.configuration.SecureConfiguration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Pattern;


public
class SpringConfigurationProvider
    implements Provider<IConfiguration>
{
    private static final Pattern PROFILE_PATTERN =
        Pattern.compile("[A-Za-z0-9_-]+");

    private final String                     defaultProfile;
    private final boolean                    requireProfileProperties;
    private final Optional<PropertyResolver> resolver;
    private final Logger                     logger;

    public
    SpringConfigurationProvider()
    {
        this("development",true);
    }

    public
    SpringConfigurationProvider(String defaultProfile,boolean requireProfileProperties)
    {
        this(defaultProfile,requireProfileProperties,Optional.empty());
    }

    public
    SpringConfigurationProvider(PropertyResolver resolver)
    {
        this("development",true,resolver);
    }

    public
    SpringConfigurationProvider(
        String           defaultProfile,
        boolean          requireProfileProperties,
        PropertyResolver resolver)
    {
        this(
            defaultProfile,
            requireProfileProperties,
            Optional.of(Objects.requireNonNull(resolver,"resolver")));
    }

    private
    SpringConfigurationProvider(
        String                     defaultProfile,
        boolean                    requireProfileProperties,
        Optional<PropertyResolver> resolver)
    {
        this.defaultProfile = defaultProfile;
        this.requireProfileProperties = requireProfileProperties;
        this.resolver = resolver;
        logger = LogManager.getLogger(SpringConfigurationProvider.class);
    }

    @Override
    public IConfiguration
    get()
    {
        String           profile = getProfile();
        String           profileFileName = "application-" + profile + ".properties";
        Optional<byte[]> base = readResource("application.properties");
        Optional<byte[]> profileSpecific = readResource(profileFileName);

        if (base.isEmpty())
            throw new RuntimeException("application.properties not found on classpath");

        logger.debug("DEPLOY_ENV = {}",profile);
        logger.debug("Loading application.properties");

        if (profileSpecific.isPresent())
            logger.debug("Loading {}",profileFileName);
        else if (requireProfileProperties)
            throw new RuntimeException(profileFileName + " not found on classpath");
        else
            logger.debug("{} not found, skipping",profileFileName);

        byte[] baseContent = prepare(base.get(),"application.properties");
        byte[] profileContent =
            profileSpecific
                .map(content -> prepare(content,profileFileName))
                .orElse(null);

        try
        {
            return
                new SecureConfiguration(
                    toStream(baseContent),
                    profileContent != null ? toStream(profileContent) : null);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private String
    getProfile()
    {
        String profile =
            Optional
                .ofNullable(System.getenv("DEPLOY_ENV"))
                .or(() -> resolver.map(r -> r.getProperty("DEPLOY_ENV")))
                .orElse(defaultProfile);

        if (profile == null || !PROFILE_PATTERN.matcher(profile).matches())
            throw new IllegalArgumentException(
                "profile name must match " + PROFILE_PATTERN.pattern());

        return profile;
    }

    private byte[]
    prepare(byte[] content,String fileName)
    {
        return
            resolver
                .map(r -> resolvePlaceholders(content,fileName,r))
                .orElse(content);
    }

    private byte[]
    resolvePlaceholders(byte[] content,String fileName,PropertyResolver r)
    {
        Properties            raw = new Properties();
        Properties            resolved = new Properties();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try
        {
            raw.load(toStream(content));

            for (String key : raw.stringPropertyNames())
                resolved.setProperty(
                    key,
                    resolveValue(key,raw.getProperty(key),fileName,r));

            resolved.store(output,null);
            return output.toByteArray();
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    private String
    resolveValue(String key,String value,String fileName,PropertyResolver r)
    {
        try
        {
            return r.resolveRequiredPlaceholders(value);
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException(
                String.format(
                    "unresolvable placeholder in property '%s' of %s",
                    key,
                    fileName),
                e);
        }
    }

    private Optional<byte[]>
    readResource(String name)
    {
        try (InputStream input = getClassLoader().getResourceAsStream(name))
        {
            return
                input != null
                    ? Optional.of(input.readAllBytes())
                    : Optional.empty();
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    private ClassLoader
    getClassLoader()
    {
        ClassLoader context = Thread.currentThread().getContextClassLoader();

        return
            context != null
                ? context
                : SpringConfigurationProvider.class.getClassLoader();
    }

    private InputStream
    toStream(byte[] content)
    {
        return new ByteArrayInputStream(content);
    }
}

//////////////////////////////////////////////////////////////////////////////
