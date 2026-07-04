//////////////////////////////////////////////////////////////////////////////
// HashedString.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.Objects;
import java.util.UUID;

public
class HashedString
    implements Serializable
{
    private byte[] itsValue;
    private byte[] itsSalt;

    public
    HashedString(String unhashedValue)
    {
        this(
            unhashedValue,
            UUID
                .randomUUID()
                .toString()
                .getBytes(StandardCharsets.UTF_8));
    }

    public
    HashedString(String unhashedValue,byte[] salt)
    {
        this(
            unhashedValue.getBytes(StandardCharsets.UTF_8),
            salt,
            false);
    }

    public
    HashedString(byte[] value,byte[] salt,boolean hashed)
    {
        if (value == null)
            throw new NullPointerException("Cannot hash null value");

        itsSalt  = salt;
        itsValue = hashed ? value : hash(value,itsSalt);
    }

    /*
    @Override
    public int
    compareTo(HashedString other)
    {
        int comparison = Arrays.compare(itsValue,other.itsValue);

        return
            comparison != 0
                ? comparison
                : Arrays.compare(itsSalt,other.itsSalt);
    }
    */

    public boolean
    equals(HashedString other)
    {
        if (other == null)
            return false;

        if (this == other)
            return true;

        return
            Arrays.equals(itsValue,other.itsValue) &&
                Arrays.equals(itsSalt,other.itsSalt);
    }

    @Override
    public boolean
    equals(Object other)
    {
        return other instanceof HashedString h ? equals(h) : false;
    }

    @Override
    public int
    hashCode()
    {
        int hash = 7;

        hash = 31 * hash + itsValue.hashCode();
        hash = 31 * hash + itsSalt.hashCode();
        return hash;
    }

    @Override
    public String
    toString()
    {
        return
            new StringBuilder()
                .append("Hash=")
                .append(new String(itsValue,StandardCharsets.UTF_8))
                .append(",Salt=")
                .append(new String(itsSalt,StandardCharsets.UTF_8))
                .toString();
    }

    public byte[]
    getValue() { return itsValue; }

    public byte[]
    getSalt() { return itsSalt; }

    public String
    getHexValue()
    {
        return
            HexFormat
                .of()
                .formatHex(itsValue);
    }

    public boolean
    matches(String unhashedValue)
    {
        return matches(unhashedValue.getBytes(StandardCharsets.UTF_8));
    }

    public boolean
    matches(byte[] unhashedValue)
    {
        return Arrays.equals(itsValue,hash(unhashedValue,itsSalt));
    }

    public static HashedString
    of(String unhashedValue)
    {
        return new HashedString(unhashedValue);
    }

    public static HashedString
    of(String unhashedValue,byte[] salt)
    {
        return new HashedString(unhashedValue,salt);
    }

    public static HashedString
    ofFixed(String unhashedValue)
    {
        return
            new HashedString(
                unhashedValue,
                getHashKey().getBytes(StandardCharsets.UTF_8));
    }

    public static HashedString
    of(byte[] value,byte[] salt,boolean hashed)
    {
        return new HashedString(value,salt,hashed);
    }

    public static HashedString
    ofFixed(byte[] value,byte[] salt)
    {
        return new HashedString(value,salt,true);
    }

    protected byte[]
    hash(byte[] unhashedValue,byte[] salt)
    {
        byte[] both =
            Arrays.copyOf(
                unhashedValue,
                unhashedValue.length + salt.length);

        System.arraycopy(
            salt,
            0,
            both,
            unhashedValue.length,
            salt.length);

        try
        {
            return
                MessageDigest
                    .getInstance("SHA-256")
                    .digest(both);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new UnsupportedOperationException(e);
        }
    }

    private static String
    getHashKey()
    {
        String key = System.getenv("HASHED_STRING_KEY");

        return Objects.nonNull(key) ? key : "S@U6&pV<!uKU(9Pw";
    }
}

//////////////////////////////////////////////////////////////////////////////