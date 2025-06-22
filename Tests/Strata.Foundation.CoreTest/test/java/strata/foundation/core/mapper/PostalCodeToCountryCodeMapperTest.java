//////////////////////////////////////////////////////////////////////////////
// PostalCodeToCountryCodeMapperTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Locale;
import java.util.Locale.Builder;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Tag("CommitStage")
public
class PostalCodeToCountryCodeMapperTest
{

    @ParameterizedTest
    @MethodSource("getValidPostalCodes")
    public void
    testValidInput(Locale locale,String postalCode,Set<String> expectedCountryCodes)
    {
        IPostalCodeToCountryCodeMapper mapper =
            new BasicPostalCodeToCountryCodeMapper(locale);
        Set<String> actualCountryCodes = mapper.map(postalCode);

        assertEquals(
            expectedCountryCodes
                .stream()
                .sorted()
                .collect(Collectors.toList()),
            actualCountryCodes
                .stream()
                .sorted()
                .collect(Collectors.toList()));
    }

    @ParameterizedTest
    @MethodSource("getInvalidInputs")
    public void
    testInvalidInput(Locale locale,String postalCode)
    {
        IPostalCodeToCountryCodeMapper mapper =
            new BasicPostalCodeToCountryCodeMapper(locale);
        Set<String> actualCountryCodes = mapper.map(postalCode);

        assertFalse(
            actualCountryCodes.contains(locale.getCountry()),

            "Returned: " +
                actualCountryCodes
                    .stream()
                    .sorted()
                    .collect(Collectors.joining(",")));
    }

    private static Stream<Arguments>
    getValidPostalCodes()
    {
        return
            Stream.of(
                Arguments.of(
                    Locale.US,
                    "12345",
                    Set.of("US")),
                Arguments.of(
                    Locale.GERMANY,
                    "12345",
                    Set.of("DE")),
                Arguments.of(
                    Locale.FRANCE,
                    "12345",
                    Set.of("FR")),
                Arguments.of(
                    Locale.ITALY,
                    "12345",
                    Set.of("IT")),
                Arguments.of(
                    Locale.US,
                    "12345-6789",
                    Set.of("US")),
                Arguments.of(
                    Locale.US,
                    "A1B 2C3",
                    Set.of("CA")),
                Arguments.of(
                    Locale.US,
                    "A1B2C3",
                    Set.of("CA")),
                Arguments.of(
                    Locale.US,
                    "SW1A 1AA",
                    Set.of("GB")),
                Arguments.of(
                    Locale.US,
                    "SW1A1AA",
                    Set.of("GB")),
                Arguments.of(
                    new Builder()
                        .setRegion("MC")
                        .build(),
                    "98001",
                    Set.of("MC")),
                Arguments.of(
                    new Builder()
                        .setRegion("SM")
                        .build(),
                    "47890",
                    Set.of("SM")),
                Arguments.of(
                    new Builder()
                        .setRegion("VA")
                        .build(),
                    "00120",
                    Set.of("VA")),
                Arguments.of(
                    new Builder()
                        .setRegion("AX")
                        .build(),
                    "22000",
                    Set.of("AX")));
    }

