/// ///////////////////////////////////////////////////////////////////////////
// UsPostalAddressFormatterTest.java
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
class UsPostalAddressFormatterTest
{
    private UsPostalAddressFormatter formatter;

    @BeforeEach
    public void
    setUp()
    {
        formatter = new UsPostalAddressFormatter();
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
    testFormat(
        String addressee,
        PostalAddress address,
        String expected)
    {
        String actual =
            formatter
                .setAddressee(addressee)
                .format(address);

        assertEquals(expected,actual);
    }

    private static Stream<Arguments>
    validTestCases()
    {
        return
            Stream.of(
                Arguments.of(
                    "Mr. John Doe",
                    new PostalAddressBuilder()
                        .setStreet("123 Main St")
                        .setCity("Springfield")
                        .setState("IL")
                        .setPostalCode("62701")
                        .build(),
                    "Mr. John Doe\n123 Main St\nSpringfield, IL 62701"),
                Arguments.of(
                    "Dr. Sarah Johnson",
                    new PostalAddressBuilder()
                        .setStreet("350 Fifth Avenue, Suite 3300")
                        .setCity("New York")
                        .setState("NY")
                        .setPostalCode("10118")
                        .build(),
                    "Dr. Sarah Johnson\n350 Fifth Avenue, Suite 3300\nNew York, NY 10118"),
                Arguments.of(
                    "The Rodriguez Family",
                    new PostalAddressBuilder()
                        .setStreet("1600 Amphitheatre Parkway")
                        .setCity("Mountain View")
                        .setState("CA")
                        .setPostalCode("94043")
                        .build(),
                    "The Rodriguez Family\n1600 Amphitheatre Parkway\nMountain View, CA 94043"),
                Arguments.of(
                    null,
                    new PostalAddressBuilder()
                        .setStreet("233 S Wacker Dr, Floor 103")
                        .setCity("Chicago")
                        .setState("IL")
                        .setPostalCode("60606")
                        .build(),
                    "233 S Wacker Dr, Floor 103\nChicago, IL 60606"),
                Arguments.of(
                    "Rev. Michael Thompson",
                    new PostalAddressBuilder()
                        .setStreet("400 Broad St")
                        .setCity("Seattle")
                        .setState("WA")
                        .setPostalCode("98109")
                        .build(),
                    "Rev. Michael Thompson\n400 Broad St\nSeattle, WA 98109"),
                Arguments.of(
                    "Prof. Robert Williams",
                    new PostalAddressBuilder()
                        .setStreet("1 Infinite Loop")
                        .setCity("Cupertino")
                        .setState("CA")
                        .setPostalCode("95014")
                        .build(),
                    "Prof. Robert Williams\n1 Infinite Loop\nCupertino, CA 95014"),
                Arguments.of(
                    "Mrs. Latisha Jackson",
                    new PostalAddressBuilder()
                        .setStreet("151 Beale St")
                        .setCity("Memphis")
                        .setState("TN")
                        .setPostalCode("38103")
                        .build(),
                    "Mrs. Latisha Jackson\n151 Beale St\nMemphis, TN 38103"),
                Arguments.of(
                    "Mx. Alex Morgan",
                    new PostalAddressBuilder()
                        .setStreet("1060 W Addison St")
                        .setCity("Chicago")
                        .setState("IL")
                        .setPostalCode("60613")
                        .build(),
                    "Mx. Alex Morgan\n1060 W Addison St\nChicago, IL 60613"),
                Arguments.of(
                    "Hon. James Wilson",
                    new PostalAddressBuilder()
                        .setStreet("1 Observatory Circle")
                        .setCity("Washington")
                        .setState("DC")
                        .setPostalCode("20008")
                        .build(),
                    "Hon. James Wilson\n1 Observatory Circle\nWashington, DC 20008"),
                Arguments.of(
                    "Dr. & Mrs. Martinez",
                    new PostalAddressBuilder()
                        .setStreet("1600 Pennsylvania Avenue NW")
                        .setCity("Washington")
                        .setState("DC")
                        .setPostalCode("20500")
                        .build(),
                    "Dr. & Mrs. Martinez\n1600 Pennsylvania Avenue NW\nWashington, DC 20500"),
                Arguments.of(
                    "Taylor Swift",
                    new PostalAddressBuilder()
                        .setStreet("200 Bourbon Street")
                        .setCity("New Orleans")
                        .setState("LA")
                        .setPostalCode("70130")
                        .build(),
                    "Taylor Swift\n200 Bourbon Street\nNew Orleans, LA 70130"),
                Arguments.of(
                    "Acme Corporation",
                    new PostalAddressBuilder()
                        .setStreet("30 Rockefeller Plaza")
                        .setCity("New York")
                        .setState("NY")
                        .setPostalCode("10112")
                        .build(),
                    "Acme Corporation\n30 Rockefeller Plaza\nNew York, NY 10112"));
    }
}

//////////////////////////////////////////////////////////////////////////////
