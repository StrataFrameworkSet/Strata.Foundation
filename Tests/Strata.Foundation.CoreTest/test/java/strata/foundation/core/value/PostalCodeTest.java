//////////////////////////////////////////////////////////////////////////////
// PostalCodeTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import strata.foundation.core.mapper.BasicPostalCodeToCountryCodeMapper;
import strata.foundation.core.mapper.ObjectMapperSupplier;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class PostalCodeTest
{
    @AfterEach
    public void
    tearDown()
    {
        Locale.setDefault(Locale.US);
    }

    @ParameterizedTest
    @MethodSource("validInputs")
    public void
    testMapping(Locale locale,String input) throws JsonProcessingException
    {
        Locale.setDefault(locale);

        ObjectMapper mapper = new ObjectMapperSupplier().get();
        PostalCode   expected = PostalCode.of(input);
        PostalCode   actual =
            mapper.readValue(
                mapper.writeValueAsString(expected),PostalCode.class);

        System.out.println(mapper.writeValueAsString(expected));
        assertEquals(expected,actual);
    }

    @ParameterizedTest
    @MethodSource("invalidInputs")
    public void
    testInvalidInputs(Locale locale,String input)
    {
        Locale.setDefault(locale);

        try
        {
            PostalCode  actual = PostalCode.of(input);
            Set<String> countryCodes =
                new BasicPostalCodeToCountryCodeMapper()
                    .map(actual.toString());

            if (!countryCodes.contains(locale.getCountry()))
                throw
                    new IllegalArgumentException(
                        "Incorrect format for " + locale.getCountry());

            fail("Expected exception for invalid input: " + input);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("Caught expected exception for input: " + input);
        }
    }

    public static Stream<Arguments>
    validInputs()
    {
        return
            Stream.of(
                // United States (5-digit and 9-digit formats)
                Arguments.of(Locale.US, "90703"),
                Arguments.of(Locale.US, "90703-1234"),
                Arguments.of(Locale.US, "90703 1234"),

                // Canada
                Arguments.of(Locale.CANADA, "K1A 0B1"),
                Arguments.of(Locale.CANADA, "H3Z 2Y7"),
                Arguments.of(Locale.CANADA, "V5K2T2"),

                // Germany
                Arguments.of(Locale.GERMANY, "04107"),
                Arguments.of(Locale.GERMANY, "10115"),
                Arguments.of(Locale.GERMANY, "80331"),

                // UK
                Arguments.of(new Locale("en", "GB"), "SW1A 1AA"),
                Arguments.of(new Locale("en", "GB"), "M1 1AA"),
                Arguments.of(new Locale("en", "GB"), "B33 8TH"),
                Arguments.of(new Locale("en", "GB"), "CR2 6XH"),
                Arguments.of(new Locale("en", "GB"), "GIR 0AA"),

                // France
                Arguments.of(Locale.FRANCE, "75001"),
                Arguments.of(Locale.FRANCE, "13001"),
                Arguments.of(Locale.FRANCE, "97400"),

                // Italy
                Arguments.of(Locale.ITALY, "00144"),
                Arguments.of(Locale.ITALY, "20019"),

                // Spain
                Arguments.of(new Locale("es", "ES"), "28001"),
                Arguments.of(new Locale("es", "ES"), "41001"),

                // Netherlands
                Arguments.of(new Locale("nl", "NL"), "1011 AB"),
                Arguments.of(new Locale("nl", "NL"), "1234AB"),

                // Australia
                Arguments.of(new Locale("en", "AU"), "2000"),
                Arguments.of(new Locale("en", "AU"), "3000"),

                // Japan
                Arguments.of(Locale.JAPAN, "100-0001"),
                Arguments.of(Locale.JAPAN, "530-0001"),

                // Brazil
                Arguments.of(new Locale("pt", "BR"), "01001-000"),
                Arguments.of(new Locale("pt", "BR"), "20010020"),

                // Sweden
                Arguments.of(new Locale("sv", "SE"), "111 22"),
                Arguments.of(new Locale("sv", "SE"), "41323"),

                // China
                Arguments.of(Locale.CHINA, "100001"),
                Arguments.of(Locale.CHINA, "200001"),

                // India
                Arguments.of(new Locale("hi", "IN"), "110001"),
                Arguments.of(new Locale("hi", "IN"), "400001"),

                // Russia
                Arguments.of(new Locale("ru", "RU"), "101000"),
                Arguments.of(new Locale("ru", "RU"), "191028"),

                // South Korea
                Arguments.of(Locale.KOREA, "123-456"),
                Arguments.of(Locale.KOREA, "040-714"),

                // Switzerland
                Arguments.of(new Locale("de", "CH"), "1000"),
                Arguments.of(new Locale("fr", "CH"), "8001"),

                // Special regions
                Arguments.of(new Locale("fr", "MC"), "98000"), // Monaco
                Arguments.of(new Locale("it", "SM"), "47890"), // San Marino
                Arguments.of(new Locale("it", "VA"), "00120"), // Vatican City
                Arguments.of(new Locale("sv", "AX"), "22100"), // Åland Islands

                // Dependencies
                Arguments.of(new Locale("en", "JE"), "JE2 3NN"), // Jersey
                Arguments.of(new Locale("en", "GG"), "GY1 1AA"), // Guernsey
                Arguments.of(new Locale("en", "IM"), "IM1 1AA"), // Isle of Man

                // Other European countries
                Arguments.of(new Locale("cs", "CZ"), "110 00"), // Czech Republic
                Arguments.of(new Locale("sk", "SK"), "811 01"), // Slovakia
                Arguments.of(new Locale("el", "GR"), "104 31"), // Greece
                Arguments.of(new Locale("pl", "PL"), "00-001"), // Poland
                Arguments.of(new Locale("fi", "FI"), "00100"), // Finland
                Arguments.of(new Locale("hu", "HU"), "1011"),  // Hungary
                Arguments.of(new Locale("pt", "PT"), "1000-001") // Portugal
            );
    }

    public static Stream<Arguments>
    invalidInputs()
    {
        return Stream.of(
            // United States - invalid formats
            Arguments.of(Locale.US, "9070"),         // Too short (needs 5 digits)
            Arguments.of(Locale.US, "907031"),       // Invalid length
            Arguments.of(Locale.US, "90703-123"),    // Incomplete ZIP+4
            Arguments.of(Locale.US, "9O703"),        // Contains letter
            Arguments.of(Locale.US, "ABCDE"),        // All letters

            // Canada - invalid formats
            Arguments.of(Locale.CANADA, "K1A0B"),    // Incomplete
            Arguments.of(Locale.CANADA, "K1A 0B"),   // Incomplete
            Arguments.of(Locale.CANADA, "K10 0B1"),  // Digit in wrong position
            Arguments.of(Locale.CANADA, "1K1 0B1"),  // Starts with digit
            Arguments.of(Locale.CANADA, "KIAOB1"),   // Contains "I" (not used)

            // UK - invalid formats
            Arguments.of(new Locale("en", "GB"), "SW1A 1A"),   // Incomplete inward code
            Arguments.of(new Locale("en", "GB"), "SW1A1A"),    // Missing space
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

            // Japan - invalid formats
            Arguments.of(Locale.JAPAN, "123-45"),    // Wrong segment lengths
            Arguments.of(Locale.JAPAN, "1234-567"),  // Wrong segment lengths
            Arguments.of(Locale.JAPAN, "12345678"),  // Too long
            Arguments.of(Locale.JAPAN, "123-ABC"),   // Contains letters

            // Poland - invalid formats
            Arguments.of(new Locale("pl", "PL"), "123-45"),    // Wrong segment lengths
            Arguments.of(new Locale("pl", "PL"), "12-34"),     // Wrong segment lengths
            Arguments.of(new Locale("pl", "PL"), "1-12345"),   // Wrong segment lengths
            Arguments.of(new Locale("pl", "PL"), "AB-123"),    // Contains letters

            // Netherlands - invalid formats
            Arguments.of(new Locale("nl", "NL"), "123 ABC"),  // Wrong number format
            Arguments.of(new Locale("nl", "NL"), "12345 AB"), // Too many digits
            Arguments.of(new Locale("nl", "NL"), "1234 A"),   // Incomplete letters
            Arguments.of(new Locale("nl", "NL"), "1234 ABC"), // Too many letters

            // General invalid patterns
            Arguments.of(Locale.US, ""),              // Empty string
            Arguments.of(Locale.GERMANY, " "),        // Just whitespace
            Arguments.of(Locale.FRANCE, "ABC-DEF"),   // All letters with separator
            Arguments.of(Locale.JAPAN, "#12345"),     // Special characters
            Arguments.of(Locale.UK, "!@#$%"),         // Special characters
            Arguments.of(Locale.CANADA, "K1A 0B1!"),  // Valid with extra character

            // Special cases - non-existent postal codes
            Arguments.of(Locale.US, "00000"),         // Non-existent code
            Arguments.of(new Locale("en", "GB"), "QQ9 9QQ")  // Non-existent area code
        );
    }
}

//////////////////////////////////////////////////////////////////////////////
