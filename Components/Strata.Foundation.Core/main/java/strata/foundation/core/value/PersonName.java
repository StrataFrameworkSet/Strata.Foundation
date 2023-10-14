//////////////////////////////////////////////////////////////////////////////
// PersonName.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import strata.foundation.core.utility.HashCodeBuilder;
import strata.foundation.core.utility.ICopyable;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public
class PersonName
    implements ICopyable,Serializable,Comparable<PersonName>
{
    private Optional<String> title;
    private String           firstName;
    private Optional<String> middleName;
    private String           lastName;

    public
    PersonName(String first,String last)
    {
        this(null,first,null,last);
    }

    public
    PersonName(String first,String middle,String last)
    {
        this(null,first,middle,last);
    }

    @JsonCreator
    public
    PersonName(
        @JsonProperty("title")      String t,
        @JsonProperty("firstName")  String first,
        @JsonProperty("middleName") String middle,
        @JsonProperty("lastName")   String last)
    {
        title = Optional.ofNullable(t);
        firstName = Objects.requireNonNull(first);
        middleName = Optional.ofNullable(middle);
        lastName = Objects.requireNonNull(last);
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
    getTitle() { return title; }

    public String
    getFirstName() { return firstName; }

    public Optional<String>
    getMiddleName() { return middleName; }

    public String
    getLastName() { return lastName; }

    public String
    toString()
    {
        StringBuilder builder = new StringBuilder();

        title.ifPresent(t -> builder.append(t).append(' '));
        builder.append(firstName).append(' ');
        middleName.ifPresent(m -> builder.append(m).append(' '));
        builder.append(lastName);

        return builder.toString();
    }
}

//////////////////////////////////////////////////////////////////////////////
