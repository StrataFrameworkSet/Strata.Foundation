//////////////////////////////////////////////////////////////////////////////
// PhoneNumber.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import strata.foundation.core.utility.ICopyable;

import java.io.Serializable;

public
class PhoneNumber
    implements ICopyable, Serializable, Comparable<PhoneNumber>
{
    private String itsPhone;

    private static final String NANP_PATTERN =
        "^([1][-.\\s]?)?\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$";

    private static final String ITU_T_PATTERN =
        "^\\+(?:[0-9] ?){6,14}[0-9]$";

    private static final String EPP_PATTERN =
        "^\\+[0-9]{1,3}\\.[0-9]{4,14}(?:x.+)?$";

    @JsonCreator
    public
    PhoneNumber(@JsonProperty("phoneNumber") String phoneNumber)
    {
        validatePhoneNumber(phoneNumber);
        itsPhone = phoneNumber;
    }

    public
    PhoneNumber(PhoneNumber other)
    {
        itsPhone = other.itsPhone;
    }

    @Override
    public PhoneNumber
    copy()
    {
        return new PhoneNumber(this);
    }

    @Override
    public int
    compareTo(PhoneNumber other)
    {
        return itsPhone.compareToIgnoreCase(other.itsPhone);
    }

    public boolean
    equals(PhoneNumber other)
    {
        return itsPhone.equals(other.itsPhone);
    }

    @Override
    public boolean
    equals(Object other)
    {
        return other instanceof PhoneNumber p ? equals(p) : false;
    }

    @Override
    public int
    hashCode()
    {
        return 51 * itsPhone.hashCode();
    }

    @Override
    @JsonProperty("phoneNumber")
    public String
    toString()
    {
        return itsPhone;
    }

    public String
    getDigitsOnly()
    {
        return
            itsPhone
                .chars()
                .mapToObj(c -> (char)c)
                .filter(Character::isDigit)
                .collect(
                    StringBuilder::new,
                    StringBuilder::append,
                    StringBuilder::append)
                .toString();
    }

    private static void
    validatePhoneNumber(String input)
    {
        if (!checkPhoneNumber(input))
            throw
                new IllegalArgumentException(
                    "Unrecognized phone format: " + input);
    }

    private static boolean
    checkPhoneNumber(String input)
    {
        if (input.matches(NANP_PATTERN))
            return true;

        if (input.matches(ITU_T_PATTERN))
            return true;

        if (input.matches(EPP_PATTERN))
            return true;

        return false;
    }
    
}

//////////////////////////////////////////////////////////////////////////////