    public static Stream<Arguments>
    getInvalidInputs()
    {
        return Stream.of(
            // United States - invalid formats
            Arguments.of(Locale.US, "1234"),         // Too short (needs 5 digits)
            Arguments.of(Locale.US, "123456"),       // Too long for base format
            Arguments.of(Locale.US, "90703-123"),    // Incomplete ZIP+4
            Arguments.of(Locale.US, "9O703"),        // Contains letter
            Arguments.of(Locale.US, "ABCDE"),        // All letters

            // Canada - invalid formats
            Arguments.of(Locale.CANADA, "K1A0B"),    // Incomplete
            Arguments.of(Locale.CANADA, "K1A BB1"),  // Invalid character in position 4
            Arguments.of(Locale.CANADA, "K10 0B1"),  // Digit in wrong position
            Arguments.of(Locale.CANADA, "1K1 0B1"),  // Starts with digit
            Arguments.of(Locale.CANADA, "KIAOB1"),   // Contains "I" (not used)

            // UK - invalid formats
            Arguments.of(new Locale("en", "GB"), "S1A 1AA"),   // Incomplete outward code
            Arguments.of(new Locale("en", "GB"), "SW1A 1A"),   // Incomplete inward code
            Arguments.of(new Locale("en", "GB"), "SW1AA1A"),   // Invalid format
            Arguments.of(new Locale("en", "GB"), "SW1A 1AAA"), // Too long
            Arguments.of(new Locale("en", "GB"), "1234 567"),  // All numeric

            // Germany - invalid formats
            Arguments.of(Locale.GERMANY, "1234"),     // Too short
            Arguments.of(Locale.GERMANY, "123456"),   // Too long
            Arguments.of(Locale.GERMANY, "1234A"),    // Contains letter
            Arguments.of(Locale.GERMANY, "0"),        // Too short

            // France - invalid formats
            Arguments.of(Locale.FRANCE, "7500"),     // Too short
            Arguments.of(Locale.FRANCE, "750011"),   // Too long
            Arguments.of(Locale.FRANCE, "A5001"),    // Contains letter

            // Italy - invalid formats
            Arguments.of(Locale.ITALY, "1234"),      // Too short
            Arguments.of(Locale.ITALY, "123456"),    // Too long
            Arguments.of(Locale.ITALY, "12B45"),     // Contains letter

            // Japan - invalid formats
            Arguments.of(Locale.JAPAN, "123-45"),    // Wrong segment lengths
            Arguments.of(Locale.JAPAN, "1234-567"),  // Wrong segment lengths
            Arguments.of(Locale.JAPAN, "123-ABCD"),  // Contains letters

            // Netherlands - invalid formats
            Arguments.of(new Locale("nl", "NL"), "123 ABC"),  // Wrong number format
            Arguments.of(new Locale("nl", "NL"), "12345 AB"), // Too many digits
            Arguments.of(new Locale("nl", "NL"), "1234 A"),   // Incomplete letters
            Arguments.of(new Locale("nl", "NL"), "1234 ABC"), // Too many letters

            // Australia - invalid formats
            Arguments.of(new Locale("en", "AU"), "123"),      // Too short
            Arguments.of(new Locale("en", "AU"), "12345"),    // Too long
            Arguments.of(new Locale("en", "AU"), "123A"),     // Contains letter

            // Brazil - invalid formats
            Arguments.of(new Locale("pt", "BR"), "01001-00"), // Too short
            Arguments.of(new Locale("pt", "BR"), "01001-0000"), // Too long
            Arguments.of(new Locale("pt", "BR"), "0100A-000"), // Contains letter

            // Poland - invalid formats
            Arguments.of(new Locale("pl", "PL"), "123-45"),    // Wrong segment lengths
            Arguments.of(new Locale("pl", "PL"), "12-34"),     // Too short
            Arguments.of(new Locale("pl", "PL"), "12-34A"),    // Contains letter

            // China - invalid formats
            Arguments.of(Locale.CHINA, "12345"),     // Too short
            Arguments.of(Locale.CHINA, "1234567"),   // Too long
            Arguments.of(Locale.CHINA, "12345A"),    // Contains letter

            // Special regions - invalid formats
            Arguments.of(new Locale("fr", "MC"), "9800"),     // Monaco - too short
            Arguments.of(new Locale("fr", "MC"), "980001"),   // Monaco - too long
            Arguments.of(new Locale("it", "SM"), "4789"),     // San Marino - too short
            Arguments.of(new Locale("it", "VA"), "0012A"),    // Vatican - contains letter

            // General invalid patterns
            Arguments.of(Locale.US, ""),              // Empty string
            Arguments.of(Locale.US, " "),             // Just whitespace
            Arguments.of(Locale.US, "ABC-DEF"),       // All letters with separator
            Arguments.of(Locale.US, "#12345"),        // Special characters
            Arguments.of(Locale.US, "12345!"),        // Valid with trailing special char
            Arguments.of(Locale.US, "12345-abcd"),    // Invalid ZIP+4 format

            // Extra edge cases
            Arguments.of(Locale.FRANCE, "2A000"),     // Corsica format with invalid number
            Arguments.of(new Locale("en", "GB"), "GIR 0AB")  // Almost correct special case (GIR 0AA)
        );
    }
}

//////////////////////////////////////////////////////////////////////////////
