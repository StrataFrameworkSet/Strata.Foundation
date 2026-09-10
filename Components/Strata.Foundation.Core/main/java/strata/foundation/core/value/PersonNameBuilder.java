//////////////////////////////////////////////////////////////////////////////
// PersonNameBuilder.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * A mutable <a href="https://en.wikipedia.org/wiki/Builder_pattern">builder</a>
 * for incrementally assembling a {@link PersonName}. Each {@code setXxx}
 * method returns {@code this}, allowing calls to be chained fluently, and
 * {@code hasXxx}/{@code getXxx} accessors let callers inspect the fields that
 * have been set so far before calling {@link #build()} to produce the
 * resulting immutable {@link PersonName}. A first name and last name must be
 * set before {@link #build()} is called.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * PersonName name =
 *     new PersonNameBuilder()
 *         .setTitle("Dr.")
 *         .setFirstName("Jane")
 *         .setMiddleName("Q.")
 *         .setLastName("Doe")
 *         .setSuffix("Jr.")
 *         .build();
 * </pre>
 */
public
class PersonNameBuilder
{
    private String itsTitle;
    private String itsFirstName;
    private String itsMiddleName;
    private String itsLastName;
    private String itsSuffix;

    public
    PersonNameBuilder()
    {
        itsTitle = null;
        itsFirstName = null;
        itsMiddleName = null;
        itsLastName = null;
        itsSuffix = null;
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
    setSuffix(String suffix)
    {
        itsSuffix = suffix;
        return this;
    }

    public PersonNameBuilder
    clear()
    {
        itsTitle = null;
        itsFirstName = null;
        itsMiddleName = null;
        itsLastName = null;
        itsSuffix = null;
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

    public Optional<String>
    getSuffix() { return Optional.ofNullable(itsSuffix); }

    public boolean
    hasTitle() { return Objects.nonNull(itsTitle); }

    public boolean
    hasFirstName() { return Objects.nonNull(itsFirstName); }

    public boolean
    hasMiddleName() { return Objects.nonNull(itsMiddleName); }

    public boolean
    hasLastName() { return Objects.nonNull(itsLastName); }

    public boolean
    hasSuffix() { return Objects.nonNull(itsSuffix); }

    public PersonName
    build()
    {
        return
            new PersonName(
                itsTitle,
                itsFirstName,
                itsMiddleName,
                itsLastName,
                itsSuffix);
    }
}

//////////////////////////////////////////////////////////////////////////////
