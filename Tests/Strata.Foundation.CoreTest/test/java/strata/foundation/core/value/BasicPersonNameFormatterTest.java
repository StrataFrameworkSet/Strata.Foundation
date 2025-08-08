/// ///////////////////////////////////////////////////////////////////////////
// BasicPersonNameFormatterTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("CommitStage")
public
class BasicPersonNameFormatterTest
{
    private BasicPersonNameFormatter formatter;

    @BeforeEach
    public void
    setUp()
    {
        formatter = new BasicPersonNameFormatter();
    }

    @AfterEach
    public void
    tearDown()
    {
        formatter = null;
    }

    @ParameterizedTest
    @MethodSource("validTestCases")
    public void
    testFormat(PersonName name,String suffix,String expected)
    {
        PersonNameBuilder builder = new PersonNameBuilder();

        name
            .getTitle()
            .ifPresent(title -> builder.setTitle(title));
        name
            .getMiddleName()
            .ifPresent(middleName -> builder.setMiddleName(middleName));

        builder
            .setFirstName(name.getFirstName())
            .setLastName(name.getLastName())
            .setSuffix(suffix);
        assertEquals(expected,formatter.format(builder.build()), "Formatted name did not match expected output");
    }

    private static Stream<Arguments>
    validTestCases()
    {
        return
            Stream.of(
                // Basic cases with no title or suffix
                Arguments.of(
                    PersonName.of("John", null, "Smith"),
                    null,
                    "John Smith"),
                Arguments.of(
                    PersonName.of("Emily", "Rose", "Johnson"),
                    null,
                    "Emily Rose Johnson"),

                // Common titles
                Arguments.of(
                    PersonName.of("Mr.", "Robert", null, "Williams"),
                    null,
                    "Mr. Robert Williams"),
                Arguments.of(
                    PersonName.of("Mrs.", "Jennifer", "Lynn", "Rodriguez"),
                    null,
                    "Mrs. Jennifer Lynn Rodriguez"),
                Arguments.of(
                    PersonName.of("Ms.", "Sarah", null, "Thompson"),
                    null,
                    "Ms. Sarah Thompson"),
                Arguments.of(
                    PersonName.of("Dr.", "Michael", "James", "Chen"),
                    null,
                    "Dr. Michael James Chen"),

                // Professional titles
                Arguments.of(
                    PersonName.of("Prof.", "David", null, "Miller"),
                    null,
                    "Prof. David Miller"),
                Arguments.of(
                    PersonName.of("Hon.", "Patricia", "Marie", "Washington"),
                    null,
                    "Hon. Patricia Marie Washington"),
                Arguments.of(
                    PersonName.of("Rev.", "Thomas", "Edward", "Jones"),
                    null,
                    "Rev. Thomas Edward Jones"),

                // Military titles
                Arguments.of(
                    PersonName.of("Capt.", "James", "T.", "Kirk"),
                    null,
                    "Capt. James T. Kirk"),
                Arguments.of(
                    PersonName.of("Gen.", "Douglas", null, "MacArthur"),
                    null,
                    "Gen. Douglas MacArthur"),

                // Common suffixes
                Arguments.of(
                    PersonName.of("Martin", "Luther", "King"),
                    "Jr.",
                    "Martin Luther King, Jr."),
                Arguments.of(
                    PersonName.of("Henry", "James", "Johnson"),
                    "III",
                    "Henry James Johnson, III"),
                Arguments.of(
                    PersonName.of("Elizabeth", null, "Smith"),
                    "PhD",
                    "Elizabeth Smith, PhD"),

                // Titles and suffixes together
                Arguments.of(
                    PersonName.of("Dr.", "Richard", "Lee", "Wilson"),
                    "MD",
                    "Dr. Richard Lee Wilson, MD"),
                Arguments.of(
                    PersonName.of("Mrs.", "Susan", "Marie", "Taylor"),
                    "Esq.",
                    "Mrs. Susan Marie Taylor, Esq."),
                Arguments.of(
                    PersonName.of("Rev.", "William", "Joseph", "Brown"),
                    "PhD",
                    "Rev. William Joseph Brown, PhD"),

                // Non-standard and international titles
                Arguments.of(
                    PersonName.of("Mx.", "Alex", null, "Morgan"),
                    null,
                    "Mx. Alex Morgan"),
                Arguments.of(
                    PersonName.of("Sir", "Elton", "Hercules", "John"),
                    null,
                    "Sir Elton Hercules John"),

                // Edge cases
                Arguments.of(
                    PersonName.of("Dr.", "Maria", "Elena", "Garcia-Rodriguez"),
                    "PhD, MD",
                    "Dr. Maria Elena Garcia-Rodriguez, PhD, MD"),
                Arguments.of(
                    PersonName.of("Prof.", "J.", "Robert", "Oppenheimer"),
                    "ScD",
                    "Prof. J. Robert Oppenheimer, ScD"));
    }
}

//////////////////////////////////////////////////////////////////////////////
