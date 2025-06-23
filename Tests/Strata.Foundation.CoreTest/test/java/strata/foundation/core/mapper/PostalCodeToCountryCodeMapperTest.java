//////////////////////////////////////////////////////////////////////////////
// PostalCodeToCountryCodeMapperTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Locale;
import java.util.Locale.Builder;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void
    testUkPostalCodeRegex()
    {
        final String OUTWARD_CODE_REGEX =
            // common outward codes
            "((A[BL]?|B[ABDHLNRSTX]?|C[ABFHMORTVW]|D[ADEGHLNTY]|E[HNX]|F[KY]|G[LU]?|H[ADGPRSUX]|I[GPV]|K[ATWY]|L[ADELNSU]?|M[EKL]?|N[EGNPRW]|O[LX]|P[AEHLOR]|R[GHM]|S[AEGKLMNOPRSTY]?|T[ADFNQRSW]|UB|W[ADFNRSV]|XX|YO|ZE)\\d{1,2})|" +
            // London specific outward codes
            "((E[1-9][0-9]?|E1W|EC[1-4][AMNPRVY]|EC50)|N[1-9][0-9]?|N1[CP]|NW[1-9][0-9]?|NW1W|SE[1-9][0-9]?|SE1P|SW1[AEHPVWXY]|SW[2-9][0-9]?|W1[ABCDFGHJKSTUW]|W[2-9][0-9]?|WC1[ABEHNRVX]|WC2[ABEHNR])";
        final String INWARD_CODE_REGEX = "([0-9][ABD-HJLN-UW-Z]{2})";
        final String SPECIAL_CODE_REGEX = "(GIR[ ]?0AA|BFPO[ ]?\\d{1,4}|XM4[ ]?5HQ)";
        final String OVERSEAS_TERRITORIES_REGEX = "((AI[ \\-]?2640)|(ASCN|STHL|TDCU|BBND|BIQQ|FIQQ|PCRN|SIQQ|TKCA)[ ]?1ZZ|GX11[ ]?1AA)";
        final String POSSIBLE_REGEX_BUG_WORKAROUND = "([BMGL][1-9][0-9]?" + "[ ]?" + INWARD_CODE_REGEX + ")";
        final String UK_POSTAL_CODE_REGEX =
            String.format(
                "(%s)[ ]?%s|%s|%s|%s",
                OUTWARD_CODE_REGEX,
                INWARD_CODE_REGEX,
                SPECIAL_CODE_REGEX,
                OVERSEAS_TERRITORIES_REGEX,
                POSSIBLE_REGEX_BUG_WORKAROUND);

        assertTrue("AB10".matches(OUTWARD_CODE_REGEX));
        assertTrue("AB10 ".matches("(" + OUTWARD_CODE_REGEX + ")[ ]?"));
        assertTrue("1XG".matches(INWARD_CODE_REGEX));
        assertTrue(" 1XG".matches("[ ]?" + INWARD_CODE_REGEX));
        assertTrue("AB10 1XG".matches(UK_POSTAL_CODE_REGEX));
    }

    private static Stream<Arguments>
    getValidPostalCodes()
    {
        return
            Stream.of(
                // United States
                Arguments.of(Locale.US, "12345", Set.of("US")),
                Arguments.of(Locale.US, "90210", Set.of("US")),
                Arguments.of(Locale.US, "12345-6789", Set.of("US")),
                Arguments.of(Locale.US, "20500-0001", Set.of("US")),

                // Canada
                Arguments.of(Locale.CANADA, "A1B 2C3", Set.of("CA")),
                Arguments.of(Locale.CANADA, "A1B2C3", Set.of("CA")),
                Arguments.of(Locale.CANADA, "K1A 0B1", Set.of("CA")),
                Arguments.of(Locale.CANADA, "H0H 0H0", Set.of("CA")),

                // United Kingdom
                Arguments.of(Locale.UK, "SW1A 1AA", Set.of("GB")),
                Arguments.of(Locale.UK, "SW1A1AA", Set.of("GB")),
                Arguments.of(Locale.UK, "M1 1AA", Set.of("GB")),
                Arguments.of(Locale.UK, "EC1A 1BB", Set.of("GB")),
                Arguments.of(Locale.UK, "W1A 0AX", Set.of("GB")),
                Arguments.of(Locale.UK, "GIR 0AA", Set.of("GB")),

                // Germany
                Arguments.of(Locale.GERMANY, "12345", Set.of("DE")),
                Arguments.of(Locale.GERMANY, "01067", Set.of("DE")),
                Arguments.of(Locale.GERMANY, "80331", Set.of("DE")),

                // France
                Arguments.of(Locale.FRANCE, "12345", Set.of("FR")),
                Arguments.of(Locale.FRANCE, "75001", Set.of("FR")),
                Arguments.of(Locale.FRANCE, "06000", Set.of("FR")),
                Arguments.of(Locale.FRANCE, "20123", Set.of("FR")), // Corsica

                // Italy
                Arguments.of(Locale.ITALY, "12345", Set.of("IT")),
                Arguments.of(Locale.ITALY, "00120", Set.of("IT")),
                Arguments.of(Locale.ITALY, "20019", Set.of("IT")),

                // Japan
                Arguments.of(Locale.JAPAN, "123-4567", Set.of("JP")),
                Arguments.of(Locale.JAPAN, "100-0001", Set.of("JP")),

                // Netherlands
                Arguments.of(new Locale("nl", "NL"), "1234 AB", Set.of("NL")),
                Arguments.of(new Locale("nl", "NL"), "1234AB", Set.of("NL")),
                Arguments.of(new Locale("nl", "NL"), "2585 EJ", Set.of("NL")),

                // Australia
                Arguments.of(new Locale("en", "AU"), "1234", Set.of("AU")),
                Arguments.of(new Locale("en", "AU"), "0800", Set.of("AU")),
                Arguments.of(new Locale("en", "AU"), "2000", Set.of("AU")),

                // Brazil
                Arguments.of(new Locale("pt", "BR"), "01001-000", Set.of("BR")),
                Arguments.of(new Locale("pt", "BR"), "01001000", Set.of("BR")),
                Arguments.of(new Locale("pt", "BR"), "70000-000", Set.of("BR")),

                // Poland
                Arguments.of(new Locale("pl", "PL"), "12-345", Set.of("PL")),
                Arguments.of(new Locale("pl", "PL"), "00-001", Set.of("PL")),
                Arguments.of(new Locale("pl", "PL"), "80-001", Set.of("PL")),

                // China
                Arguments.of(Locale.CHINA, "123456", Set.of("CN")),
                Arguments.of(Locale.CHINA, "100000", Set.of("CN")),
                Arguments.of(Locale.CHINA, "200000", Set.of("CN")),

                // Special territories and small countries
                Arguments.of(new Builder().setRegion("MC").build(), "98000", Set.of("MC")),
                Arguments.of(new Builder().setRegion("MC").build(), "98001", Set.of("MC")),
                Arguments.of(new Builder().setRegion("SM").build(), "47890", Set.of("SM")),
                Arguments.of(new Builder().setRegion("SM").build(), "47891", Set.of("SM")),
                Arguments.of(new Builder().setRegion("VA").build(), "00120", Set.of("VA")),
                Arguments.of(new Builder().setRegion("AX").build(), "22000", Set.of("AX")),
                Arguments.of(new Builder().setRegion("AX").build(), "22100", Set.of("AX")),

                // Cross-checking cases (international detection)
                Arguments.of(Locale.US, "A1B 2C3", Set.of("CA")),
                Arguments.of(Locale.US, "SW1A 1AA", Set.of("GB"))
            );
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
