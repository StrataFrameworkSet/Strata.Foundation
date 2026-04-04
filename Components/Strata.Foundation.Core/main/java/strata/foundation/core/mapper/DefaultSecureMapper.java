/// ///////////////////////////////////////////////////////////////////////////
// DefaultSecureMapper.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import strata.foundation.core.inject.EnvironmentValueProvider;
import strata.foundation.core.inject.IEnvironmentValueProvider;

import java.io.Serializable;
import java.util.Objects;

public
class DefaultSecureMapper<T extends Serializable>
    implements ISecureMapper<T>
{
    private final ObjectMapper    mapper;
    private final StringEncryptor encryptor;
    private final Class<T>        type;

    public
    DefaultSecureMapper(Class<T> type)
    {
        this(
            type,
            EnvironmentValueProvider.ofVariable("PROPERTIES_ENCRYPTION_KEY"));
    }

    public
    DefaultSecureMapper(Class<T> type,IEnvironmentValueProvider provider)
    {
        this(type,getEncryptionKey(provider));
    }

    public
    DefaultSecureMapper(Class<T> type,String key)
    {
        this.mapper = new ObjectMapperSupplier().get();
        this.encryptor = createEncrypter(key);
        this.type = type;
    }

    @Override
    public String
    mapEncrypted(T value)
    {
        try
        {
            return
                Objects.nonNull(value)
                    ? encryptor.encrypt(mapper.writeValueAsString(value))
                    : null;
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T
    mapDecrypted(String value)
    {
        try
        {
            return
                Objects.nonNull(value)
                    ? mapper.readValue(encryptor.decrypt(value),type)
                    : null;
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
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
