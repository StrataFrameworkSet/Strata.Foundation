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
            Stream.concat(
                validUkInputs(),
                Stream.of(
                    // United States (5-digit and 9-digit formats)
                    Arguments.of(Locale.US,"90703"),
                    Arguments.of(Locale.US,"90703-1234"),
                    Arguments.of(Locale.US,"90703 1234"),

                    // Canada
                    Arguments.of(Locale.CANADA,"K1A 0B1"),
                    Arguments.of(Locale.CANADA,"H3Z 2Y7"),
                    Arguments.of(Locale.CANADA,"V5K2T2"),

                    // Germany
                    Arguments.of(Locale.GERMANY,"04107"),
                    Arguments.of(Locale.GERMANY,"10115"),
                    Arguments.of(Locale.GERMANY,"80331"),

                    // UK
                    Arguments.of(Locale.UK,"SW1A 1AA"),
                    Arguments.of(Locale.UK,"M1 1AA"),
                    Arguments.of(Locale.UK,"B33 8TH"),
                    Arguments.of(Locale.UK,"CR2 6XH"),
                    Arguments.of(Locale.UK,"GIR 0AA"),

                    // France
                    Arguments.of(Locale.FRANCE,"75001"),
                    Arguments.of(Locale.FRANCE,"13001"),
                    Arguments.of(Locale.FRANCE,"97400"),

                    // Italy
                    Arguments.of(Locale.ITALY,"00144"),
                    Arguments.of(Locale.ITALY,"20019"),

                    // Spain
                    Arguments.of(new Locale("es","ES"),"28001"),
                    Arguments.of(new Locale("es","ES"),"41001"),

                    // Netherlands
                    Arguments.of(new Locale("nl","NL"),"1011 AB"),
                    Arguments.of(new Locale("nl","NL"),"1234AB"),

                    // Australia
                    Arguments.of(new Locale("en","AU"),"2000"),
                    Arguments.of(new Locale("en","AU"),"3000"),

                    // Japan
                    Arguments.of(Locale.JAPAN,"100-0001"),
                    Arguments.of(Locale.JAPAN,"530-0001"),

                    // Brazil
                    Arguments.of(new Locale("pt","BR"),"01001-000"),
                    Arguments.of(new Locale("pt","BR"),"20010020"),

                    // Sweden
                    Arguments.of(new Locale("sv","SE"),"111 22"),
                    Arguments.of(new Locale("sv","SE"),"41323"),

                    // China
                    Arguments.of(Locale.CHINA,"100001"),
                    Arguments.of(Locale.CHINA,"200001"),

                    // India
                    Arguments.of(new Locale("hi","IN"),"110001"),
                    Arguments.of(new Locale("hi","IN"),"400001"),

                    // Russia
                    Arguments.of(new Locale("ru","RU"),"101000"),
                    Arguments.of(new Locale("ru","RU"),"191028"),

                    // South Korea
                    Arguments.of(Locale.KOREA,"123-456"),
                    Arguments.of(Locale.KOREA,"040-714"),

                    // Switzerland
                    Arguments.of(new Locale("de","CH"),"1000"),
                    Arguments.of(new Locale("fr","CH"),"8001"),

                    // Special regions
                    Arguments.of(new Locale("fr","MC"),"98000"), // Monaco
                    Arguments.of(new Locale("it","SM"),"47890"), // San Marino
                    Arguments.of(new Locale("it","VA"),"00120"), // Vatican City
                    Arguments.of(new Locale("sv","AX"),"22100"), // Åland Islands

                    // Dependencies
                    Arguments.of(new Locale("en","JE"),"JE2 3NN"), // Jersey
                    Arguments.of(new Locale("en","GG"),"GY1 1AA"), // Guernsey
                    Arguments.of(new Locale("en","IM"),"IM1 1AA"), // Isle of Man
                    Arguments.of(Locale.UK,"JE2 3NN"), // Jersey
                    Arguments.of(Locale.UK,"GY1 1AA"), // Guernsey
                    Arguments.of(Locale.UK,"IM1 1AA"), // Isle of Man

                    // Other European countries
                    Arguments.of(new Locale("cs","CZ"),"110 00"), // Czech Republic
                    Arguments.of(new Locale("sk","SK"),"811 01"), // Slovakia
                    Arguments.of(new Locale("el","GR"),"104 31"), // Greece
                    Arguments.of(new Locale("pl","PL"),"00-001"), // Poland
                    Arguments.of(new Locale("fi","FI"),"00100"), // Finland
                    Arguments.of(new Locale("hu","HU"),"1011"),  // Hungary
                    Arguments.of(new Locale("pt","PT"),"1000-001") // Portugal
                ));
    }

    public static Stream<Arguments>
    validUkInputs()
    {
        return Stream.of(
            // London postal districts - EC (Eastern Central)
            Arguments.of(Locale.UK, "EC1A 1BB"),  // City of London
            Arguments.of(Locale.UK, "EC1M 5UD"),  // Clerkenwell
            Arguments.of(Locale.UK, "EC1V 9NR"),  // Finsbury
            Arguments.of(Locale.UK, "EC2A 1AH"),  // Bishopsgate
            Arguments.of(Locale.UK, "EC3N 4AB"),  // Aldgate
            Arguments.of(Locale.UK, "EC4A 2AB"),  // Fleet Street

            // London postal districts - WC (Western Central)
            Arguments.of(Locale.UK, "WC1A 1AA"),  // British Museum
            Arguments.of(Locale.UK, "WC1E 7HU"),  // University College London
            Arguments.of(Locale.UK, "WC1H 9JR"),  // St Pancras
            Arguments.of(Locale.UK, "WC2B 6NH"),  // Covent Garden
            Arguments.of(Locale.UK, "WC2H 9JQ"),  // Leicester Square
            Arguments.of(Locale.UK, "WC2R 0RL"),  // Strand

            // London postal districts - SW (South Western)
            Arguments.of(Locale.UK, "SW1A 0AA"),  // Westminster (Houses of Parliament)
            Arguments.of(Locale.UK, "SW1E 6JP"),  // Victoria
            Arguments.of(Locale.UK, "SW1H 9EU"),  // Westminster
            Arguments.of(Locale.UK, "SW1P 3EU"),  // Westminster
            Arguments.of(Locale.UK, "SW1V 1RB"),  // Pimlico
            Arguments.of(Locale.UK, "SW1W 0NY"),  // Belgravia
            Arguments.of(Locale.UK, "SW1X 7XL"),  // Knightsbridge
            Arguments.of(Locale.UK, "SW1Y 5AH"),  // St James's

            // London postal districts - W (Western)
            Arguments.of(Locale.UK, "W1A 6US"),   // BBC Broadcasting House
            Arguments.of(Locale.UK, "W1B 5TT"),   // Great Portland Street
            Arguments.of(Locale.UK, "W1C 1DE"),   // Oxford Street
            Arguments.of(Locale.UK, "W1D 3QU"),   // Soho
            Arguments.of(Locale.UK, "W1F 0DP"),   // Soho
            Arguments.of(Locale.UK, "W1G 9QD"),   // Harley Street
            Arguments.of(Locale.UK, "W1H 5TL"),   // Marylebone
            Arguments.of(Locale.UK, "W1J 7NB"),   // Mayfair
            Arguments.of(Locale.UK, "W1K 6TL"),   // Mayfair
            Arguments.of(Locale.UK, "W1S 4PZ"),   // Mayfair
            Arguments.of(Locale.UK, "W1T 4JD"),   // Fitzrovia
            Arguments.of(Locale.UK, "W1U 6BH"),   // Marylebone
            Arguments.of(Locale.UK, "W1W 7JE"),   // Fitzrovia

            // Other London districts
            Arguments.of(Locale.UK, "E1 6AN"),    // Tower Hamlets
            Arguments.of(Locale.UK, "E14 5AB"),   // Canary Wharf
            Arguments.of(Locale.UK, "N1 9GU"),    // Islington
            Arguments.of(Locale.UK, "NW1 5PH"),   // Camden
            Arguments.of(Locale.UK, "SE1 7PB"),   // Southwark
            Arguments.of(Locale.UK, "SE10 9LS"),  // Greenwich

            // Single-letter area codes
            Arguments.of(Locale.UK, "B1 1HQ"),    // Birmingham
            Arguments.of(Locale.UK, "G1 1QT"),    // Glasgow
            Arguments.of(Locale.UK, "L1 8JQ"),    // Liverpool
            Arguments.of(Locale.UK, "M1 1AE"),    // Manchester
            Arguments.of(Locale.UK, "M60 1NW"),   // Manchester (two-digit district)

            // Standard two-letter area codes
            Arguments.of(Locale.UK, "AB10 1XG"),  // Aberdeen
            Arguments.of(Locale.UK, "BN1 1ZZ"),   // Brighton
            Arguments.of(Locale.UK, "CF10 1DD"),  // Cardiff
            Arguments.of(Locale.UK, "DT1 1AA"),   // Dorchester
            Arguments.of(Locale.UK, "EH1 1BB"),   // Edinburgh
            Arguments.of(Locale.UK, "FK1 1UU"),   // Falkirk
            Arguments.of(Locale.UK, "GL50 3DA"),  // Cheltenham
            Arguments.of(Locale.UK, "HA1 3AW"),   // Harrow
            Arguments.of(Locale.UK, "HD1 2EY"),   // Huddersfield
            Arguments.of(Locale.UK, "KY16 9AJ"),  // St Andrews
            Arguments.of(Locale.UK, "LS1 1UR"),   // Leeds
            Arguments.of(Locale.UK, "MK9 3EP"),   // Milton Keynes
            Arguments.of(Locale.UK, "OX1 1PT"),   // Oxford
            Arguments.of(Locale.UK, "PL1 2HB"),   // Plymouth

            // Special cases
            Arguments.of(Locale.UK, "GIR 0AA"),   // Girobank
            Arguments.of(Locale.UK, "CR0 1XR"),   // Croydon, district with 0
            Arguments.of(Locale.UK, "BN99 9AA"),  // Brighton, district with 99
            Arguments.of(Locale.UK, "BS98 1TL"),  // Bristol, special district
            Arguments.of(Locale.UK, "TS1 3BA"),   // Middlesbrough
            Arguments.of(Locale.UK, "TS13 4TT"),  // Saltburn (different format)

            // With and without spaces
            Arguments.of(Locale.UK, "WC1H 9HQ"),  // With space
            Arguments.of(Locale.UK, "WC1H9HQ"),   // Without space
            Arguments.of(Locale.UK, "BH23 6AA"),  // With space
            Arguments.of(Locale.UK, "BH236AA"),   // Without space

            // British Overseas Territories
            Arguments.of(Locale.UK, "ASCN 1ZZ"),  // Ascension Island
            Arguments.of(Locale.UK, "BBND 1ZZ"),  // British Indian Ocean Territory
            Arguments.of(Locale.UK, "BIQQ 1ZZ"),  // British Antarctic Territory
            Arguments.of(Locale.UK, "FIQQ 1ZZ"),  // Falkland Islands
            Arguments.of(Locale.UK, "PCRN 1ZZ"),  // Pitcairn Islands
            Arguments.of(Locale.UK, "SIQQ 1ZZ"),  // South Georgia
            Arguments.of(Locale.UK, "STHL 1ZZ"),  // Saint Helena
            Arguments.of(Locale.UK, "TDCU 1ZZ"),  // Tristan da Cunha

            // BFPO (British Forces Post Office)
            Arguments.of(Locale.UK, "BFPO 1"),    // Basic BFPO address
            Arguments.of(Locale.UK, "BFPO 43"),   // BFPO Gibraltar
            Arguments.of(Locale.UK, "BFPO 56"),   // BFPO Naples
            Arguments.of(Locale.UK, "BFPO 567"),  // BFPO Operation Shader
            Arguments.of(Locale.UK, "BFPO 1234"), // BFPO with higher number

            // Crown Dependencies
            Arguments.of(Locale.UK, "JE1 1AA"),   // Jersey
            Arguments.of(Locale.UK, "JE2 3NN"),   // Jersey
            Arguments.of(Locale.UK, "GY1 1AF"),   // Guernsey
            Arguments.of(Locale.UK, "GY9 3UH"),   // Guernsey (Alderney)
            Arguments.of(Locale.UK, "IM1 1AE"),   // Isle of Man
            Arguments.of(Locale.UK, "IM99 1PS"),  // Isle of Man special code

            // Non-geographic postcodes
            Arguments.of(Locale.UK, "BX1 1LT"),   // Barclays Bank
            Arguments.of(Locale.UK, "BX5 5AT"),   // VAT return

            // Special districts
            Arguments.of(Locale.UK, "PR1 9LY"),   // Preston normal
            Arguments.of(Locale.UK, "PR3 0SG"),   // Preston rural
            Arguments.of(Locale.UK, "ZE1 0QP"),   // Lerwick (Shetland)
            Arguments.of(Locale.UK, "ZE2 9JH"),   // Shetland rural
            Arguments.of(Locale.UK, "ZE3 9JZ"),   // Shetland remote

            // Various comprehensive examples
            Arguments.of(Locale.UK, "IV21 2LR"),  // Highlands
            Arguments.of(Locale.UK, "AB41 7LW"),  // Aberdeenshire
            Arguments.of(Locale.UK, "PA28 6RE"),  // Argyll
            Arguments.of(Locale.UK, "SA71 5DQ"),  // Pembrokeshire
            Arguments.of(Locale.UK, "CO4 5XY"),   // Colchester
            Arguments.of(Locale.UK, "B42 2SU"),   // Birmingham
            Arguments.of(Locale.UK, "S8 8BG"),    // Sheffield
            Arguments.of(Locale.UK, "NG17 4JY"),  // Nottinghamshire
            Arguments.of(Locale.UK, "DE12 6RE"),  // Derbyshire
            Arguments.of(Locale.UK, "CB3 0DS"),   // Cambridge
            Arguments.of(Locale.UK, "G42 0BG"),   // Glasgow
            Arguments.of(Locale.UK, "NE99 1BB")   // Newcastle special code
        );
    }

    public static Stream<Arguments>
    validUkxInputs()
    {
        return Stream.of(
            // London postal districts (single-digit)
            Arguments.of(Locale.UK, "EC1A 9AA"),   // City of London
            Arguments.of(Locale.UK, "WC1X 9AA"),   // Westminster/Camden
            Arguments.of(Locale.UK, "SW1 9AA"),   // Westminster
            Arguments.of(Locale.UK, "W1 9AA"),    // Westminster/Camden
            Arguments.of(Locale.UK, "NW1 9AA"),   // Camden
            Arguments.of(Locale.UK, "N1 9AA"),    // Islington
            Arguments.of(Locale.UK, "NE1 9AA"),   // Newcastle (not London)
            Arguments.of(Locale.UK, "E1 9AA"),    // Tower Hamlets
            Arguments.of(Locale.UK, "SE1 9AA"),   // Southwark
            Arguments.of(Locale.UK, "S1 9AA"),    // Sheffield (not London)

            // London postal districts (with letters)
            Arguments.of(Locale.UK, "EC1A 9AA"),  // City of London
            Arguments.of(Locale.UK, "EC2M 7PB"),  // City of London
            Arguments.of(Locale.UK, "EC3V 0DN"),  // City of London
            Arguments.of(Locale.UK, "EC4A 2AB"),  // City of London
            Arguments.of(Locale.UK, "WC1A 1AA"),  // Camden
            Arguments.of(Locale.UK, "WC2H 9JQ"),  // Westminster
            Arguments.of(Locale.UK, "SW1A 0AA"),  // Westminster (Houses of Parliament)
            Arguments.of(Locale.UK, "SW1E 6JP"),  // Westminster
            Arguments.of(Locale.UK, "SW1H 9EU"),  // Westminster
            Arguments.of(Locale.UK, "SW1P 3EU"),  // Westminster
            Arguments.of(Locale.UK, "SW1V 1RB"),  // Westminster
            Arguments.of(Locale.UK, "SW1W 0NY"),  // Westminster
            Arguments.of(Locale.UK, "SW1X 7XL"),  // Westminster/Kensington & Chelsea
            Arguments.of(Locale.UK, "SW1Y 5AH"),  // Westminster
            Arguments.of(Locale.UK, "W1A 6US"),   // Westminster (BBC Broadcasting House)
            Arguments.of(Locale.UK, "W1B 5TT"),   // Westminster
            Arguments.of(Locale.UK, "W1C 1DE"),   // Westminster
            Arguments.of(Locale.UK, "W1D 3QU"),   // Westminster
            Arguments.of(Locale.UK, "W1F 0DP"),   // Westminster
            Arguments.of(Locale.UK, "W1G 9QD"),   // Westminster
            Arguments.of(Locale.UK, "W1H 5TL"),   // Westminster

            // Single-letter area codes
            Arguments.of(Locale.UK, "B1 1HQ"),    // Birmingham
            Arguments.of(Locale.UK, "G1 1QT"),    // Glasgow
            Arguments.of(Locale.UK, "L1 8JQ"),    // Liverpool
            Arguments.of(Locale.UK, "M1 1AE"),    // Manchester
            Arguments.of(Locale.UK, "M60 1NW"),   // Manchester (two-digit district)
            Arguments.of(Locale.UK, "V1A 1A1"),   // Invalid - V not used in UK

            // Standard two-letter area codes
            Arguments.of(Locale.UK, "AB10 1XG"),  // Aberdeen
            Arguments.of(Locale.UK, "BN1 1ZZ"),   // Brighton
            Arguments.of(Locale.UK, "CF10 1DD"),  // Cardiff
            Arguments.of(Locale.UK, "DT1 1AA"),   // Dorchester
            Arguments.of(Locale.UK, "EH1 1BB"),   // Edinburgh
            Arguments.of(Locale.UK, "FK1 1UU"),   // Falkirk
            Arguments.of(Locale.UK, "GL50 3DA"),  // Cheltenham
            Arguments.of(Locale.UK, "HA1 3AW"),   // Harrow
            Arguments.of(Locale.UK, "HD1 2EY"),   // Huddersfield
            Arguments.of(Locale.UK, "KY16 9AJ"),  // St Andrews
            Arguments.of(Locale.UK, "LS1 1UR"),   // Leeds
            Arguments.of(Locale.UK, "MK9 3EP"),   // Milton Keynes
            Arguments.of(Locale.UK, "OX1 1PT"),   // Oxford
            Arguments.of(Locale.UK, "PL1 2HB"),   // Plymouth

            // Special cases
            Arguments.of(Locale.UK, "GIR 0AA"),   // Girobank
            Arguments.of(Locale.UK, "CR0 1XR"),   // Croydon, district with 0
            Arguments.of(Locale.UK, "BN99 9AA"),  // Brighton, district with 99
            Arguments.of(Locale.UK, "BS98 1TL"),  // Bristol, special district
            Arguments.of(Locale.UK, "TS1 3BA"),   // Middlesbrough
            Arguments.of(Locale.UK, "TS13 4TT"),  // Saltburn (different format)

            // With and without spaces
            Arguments.of(Locale.UK, "WC1H 9HQ"),  // With space
            Arguments.of(Locale.UK, "WC1H9HQ"),   // Without space
            Arguments.of(Locale.UK, "BH23 6AA"),  // With space
            Arguments.of(Locale.UK, "BH236AA"),   // Without space

            // British Overseas Territories
            Arguments.of(Locale.UK, "ASCN 1ZZ"),  // Ascension Island
            Arguments.of(Locale.UK, "BBND 1ZZ"),  // British Indian Ocean Territory
            Arguments.of(Locale.UK, "BIQQ 1ZZ"),  // British Antarctic Territory
            Arguments.of(Locale.UK, "FIQQ 1ZZ"),  // Falkland Islands
            Arguments.of(Locale.UK, "PCRN 1ZZ"),  // Pitcairn Islands
            Arguments.of(Locale.UK, "SIQQ 1ZZ"),  // South Georgia
            Arguments.of(Locale.UK, "STHL 1ZZ"),  // Saint Helena
            Arguments.of(Locale.UK, "TDCU 1ZZ"),  // Tristan da Cunha

            // BFPO (British Forces Post Office)
            Arguments.of(Locale.UK, "BFPO 1"),    // Basic BFPO address
            Arguments.of(Locale.UK, "BFPO 43"),   // BFPO Gibraltar
            Arguments.of(Locale.UK, "BFPO 56"),   // BFPO Naples
            Arguments.of(Locale.UK, "BFPO 567"),  // BFPO Operation Shader
            Arguments.of(Locale.UK, "BFPO 1234"), // BFPO with higher number

            // Crown Dependencies
            Arguments.of(Locale.UK, "JE1 1AA"),   // Jersey
            Arguments.of(Locale.UK, "JE2 3NN"),   // Jersey
            Arguments.of(Locale.UK, "GY1 1AF"),   // Guernsey
            Arguments.of(Locale.UK, "GY9 3UH"),   // Guernsey (Alderney)
            Arguments.of(Locale.UK, "IM1 1AE"),   // Isle of Man
            Arguments.of(Locale.UK, "IM99 1PS"),  // Isle of Man special code

            // Non-geographic postcodes
            Arguments.of(Locale.UK, "BX1 1LT"),   // Barclays Bank
            Arguments.of(Locale.UK, "BX5 5AT"),   // VAT return
            Arguments.of(Locale.UK, "DX1 1HU"),   // Legal document exchange

            // Special districts
            Arguments.of(Locale.UK, "PR1 9LY"),   // Preston normal
            Arguments.of(Locale.UK, "PR3 0SG"),   // Preston rural
            Arguments.of(Locale.UK, "ZE1 0QP"),   // Lerwick (Shetland)
            Arguments.of(Locale.UK, "ZE2 9JH"),   // Shetland rural
            Arguments.of(Locale.UK, "ZE3 9JZ"),   // Shetland remote

            // Various comprehensive examples
            Arguments.of(Locale.UK, "IV21 2LR"),  // Highlands
            Arguments.of(Locale.UK, "AB41 7LW"),  // Aberdeenshire
            Arguments.of(Locale.UK, "PA28 6RE"),  // Argyll
            Arguments.of(Locale.UK, "SA71 5DQ"),  // Pembrokeshire
            Arguments.of(Locale.UK, "CO4 5XY"),   // Colchester
            Arguments.of(Locale.UK, "B42 2SU"),   // Birmingham
            Arguments.of(Locale.UK, "S8 8BG"),    // Sheffield
            Arguments.of(Locale.UK, "NG17 4JY"),  // Nottinghamshire
            Arguments.of(Locale.UK, "DE12 6RE"),  // Derbyshire
            Arguments.of(Locale.UK, "CB3 0DS"),   // Cambridge

            // Additional edge cases
            Arguments.of(Locale.UK, "M1 1AA"),    // Manchester shortest
            Arguments.of(Locale.UK, "G42 0BG"),   // Glasgow
            Arguments.of(Locale.UK, "NE99 1BB")   // Newcastle special code
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
