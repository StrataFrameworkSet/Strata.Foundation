//////////////////////////////////////////////////////////////////////////////
// EmailAddress.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import strata.foundation.core.utility.ICopyable;

import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * An immutable value type representing a validated
 * <a href="https://en.wikipedia.org/wiki/Email_address">email address</a>.
 * The supplied string is checked on construction against a
 * local-part/domain pattern, rejecting values that do not resemble a
 * well-formed email address.
 * </p>
 * <p>
 * Equality and hashing are case-insensitive, and ordering via {@link
 * java.lang.Comparable} likewise compares the underlying string
 * case-insensitively. Copies are produced via {@link ICopyable#copy()},
 * and instances implement {@link java.io.Serializable}.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * EmailAddress emailAddress = EmailAddress.of("jane.doe@example.com");
 *
 * EmailAddress copy = emailAddress.copy();
 *
 * boolean same = emailAddress.equals(EmailAddress.of("JANE.DOE@EXAMPLE.COM"));
 * </pre>
 * </p>
 */
public
class EmailAddress
    implements ICopyable,Serializable,Comparable<EmailAddress>
{
    private String itsEmail;

    private static final String EMAIL_PATTERN =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public
    EmailAddress() {}

    @JsonCreator
    public
    EmailAddress(@JsonProperty("emailAddress") String emailAddress)
    {
        Objects.requireNonNull(emailAddress, "emailAddress cannot be null");
        validateEmailAddress(emailAddress);
        itsEmail = emailAddress;
    }

    public
    EmailAddress(EmailAddress other)
    {
        itsEmail = other.itsEmail;
    }

    @Override
    public EmailAddress
    copy() { return new EmailAddress(this); }

    @Override
    public int
    compareTo(EmailAddress other)
    {
        return itsEmail.compareToIgnoreCase(other.itsEmail);
    }

    public boolean
    equals(EmailAddress other)
    {
        return itsEmail.equalsIgnoreCase(other.itsEmail);
    }

    @Override
    public boolean
    equals(Object other)
    {
        return other instanceof EmailAddress e ? equals(e) : false;
    }

    @Override
    public int
    hashCode()
    {
        return 47 * itsEmail.toLowerCase().hashCode();
    }

    @Override
    @JsonProperty("emailAddress")
    public String
    toString()
    {
        return itsEmail;
    }

    public static EmailAddress
    of(String emailAddress)
    {
        return new EmailAddress(emailAddress);
    }

    private static void
    validateEmailAddress(String input)
    {
        if (!input.matches(EMAIL_PATTERN))
            throw
                new IllegalArgumentException(
                    "Unrecognized email format: " + input);
    }
}

//////////////////////////////////////////////////////////////////////////////