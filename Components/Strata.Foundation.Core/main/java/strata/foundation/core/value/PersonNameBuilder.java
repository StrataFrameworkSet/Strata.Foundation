//////////////////////////////////////////////////////////////////////////////
// PersonNameBuilder.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import java.util.Objects;
import java.util.Optional;

public
class PersonNameBuilder
{
    private String itsTitle;
    private String itsFirstName;
    private String itsMiddleName;
    private String itsLastName;

    public
    PersonNameBuilder()
    {
        itsTitle = null;
        itsFirstName = null;
        itsMiddleName = null;
        itsLastName = null;
    }

    public PersonNameBuilder
    setTitle(String title)
    {
        itsTitle = title;
        return this;
    }

    public PersonNameBuilder
    setFirstName(String firstName)
    {
        itsFirstName = firstName;
        return this;
    }

    public PersonNameBuilder
    setMiddleName(String middleName)
    {
        itsMiddleName = middleName;
        return this;
    }

    public PersonNameBuilder
    setLastName(String lastName)
    {
        itsLastName = lastName;
        return this;
    }

    public PersonNameBuilder
    clear()
    {
        itsTitle = null;
        itsFirstName = null;
        itsMiddleName = null;
        itsLastName = null;
        return this;
    }

    public Optional<String>
    getTitle() { return Optional.ofNullable(itsTitle); }

    public String
    getFirstName() { return Objects.requireNonNull(itsFirstName); }

    public Optional<String>
    getMiddleName() { return Optional.ofNullable(itsMiddleName); }

    public String
    getLastName() { return Objects.requireNonNull(itsLastName); }

    public boolean
    hasTitle() { return Objects.nonNull(itsTitle); }

    public boolean
    hasFirstName() { return Objects.nonNull(itsFirstName); }

    public boolean
    hasMiddleName() { return Objects.nonNull(itsMiddleName); }

    public boolean
    hasLastName() { return Objects.nonNull(itsLastName); }

    public PersonName
    build()
    {
        return new PersonName(itsTitle,itsFirstName,itsMiddleName,itsLastName);
    }
}

//////////////////////////////////////////////////////////////////////////////
