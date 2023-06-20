//////////////////////////////////////////////////////////////////////////////
// PhoneNumber.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import java.io.Serializable;

public
class PhoneNumber
    implements Serializable
{
    private String itsPhone;

    private static final String NANP_PATTERN =
        "^([1][-.\\s]?)?\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$";

    private static final String ITU_T_PATTERN =
        "^\\+(?:[0-9] ?){6,14}[0-9]$";

    private static final String EPP_PATTERN =
        "^\\+[0-9]{1,3}\\.[0-9]{4,14}(?:x.+)?$";

    public
    PhoneNumber(String phoneNumber)
    {
        validatePhoneNumber(phoneNumber);
        itsPhone = phoneNumber;
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
        return
            other instanceof PhoneNumber && equals((PhoneNumber)other);
    }

    @Override
    public int
    hashCode()
    {
        return 51 * itsPhone.hashCode();
    }

    @Override
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