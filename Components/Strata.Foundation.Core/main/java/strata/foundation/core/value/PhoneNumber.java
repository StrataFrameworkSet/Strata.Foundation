//////////////////////////////////////////////////////////////////////////////
// PhoneNumber.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import strata.foundation.core.utility.ICopyable;

import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * An immutable value type representing a validated phone number. The
 * supplied string is checked on construction against a set of common
 * formats: North American Numbering Plan (NANP), ITU-T
 * <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>, EPP, and a
 * plain run of 7 to 17 digits, rejecting values that match none of them.
 * </p>
 * <p>
 * Equality is based on the digits-only representation of the number (see
 * {@link #getDigitsOnly()}), so formatting differences such as
 * punctuation or whitespace do not affect equality, while ordering via
 * {@link java.lang.Comparable} compares the original, formatted string
 * case-insensitively. Copies are produced via {@link ICopyable#copy()},
 * and instances implement {@link java.io.Serializable}.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * PhoneNumber phoneNumber = PhoneNumber.of("+1 (555) 123-4567");
 *
 * PhoneNumber copy = phoneNumber.copy();
 *
 * boolean same = phoneNumber.equals(PhoneNumber.of("555-123-4567"));
 * String digits = phoneNumber.getDigitsOnly();
 * </pre>
 */
public
class PhoneNumber
    implements ICopyable, Serializable, Comparable<PhoneNumber>
{
    private String phone;

    private static final String NANP_PATTERN =
        "^(\\+?[1][-.\\s]?)?\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$";

    private static final String ITU_T_PATTERN =
        "^\\+(?:[0-9] ?){6,14}[0-9]$";

    private static final String EPP_PATTERN =
        "^\\+[0-9]{1,3}\\.[0-9]{4,14}(?:x.+)?$";

    private static final String ALL_DIGITS_PATTERN = "^[0-9]{7,17}$";

    @JsonCreator
    public
    PhoneNumber(@JsonProperty("phoneNumber") String phoneNumber)
    {
        Objects.requireNonNull(phoneNumber, "phoneNumber cannot be null");
        validatePhoneNumber(phoneNumber);
        phone = phoneNumber;
    }

    public
    PhoneNumber(PhoneNumber other)
    {
        phone = other.phone;
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
        return phone.compareToIgnoreCase(other.phone);
    }

    public boolean
    equals(PhoneNumber other)
    {
        return getDigitsOnly().equals(other.getDigitsOnly());
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
        return 51 * phone.hashCode();
    }

    @Override
    @JsonProperty("phoneNumber")
    public String
    toString()
    {
        return phone;
    }

    @JsonIgnore
    public String
    getDigitsOnly()
    {
        return
            phone
                .chars()
                .mapToObj(c -> (char)c)
                .filter(Character::isDigit)
                .collect(
                    StringBuilder::new,
                    StringBuilder::append,
                    StringBuilder::append)
                .toString();
    }

    public static PhoneNumber
    of(String phoneNumber)
    {
        return new PhoneNumber(phoneNumber);
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

        if (input.matches(ALL_DIGITS_PATTERN))
            return true;

        return false;
    }
    
}

//////////////////////////////////////////////////////////////////////////////
