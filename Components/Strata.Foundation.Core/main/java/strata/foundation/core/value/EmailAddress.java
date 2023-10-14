//////////////////////////////////////////////////////////////////////////////
// EmailAddress.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import strata.foundation.core.utility.ICopyable;

import java.io.Serializable;

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