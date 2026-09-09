//////////////////////////////////////////////////////////////////////////////
// DefaultSecureStringMapper.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import strata.foundation.core.inject.EnvironmentValueProvider;
import strata.foundation.core.inject.IEnvironmentValueProvider;

import java.util.Objects;

/**
 * <p>
 * Default implementation of {@link ISecureStringMapper} that encrypts and
 * decrypts {@link String} values directly, without an intermediate
 * serialization step, using a Jasypt
 * {@link StandardPBEStringEncryptor} configured with the
 * {@code PBEWithHMACSHA512AndAES_256} algorithm. The encryption key is
 * either supplied directly or resolved from an
 * {@link IEnvironmentValueProvider}, defaulting to the
 * {@code PROPERTIES_ENCRYPTION_KEY} environment variable.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * DefaultSecureStringMapper mapper =
 *     new DefaultSecureStringMapper("my-secret-key");
 *
 * String encrypted = mapper.mapEncrypted("sensitive-value");
 * String decrypted = mapper.mapDecrypted(encrypted);
 * </pre>
 * </p>
 */
public
class DefaultSecureStringMapper
    implements ISecureStringMapper
{
    private final StringEncryptor encryptor;

    public
    DefaultSecureStringMapper()
    {
        this(
            EnvironmentValueProvider
                .ofVariable("PROPERTIES_ENCRYPTION_KEY"));
    }

    public
    DefaultSecureStringMapper(IEnvironmentValueProvider provider)
    {
        this(getEncryptionKey(provider));
    }

    public
    DefaultSecureStringMapper(String key)
    {
        encryptor = createEncrypter(key);
    }

    @Override
    public String
    mapEncrypted(String value)
    {
        return
            Objects.nonNull(value)
                ? encryptor.encrypt(value)
                : null;
    }

    @Override
    public String
    mapDecrypted(String value)
    {
        return
            Objects.nonNull(value)
                ? encryptor.decrypt(value)
                : null;
    }

    private static StandardPBEStringEncryptor
    createEncrypter(String key)
    {
        StandardPBEStringEncryptor encrypter = new StandardPBEStringEncryptor();

        encrypter.setPassword(key);
        encrypter.setAlgorithm("PBEWithHMACSHA512AndAES_256");
        encrypter.setIvGenerator(new RandomIvGenerator());

        return encrypter;
    }


    private static String
    getEncryptionKey(IEnvironmentValueProvider provider)
    {
        return
            provider
                .get()
                .orElseThrow(provider.getException());
    }

}

//////////////////////////////////////////////////////////////////////////////
