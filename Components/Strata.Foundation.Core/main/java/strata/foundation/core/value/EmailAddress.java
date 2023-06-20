//////////////////////////////////////////////////////////////////////////////
// EmailAddress.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import java.io.Serializable;

public
class EmailAddress
    implements Serializable
{
    private String itsEmail;

    private static final String EMAIL_PATTERN =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public
    EmailAddress() {}

    public
    EmailAddress(String emailAddress)
    {
        validateEmailAddress(emailAddress);
        itsEmail = emailAddress;
    }

    public boolean
    equals(EmailAddress other)
    {
        return itsEmail.equals(other.itsEmail);
    }

    @Override
    public boolean
    equals(Object other)
    {
        return
            other instanceof EmailAddress && equals((EmailAddress)other);
    }

    @Override
    public int
    hashCode()
    {
        return 47 * itsEmail.hashCode();
    }

    @Override
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