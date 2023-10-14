//////////////////////////////////////////////////////////////////////////////
// HashedString.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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
}

//////////////////////////////////////////////////////////////////////////////