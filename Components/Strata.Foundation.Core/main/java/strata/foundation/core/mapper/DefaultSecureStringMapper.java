/// ///////////////////////////////////////////////////////////////////////////
// DefaultSecureStringMapper.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;

import java.util.Objects;

public
class DefaultSecureStringMapper
    implements ISecureStringMapper
{
    private final StringEncryptor encryptor;

    public
    DefaultSecureStringMapper()
    {
        this(getPropertiesEncryptionKey());
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
    getPropertiesEncryptionKey()
    {
        return System.getenv("PROPERTIES_ENCRYPTION_KEY");
    }

}

//////////////////////////////////////////////////////////////////////////////
