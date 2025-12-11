//////////////////////////////////////////////////////////////////////////////
// PersonName.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import strata.foundation.core.utility.HashCodeBuilder;
import strata.foundation.core.utility.ICopyable;
import strata.foundation.core.utility.Strings;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public
class PersonName
    implements ICopyable,Serializable,Comparable<PersonName>
{
    private String title;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;

    public
    PersonName(String first,String last)
    {
        this(null,first,null,last,null);
    }

    public
    PersonName(String first,String middle,String last)
    {
        this(null,first,middle,last,null);
    }

    @JsonCreator
    public
    PersonName(
        @JsonProperty("title")      String title,
        @JsonProperty("firstName")  String first,
        @JsonProperty("middleName") String middle,
        @JsonProperty("lastName")   String last,
        @JsonProperty("suffix")     String suffix)
    {
        this.title = Strings.toNonBlankOrNull(title);
        this.firstName = Objects.requireNonNull(first);
        this.middleName = Strings.toNonBlankOrNull(middle);
        this.lastName = Objects.requireNonNull(last);
        this.suffix = Strings.toNonBlankOrNull(suffix);
    }

    public
    PersonName(PersonName other)
    {
        title = other.title;
        firstName = other.firstName;
        middleName = other.middleName;
        lastName = other.lastName;
    }

    @Override
    public PersonName
    copy()
    {
        return new PersonName(this);
    }

    @Override
    public int
    compareTo(PersonName other)
    {
        return toString().compareTo(other.toString());
    }

    @Override
    public int
    hashCode()
    {
        return
            new HashCodeBuilder()
                .append(title)
                .append(firstName)
                .append(middleName)
                .append(lastName)
                .getHashCode();
    }

    @Override
    public boolean
    equals(Object other)
    {
        return other instanceof PersonName p ? equals(p) : false;
    }

    public boolean
    equals(PersonName other)
    {
        return toString().equals(other.toString());
    }

    public Optional<String>
    getTitle() { return Optional.ofNullable(title); }

    public String
    getFirstName() { return firstName; }

    public Optional<String>
    getMiddleName() { return Optional.ofNullable(middleName); }

    public String
    getLastName() { return lastName; }

    public Optional<String>
    getSuffix() { return Optional.ofNullable(suffix); }

    public String
    toString()
    {
        StringBuilder builder = new StringBuilder();

        getTitle().ifPresent(t -> builder.append(t).append(' '));
        builder.append(getFirstName()).append(' ');
        getMiddleName().ifPresent(m -> builder.append(m).append(' '));
        builder.append(getLastName());
        getSuffix().ifPresent(s -> builder.append(", ").append(s));

        return builder.toString();
    }

    public static PersonName
    of(String first,String last)
    {
        return new PersonName(first,last);
    }

    public static PersonName
    of(String first,String middle,String last)
    {
        return new PersonName(first,middle,last);
    }

    public static PersonName
    of(String title,String first,String middle,String last)
    {
        return new PersonName(title,first,middle,last,null);
    }

    public static PersonName
    of(String title,String first,String middle,String last,String suffix)
    {
        return new PersonName(title,first,middle,last,suffix);
    }

}

//////////////////////////////////////////////////////////////////////////////
