/// ///////////////////////////////////////////////////////////////////////////
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
        PostalCode expected = PostalCode.of(input);
        PostalCode actual =
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
            PostalCode actual = PostalCode.of(input);
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
                validOtherInputs(),
                Stream.concat(
                    validUkInputs(),
                    validCanadianInputs()));
    }


    public static Stream<Arguments>
    validOtherInputs()
    {
        return
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
            );
    }

    public static Stream<Arguments>
    validUkInputs()
    {
        return Stream.of(
            // London postal districts - EC (Eastern Central)
            Arguments.of(Locale.UK,"EC1A 1BB"),  // City of London
            Arguments.of(Locale.UK,"EC1M 5UD"),  // Clerkenwell
            Arguments.of(Locale.UK,"EC1V 9NR"),  // Finsbury
            Arguments.of(Locale.UK,"EC2A 1AH"),  // Bishopsgate
            Arguments.of(Locale.UK,"EC3N 4AB"),  // Aldgate
            Arguments.of(Locale.UK,"EC4A 2AB"),  // Fleet Street

            // London postal districts - WC (Western Central)
            Arguments.of(Locale.UK,"WC1A 1AA"),  // British Museum
            Arguments.of(Locale.UK,"WC1E 7HU"),  // University College London
            Arguments.of(Locale.UK,"WC1H 9JR"),  // St Pancras
            Arguments.of(Locale.UK,"WC2B 6NH"),  // Covent Garden
            Arguments.of(Locale.UK,"WC2H 9JQ"),  // Leicester Square
            Arguments.of(Locale.UK,"WC2R 0RL"),  // Strand

            // London postal districts - SW (South Western)
            Arguments.of(Locale.UK,"SW1A 0AA"),  // Westminster (Houses of Parliament)
            Arguments.of(Locale.UK,"SW1E 6JP"),  // Victoria
            Arguments.of(Locale.UK,"SW1H 9EU"),  // Westminster
            Arguments.of(Locale.UK,"SW1P 3EU"),  // Westminster
            Arguments.of(Locale.UK,"SW1V 1RB"),  // Pimlico
            Arguments.of(Locale.UK,"SW1W 0NY"),  // Belgravia
            Arguments.of(Locale.UK,"SW1X 7XL"),  // Knightsbridge
            Arguments.of(Locale.UK,"SW1Y 5AH"),  // St James's

            // London postal districts - W (Western)
            Arguments.of(Locale.UK,"W1A 6US"),   // BBC Broadcasting House
            Arguments.of(Locale.UK,"W1B 5TT"),   // Great Portland Street
            Arguments.of(Locale.UK,"W1C 1DE"),   // Oxford Street
            Arguments.of(Locale.UK,"W1D 3QU"),   // Soho
            Arguments.of(Locale.UK,"W1F 0DP"),   // Soho
            Arguments.of(Locale.UK,"W1G 9QD"),   // Harley Street
            Arguments.of(Locale.UK,"W1H 5TL"),   // Marylebone
            Arguments.of(Locale.UK,"W1J 7NB"),   // Mayfair
            Arguments.of(Locale.UK,"W1K 6TL"),   // Mayfair
            Arguments.of(Locale.UK,"W1S 4PZ"),   // Mayfair
            Arguments.of(Locale.UK,"W1T 4JD"),   // Fitzrovia
            Arguments.of(Locale.UK,"W1U 6BH"),   // Marylebone
            Arguments.of(Locale.UK,"W1W 7JE"),   // Fitzrovia

            // Other London districts
            Arguments.of(Locale.UK,"E1 6AN"),    // Tower Hamlets
            Arguments.of(Locale.UK,"E14 5AB"),   // Canary Wharf
            Arguments.of(Locale.UK,"N1 9GU"),    // Islington
            Arguments.of(Locale.UK,"NW1 5PH"),   // Camden
            Arguments.of(Locale.UK,"SE1 7PB"),   // Southwark
            Arguments.of(Locale.UK,"SE10 9LS"),  // Greenwich

            // Single-letter area codes
            Arguments.of(Locale.UK,"B1 1HQ"),    // Birmingham
            Arguments.of(Locale.UK,"G1 1QT"),    // Glasgow
            Arguments.of(Locale.UK,"L1 8JQ"),    // Liverpool
            Arguments.of(Locale.UK,"M1 1AE"),    // Manchester
            Arguments.of(Locale.UK,"M60 1NW"),   // Manchester (two-digit district)

            // Standard two-letter area codes
            Arguments.of(Locale.UK,"AB10 1XG"),  // Aberdeen
            Arguments.of(Locale.UK,"BN1 1ZZ"),   // Brighton
            Arguments.of(Locale.UK,"CF10 1DD"),  // Cardiff
            Arguments.of(Locale.UK,"DT1 1AA"),   // Dorchester
            Arguments.of(Locale.UK,"EH1 1BB"),   // Edinburgh
            Arguments.of(Locale.UK,"FK1 1UU"),   // Falkirk
            Arguments.of(Locale.UK,"GL50 3DA"),  // Cheltenham
            Arguments.of(Locale.UK,"HA1 3AW"),   // Harrow
            Arguments.of(Locale.UK,"HD1 2EY"),   // Huddersfield
            Arguments.of(Locale.UK,"KY16 9AJ"),  // St Andrews
            Arguments.of(Locale.UK,"LS1 1UR"),   // Leeds
            Arguments.of(Locale.UK,"MK9 3EP"),   // Milton Keynes
            Arguments.of(Locale.UK,"OX1 1PT"),   // Oxford
            Arguments.of(Locale.UK,"PL1 2HB"),   // Plymouth

            // Special cases
            Arguments.of(Locale.UK,"GIR 0AA"),   // Girobank
            Arguments.of(Locale.UK,"CR0 1XR"),   // Croydon, district with 0
            Arguments.of(Locale.UK,"BN99 9AA"),  // Brighton, district with 99
            Arguments.of(Locale.UK,"BS98 1TL"),  // Bristol, special district
            Arguments.of(Locale.UK,"TS1 3BA"),   // Middlesbrough
            Arguments.of(Locale.UK,"TS13 4TT"),  // Saltburn (different format)

            // With and without spaces
            Arguments.of(Locale.UK,"WC1H 9HQ"),  // With space
            Arguments.of(Locale.UK,"WC1H9HQ"),   // Without space
            Arguments.of(Locale.UK,"BH23 6AA"),  // With space
            Arguments.of(Locale.UK,"BH236AA"),   // Without space

            // British Overseas Territories
            Arguments.of(Locale.UK,"ASCN 1ZZ"),  // Ascension Island
            Arguments.of(Locale.UK,"BBND 1ZZ"),  // British Indian Ocean Territory
            Arguments.of(Locale.UK,"BIQQ 1ZZ"),  // British Antarctic Territory
            Arguments.of(Locale.UK,"FIQQ 1ZZ"),  // Falkland Islands
            Arguments.of(Locale.UK,"PCRN 1ZZ"),  // Pitcairn Islands
            Arguments.of(Locale.UK,"SIQQ 1ZZ"),  // South Georgia
            Arguments.of(Locale.UK,"STHL 1ZZ"),  // Saint Helena
            Arguments.of(Locale.UK,"TDCU 1ZZ"),  // Tristan da Cunha

            // BFPO (British Forces Post Office)
            Arguments.of(Locale.UK,"BFPO 1"),    // Basic BFPO address
            Arguments.of(Locale.UK,"BFPO 43"),   // BFPO Gibraltar
            Arguments.of(Locale.UK,"BFPO 56"),   // BFPO Naples
            Arguments.of(Locale.UK,"BFPO 567"),  // BFPO Operation Shader
            Arguments.of(Locale.UK,"BFPO 1234"), // BFPO with higher number

            // Crown Dependencies
            Arguments.of(Locale.UK,"JE1 1AA"),   // Jersey
            Arguments.of(Locale.UK,"JE2 3NN"),   // Jersey
            Arguments.of(Locale.UK,"GY1 1AF"),   // Guernsey
            Arguments.of(Locale.UK,"GY9 3UH"),   // Guernsey (Alderney)
            Arguments.of(Locale.UK,"IM1 1AE"),   // Isle of Man
            Arguments.of(Locale.UK,"IM99 1PS"),  // Isle of Man special code

            // Non-geographic postcodes
            Arguments.of(Locale.UK,"BX1 1LT"),   // Barclays Bank
            Arguments.of(Locale.UK,"BX5 5AT"),   // VAT return

            // Special districts
            Arguments.of(Locale.UK,"PR1 9LY"),   // Preston normal
            Arguments.of(Locale.UK,"PR3 0SG"),   // Preston rural
            Arguments.of(Locale.UK,"ZE1 0QP"),   // Lerwick (Shetland)
            Arguments.of(Locale.UK,"ZE2 9JH"),   // Shetland rural
            Arguments.of(Locale.UK,"ZE3 9JZ"),   // Shetland remote

            // Various comprehensive examples
            Arguments.of(Locale.UK,"IV21 2LR"),  // Highlands
            Arguments.of(Locale.UK,"AB41 7LW"),  // Aberdeenshire
            Arguments.of(Locale.UK,"PA28 6RE"),  // Argyll
            Arguments.of(Locale.UK,"SA71 5DQ"),  // Pembrokeshire
            Arguments.of(Locale.UK,"CO4 5XY"),   // Colchester
            Arguments.of(Locale.UK,"B42 2SU"),   // Birmingham
            Arguments.of(Locale.UK,"S8 8BG"),    // Sheffield
            Arguments.of(Locale.UK,"NG17 4JY"),  // Nottinghamshire
            Arguments.of(Locale.UK,"DE12 6RE"),  // Derbyshire
            Arguments.of(Locale.UK,"CB3 0DS"),   // Cambridge
            Arguments.of(Locale.UK,"G42 0BG"),   // Glasgow
            Arguments.of(Locale.UK,"NE99 1BB")   // Newcastle special code
        );
    }

    public static Stream<Arguments>
    validCanadianInputs()
    {
        return Stream.of(
            // Ontario (K, L, M, N, P)
            Arguments.of(Locale.CANADA,"K0A 1A0"),  // Rural Eastern Ontario
            Arguments.of(Locale.CANADA,"K0B 1J0"),  // Rural Eastern Ontario
            Arguments.of(Locale.CANADA,"K0C 1A0"),  // Rural Eastern Ontario
            Arguments.of(Locale.CANADA,"K1A 0A1"),  // Federal Government
            Arguments.of(Locale.CANADA,"K1A 0B1"),  // Ottawa - Parliament Hill
            Arguments.of(Locale.CANADA,"K1A0B1"),   // Without space
            Arguments.of(Locale.CANADA,"K1G 5K7"),  // Ottawa - Alta Vista
            Arguments.of(Locale.CANADA,"K1H 7X3"),  // Ottawa - Old Ottawa South
            Arguments.of(Locale.CANADA,"K1M 1M4"),  // Ottawa - New Edinburgh
            Arguments.of(Locale.CANADA,"K1P 5H9"),  // Ottawa - Downtown Core
            Arguments.of(Locale.CANADA,"K1S 5R5"),  // Ottawa - Glebe
            Arguments.of(Locale.CANADA,"K1V 8R5"),  // Ottawa - South Keys
            Arguments.of(Locale.CANADA,"K1Z 5T7"),  // Ottawa - Westboro
            Arguments.of(Locale.CANADA,"K2P 0X8"),  // Ottawa - Centretown
            Arguments.of(Locale.CANADA,"K2R 1C6"),  // Ottawa - Nepean
            Arguments.of(Locale.CANADA,"K4A 0Z5"),  // Ottawa - Orleans
            Arguments.of(Locale.CANADA,"K7L 4V1"),  // Kingston
            Arguments.of(Locale.CANADA,"K7P 2N6"),  // Kingston - West
            Arguments.of(Locale.CANADA,"K9A 1K7"),  // Cobourg

            // Toronto and GTA (L, M)
            Arguments.of(Locale.CANADA,"L0A 1A0"),  // Rural GTA/Durham
            Arguments.of(Locale.CANADA,"L1H 7K4"),  // Oshawa
            Arguments.of(Locale.CANADA,"L3R 0L4"),  // Markham
            Arguments.of(Locale.CANADA,"L4K 4V4"),  // Concord/Vaughan
            Arguments.of(Locale.CANADA,"L5B 3C2"),  // Mississauga
            Arguments.of(Locale.CANADA,"L6H 3P1"),  // Oakville
            Arguments.of(Locale.CANADA,"L7A 0W6"),  // Brampton
            Arguments.of(Locale.CANADA,"L8P 1B2"),  // Hamilton
            Arguments.of(Locale.CANADA,"L9G 1N8"),  // Dundas
            Arguments.of(Locale.CANADA,"L9T 7Y8"),  // Milton
            Arguments.of(Locale.CANADA,"M1B 5K5"),  // Scarborough - Malvern
            Arguments.of(Locale.CANADA,"M1C 4R5"),  // Scarborough - Highland Creek
            Arguments.of(Locale.CANADA,"M2N 6S4"),  // North York
            Arguments.of(Locale.CANADA,"M3C 0C1"),  // Don Mills
            Arguments.of(Locale.CANADA,"M4B 1B3"),  // East York
            Arguments.of(Locale.CANADA,"M4T 1M5"),  // Toronto - Rosedale
            Arguments.of(Locale.CANADA,"M5A 2A7"),  // Toronto - Regent Park
            Arguments.of(Locale.CANADA,"M5G 2C2"),  // Toronto - Hospital District
            Arguments.of(Locale.CANADA,"M5J 2N8"),  // Toronto - Downtown/Financial District
            Arguments.of(Locale.CANADA,"M5S 1A1"),  // Toronto - University of Toronto
            Arguments.of(Locale.CANADA,"M5T 2Z2"),  // Toronto - Kensington Market
            Arguments.of(Locale.CANADA,"M5V 2H1"),  // Toronto - CN Tower
            Arguments.of(Locale.CANADA,"M6G 3H2"),  // Toronto - Christie Pits
            Arguments.of(Locale.CANADA,"M6J 2X4"),  // Toronto - West Queen West
            Arguments.of(Locale.CANADA,"M6K 3C3"),  // Toronto - Liberty Village
            Arguments.of(Locale.CANADA,"M9A 4X6"),  // Etobicoke
            Arguments.of(Locale.CANADA,"M9W 7G8"),  // Etobicoke - Rexdale

            // Southwestern Ontario (N)
            Arguments.of(Locale.CANADA,"N0A 1K0"),  // Rural Southwestern Ontario
            Arguments.of(Locale.CANADA,"N1G 2W1"),  // Guelph
            Arguments.of(Locale.CANADA,"N2L 3G1"),  // Waterloo
            Arguments.of(Locale.CANADA,"N3A 2K7"),  // Cambridge
            Arguments.of(Locale.CANADA,"N4S 3M2"),  // Brantford
            Arguments.of(Locale.CANADA,"N5W 5C1"),  // London
            Arguments.of(Locale.CANADA,"N6A 3K7"),  // London - Downtown
            Arguments.of(Locale.CANADA,"N7T 7H9"),  // Sarnia
            Arguments.of(Locale.CANADA,"N8A 4K9"),  // Leamington
            Arguments.of(Locale.CANADA,"N9A 6W1"),  // Windsor
            Arguments.of(Locale.CANADA,"N9G 1W5"),  // Windsor - South Windsor

            // Northern Ontario (P)
            Arguments.of(Locale.CANADA,"P0A 1K0"),  // Algonquin Park area
            Arguments.of(Locale.CANADA,"P0B 1J0"),  // Rural Northeastern Ontario
            Arguments.of(Locale.CANADA,"P0M 2J0"),  // Rural Northwest Ontario
            Arguments.of(Locale.CANADA,"P1A 3S6"),  // North Bay
            Arguments.of(Locale.CANADA,"P2B 8J6"),  // Sturgeon Falls
            Arguments.of(Locale.CANADA,"P3A 1T7"),  // Sudbury
            Arguments.of(Locale.CANADA,"P3E 5P9"),  // Sudbury - South End
            Arguments.of(Locale.CANADA,"P3Y 1N3"),  // Espanola
            Arguments.of(Locale.CANADA,"P4N 4C5"),  // Timmins
            Arguments.of(Locale.CANADA,"P5A 2P2"),  // Elliot Lake
            Arguments.of(Locale.CANADA,"P6A 5N9"),  // Sault Ste. Marie
            Arguments.of(Locale.CANADA,"P7A 4T7"),  // Thunder Bay - North
            Arguments.of(Locale.CANADA,"P7B 6T8"),  // Thunder Bay - South
            Arguments.of(Locale.CANADA,"P9N 3W8"),  // Dryden

            // Quebec (G, H, J)
            // Eastern Quebec (G)
            Arguments.of(Locale.CANADA,"G0A 1B0"),  // Rural Eastern Quebec
            Arguments.of(Locale.CANADA,"G0C 1Z0"),  // Gaspé Peninsula
            Arguments.of(Locale.CANADA,"G0G 2W0"),  // Magdalen Islands
            Arguments.of(Locale.CANADA,"G0S 1E0"),  // Rural Chaudière-Appalaches
            Arguments.of(Locale.CANADA,"G1C 7B7"),  // Quebec City - Beauport
            Arguments.of(Locale.CANADA,"G1K 3C8"),  // Quebec City - Saint-Roch
            Arguments.of(Locale.CANADA,"G1R 4P3"),  // Quebec City - Old Quebec
            Arguments.of(Locale.CANADA,"G1V 2M2"),  // Quebec City - Sainte-Foy
            Arguments.of(Locale.CANADA,"G2B 1C2"),  // Quebec City - Charlesbourg
            Arguments.of(Locale.CANADA,"G4R 4K6"),  // Sept-Îles
            Arguments.of(Locale.CANADA,"G5A 1G1"),  // Rivière-du-Loup
            Arguments.of(Locale.CANADA,"G6J 1H7"),  // Lévis
            Arguments.of(Locale.CANADA,"G7H 5A8"),  // Jonquière
            Arguments.of(Locale.CANADA,"G8B 5W1"),  // Saguenay - Chicoutimi
            Arguments.of(Locale.CANADA,"G9A 5J9"),  // Trois-Rivières

            // Montreal area (H)
            Arguments.of(Locale.CANADA,"H0H 0H0"),  // Santa Claus (special code)
            Arguments.of(Locale.CANADA,"H1A 5A7"),  // Montreal - Pointe-aux-Trembles
            Arguments.of(Locale.CANADA,"H1G 3L6"),  // Montreal - Montreal-Nord
            Arguments.of(Locale.CANADA,"H1T 1R4"),  // Montreal - Rosemont
            Arguments.of(Locale.CANADA,"H2E 1S2"),  // Montreal - Villeray
            Arguments.of(Locale.CANADA,"H2K 4M2"),  // Montreal - Plateau Mont-Royal
            Arguments.of(Locale.CANADA,"H2S 2C7"),  // Montreal - Mile End
            Arguments.of(Locale.CANADA,"H2X 1Y4"),  // Montreal - Downtown
            Arguments.of(Locale.CANADA,"H3A 0G4"),  // Montreal - McGill University
            Arguments.of(Locale.CANADA,"H3B 3K5"),  // Montreal - Ville-Marie
            Arguments.of(Locale.CANADA,"H3C 3J7"),  // Montreal - Cité du Multimédia
            Arguments.of(Locale.CANADA,"H3G 1M8"),  // Montreal - Golden Square Mile
            Arguments.of(Locale.CANADA,"H3Z 2Y7"),  // Montreal - Westmount
            Arguments.of(Locale.CANADA,"H4B 1R6"),  // Montreal - Notre-Dame-de-Grâce
            Arguments.of(Locale.CANADA,"H4M 2J4"),  // Montreal - Saint-Laurent
            Arguments.of(Locale.CANADA,"H7N 3W8"),  // Laval
            Arguments.of(Locale.CANADA,"H8N 1Y6"),  // Brossard
            Arguments.of(Locale.CANADA,"H9B 1T7"),  // Dollard-Des Ormeaux
            Arguments.of(Locale.CANADA,"H9R 5P9"),  // Pointe-Claire

            // Western Quebec (J)
            Arguments.of(Locale.CANADA,"J0A 1A0"),  // Rural Western Quebec
            Arguments.of(Locale.CANADA,"J0E 1E0"),  // Eastern Townships - Rural
            Arguments.of(Locale.CANADA,"J0K 2R0"),  // Lanaudière - Rural
            Arguments.of(Locale.CANADA,"J0T 1C0"),  // Laurentians - Rural
            Arguments.of(Locale.CANADA,"J0Z 3B0"),  // Outaouais - Rural
            Arguments.of(Locale.CANADA,"J1S 1K1"),  // Sherbrooke
            Arguments.of(Locale.CANADA,"J3B 6J9"),  // Saint-Jean-sur-Richelieu
            Arguments.of(Locale.CANADA,"J3X 1B3"),  // Saint-Jean-sur-Richelieu - Rural
            Arguments.of(Locale.CANADA,"J4K 4Z7"),  // Longueuil
            Arguments.of(Locale.CANADA,"J6A 1K2"),  // Repentigny
            Arguments.of(Locale.CANADA,"J8L 2W6"),  // Mont-Laurier
            Arguments.of(Locale.CANADA,"J8X 3H8"),  // Gatineau - Hull
            Arguments.of(Locale.CANADA,"J9A 1K7"),  // Gatineau - Aylmer
            Arguments.of(Locale.CANADA,"J9E 2B5"),  // Gatineau - Buckingham

            // Nova Scotia (B)
            Arguments.of(Locale.CANADA,"B0C 1B0"),  // Cape Breton - Rural
            Arguments.of(Locale.CANADA,"B0E 3B0"),  // Northern Nova Scotia - Rural
            Arguments.of(Locale.CANADA,"B0J 1J0"),  // Shelburne County
            Arguments.of(Locale.CANADA,"B0K 1C0"),  // Annapolis Valley - Rural
            Arguments.of(Locale.CANADA,"B0P 1P0"),  // South Shore - Rural
            Arguments.of(Locale.CANADA,"B1A 4P1"),  // Sydney
            Arguments.of(Locale.CANADA,"B1P 6K7"),  // Sydney - Cape Breton Regional Municipality
            Arguments.of(Locale.CANADA,"B2A 4N9"),  // Glace Bay
            Arguments.of(Locale.CANADA,"B2G 2L1"),  // New Glasgow
            Arguments.of(Locale.CANADA,"B2N 3K2"),  // Truro
            Arguments.of(Locale.CANADA,"B3A 4P7"),  // Bedford
            Arguments.of(Locale.CANADA,"B3H 4R2"),  // Halifax - Downtown/South End
            Arguments.of(Locale.CANADA,"B3K 5M8"),  // Halifax - North End
            Arguments.of(Locale.CANADA,"B3L 4T6"),  // Halifax - West End
            Arguments.of(Locale.CANADA,"B3N 2P1"),  // Halifax - Clayton Park
            Arguments.of(Locale.CANADA,"B3S 1B5"),  // Halifax - Bayers Lake
            Arguments.of(Locale.CANADA,"B3Z 4G7"),  // Peggy's Cove
            Arguments.of(Locale.CANADA,"B4A 1T7"),  // Dartmouth
            Arguments.of(Locale.CANADA,"B4E 3K6"),  // Cole Harbour
            Arguments.of(Locale.CANADA,"B4V 2M9"),  // Bridgewater

            // New Brunswick (E)
            Arguments.of(Locale.CANADA,"E1A 7Y4"),  // Moncton
            Arguments.of(Locale.CANADA,"E1C 8Z5"),  // Dieppe
            Arguments.of(Locale.CANADA,"E1G 1Y6"),  // Riverview
            Arguments.of(Locale.CANADA,"E1N 4R8"),  // Sackville
            Arguments.of(Locale.CANADA,"E2A 4Z7"),  // Bathurst
            Arguments.of(Locale.CANADA,"E2E 4Z6"),  // Miramichi
            Arguments.of(Locale.CANADA,"E2L 4S8"),  // Saint John
            Arguments.of(Locale.CANADA,"E3A 9C2"),  // Fredericton - North Side
            Arguments.of(Locale.CANADA,"E3B 1A3"),  // Fredericton - Downtown
            Arguments.of(Locale.CANADA,"E3L 5K4"),  // Woodstock
            Arguments.of(Locale.CANADA,"E4S 3E4"),  // Woodstock - Rural
            Arguments.of(Locale.CANADA,"E5A 1A1"),  // Grand Falls
            Arguments.of(Locale.CANADA,"E7B 1G5"),  // Caraquet
            Arguments.of(Locale.CANADA,"E8A 1N5"),  // Campbellton
            Arguments.of(Locale.CANADA,"E9C 2H5"),  // Edmundston

            // Manitoba (R)
            Arguments.of(Locale.CANADA,"R0A 0A1"),  // Southern Manitoba - Rural
            Arguments.of(Locale.CANADA,"R0B 0E0"),  // Northern Manitoba - Rural
            Arguments.of(Locale.CANADA,"R0E 1J0"),  // Eastern Manitoba - Rural
            Arguments.of(Locale.CANADA,"R0G 0T0"),  // Western Manitoba - Rural
            Arguments.of(Locale.CANADA,"R1A 1A1"),  // Selkirk
            Arguments.of(Locale.CANADA,"R2C 3T4"),  // Winnipeg - Transcona
            Arguments.of(Locale.CANADA,"R2G 0W5"),  // Winnipeg - East Kildonan
            Arguments.of(Locale.CANADA,"R2H 2A4"),  // Winnipeg - St. Boniface
            Arguments.of(Locale.CANADA,"R2J 0K6"),  // Winnipeg - Windsor Park
            Arguments.of(Locale.CANADA,"R2N 1R6"),  // Winnipeg - St. Vital
            Arguments.of(Locale.CANADA,"R2P 2P7"),  // Winnipeg - Garden City
            Arguments.of(Locale.CANADA,"R2R 2R9"),  // Winnipeg - The Maples
            Arguments.of(Locale.CANADA,"R2V 1P8"),  // Winnipeg - West Kildonan
            Arguments.of(Locale.CANADA,"R3B 2B9"),  // Winnipeg - Downtown
            Arguments.of(Locale.CANADA,"R3C 4K5"),  // Winnipeg - Exchange District
            Arguments.of(Locale.CANADA,"R3J 0P2"),  // Winnipeg - St. James
            Arguments.of(Locale.CANADA,"R3M 2K7"),  // Winnipeg - River Heights
            Arguments.of(Locale.CANADA,"R3N 1Z6"),  // Winnipeg - Tuxedo
            Arguments.of(Locale.CANADA,"R3T 2N2"),  // Winnipeg - University of Manitoba
            Arguments.of(Locale.CANADA,"R7A 7A7"),  // Brandon - Downtown
            Arguments.of(Locale.CANADA,"R7B 0B1"),  // Brandon - North Hill
            Arguments.of(Locale.CANADA,"R8N 0B6"),  // Thompson
            Arguments.of(Locale.CANADA,"R9A 1L4"),  // Portage la Prairie

            // British Columbia (V)
            Arguments.of(Locale.CANADA,"V0A 1K0"),  // Rural BC - Kootenays
            Arguments.of(Locale.CANADA,"V0B 1G0"),  // Rural BC - East Kootenays
            Arguments.of(Locale.CANADA,"V0C 1K0"),  // Rural BC - Northern BC
            Arguments.of(Locale.CANADA,"V0E 1V0"),  // Rural BC - Thompson/Shuswap
            Arguments.of(Locale.CANADA,"V0G 1H0"),  // Rural BC - West Kootenays
            Arguments.of(Locale.CANADA,"V0H 1T0"),  // Rural BC - South Okanagan
            Arguments.of(Locale.CANADA,"V0J 1E0"),  // Rural BC - Northwest BC
            Arguments.of(Locale.CANADA,"V0K 1V0"),  // Rural BC - Thompson/Nicola
            Arguments.of(Locale.CANADA,"V0L 1K0"),  // Rural BC - Chilcotin
            Arguments.of(Locale.CANADA,"V0N 1A0"),  // Rural BC - Sea-to-Sky/Sunshine Coast
            Arguments.of(Locale.CANADA,"V0R 1L0"),  // Rural BC - Vancouver Island North
            Arguments.of(Locale.CANADA,"V0S 1K0"),  // Rural BC - Central Vancouver Island
            Arguments.of(Locale.CANADA,"V0T 1S0"),  // Rural BC - Central Coast
            Arguments.of(Locale.CANADA,"V0V 1A0"),  // Rural BC - Queen Charlotte Islands
            Arguments.of(Locale.CANADA,"V0W 1J0"),  // Rural BC - Atlin
            Arguments.of(Locale.CANADA,"V0X 1N0"),  // Rural BC - South Cariboo
            Arguments.of(Locale.CANADA,"V1A 1A1"),  // Cranbrook
            Arguments.of(Locale.CANADA,"V1C 4Z5"),  // Cranbrook - Downtown
            Arguments.of(Locale.CANADA,"V1J 5J4"),  // Fort St. John
            Arguments.of(Locale.CANADA,"V1L 6V6"),  // Nelson
            Arguments.of(Locale.CANADA,"V1Y 7W8"),  // Kelowna
            Arguments.of(Locale.CANADA,"V2A 5C6"),  // Penticton
            Arguments.of(Locale.CANADA,"V2C 6K2"),  // Kamloops
            Arguments.of(Locale.CANADA,"V2G 2A5"),  // Williams Lake
            Arguments.of(Locale.CANADA,"V2N 4Z3"),  // Prince George
            Arguments.of(Locale.CANADA,"V3A 1P9"),  // Langley
            Arguments.of(Locale.CANADA,"V3K 6R2"),  // Coquitlam
            Arguments.of(Locale.CANADA,"V3L 1B5"),  // New Westminster
            Arguments.of(Locale.CANADA,"V3N 4X8"),  // Burnaby - East
            Arguments.of(Locale.CANADA,"V3R 6Y8"),  // Surrey - Whalley
            Arguments.of(Locale.CANADA,"V3S 9A8"),  // Surrey - Cloverdale
            Arguments.of(Locale.CANADA,"V3T 4T8"),  // Surrey - Central City
            Arguments.of(Locale.CANADA,"V3W 1N1"),  // Surrey - Newton
            Arguments.of(Locale.CANADA,"V4A 4P1"),  // Surrey - White Rock
            Arguments.of(Locale.CANADA,"V4C 7V5"),  // Delta - North
            Arguments.of(Locale.CANADA,"V4N 0E2"),  // Surrey - Fraser Heights
            Arguments.of(Locale.CANADA,"V5A 1S6"),  // Burnaby - Simon Fraser University
            Arguments.of(Locale.CANADA,"V5G 4S2"),  // Burnaby - Metrotown
            Arguments.of(Locale.CANADA,"V5H 4N2"),  // Burnaby - Central
            Arguments.of(Locale.CANADA,"V5K 0A1"),  // Vancouver - East
            Arguments.of(Locale.CANADA,"V5L 1K9"),  // Vancouver - Grandview-Woodland
            Arguments.of(Locale.CANADA,"V5T 3K5"),  // Vancouver - Mount Pleasant
            Arguments.of(Locale.CANADA,"V5Z 4B8"),  // Vancouver - Fairview
            Arguments.of(Locale.CANADA,"V6A 1A1"),  // Vancouver - Downtown Eastside
            Arguments.of(Locale.CANADA,"V6B 5K3"),  // Vancouver - Downtown
            Arguments.of(Locale.CANADA,"V6C 3E1"),  // Vancouver - Coal Harbour
            Arguments.of(Locale.CANADA,"V6E 1V3"),  // Vancouver - West End
            Arguments.of(Locale.CANADA,"V6G 1Y6"),  // Vancouver - Stanley Park
            Arguments.of(Locale.CANADA,"V6H 3Z7"),  // Vancouver - Fairview/South Granville
            Arguments.of(Locale.CANADA,"V6H3Z7"),   // Without space
            Arguments.of(Locale.CANADA,"V6J 4Y9"),  // Vancouver - Kitsilano
            Arguments.of(Locale.CANADA,"V6K 4S6"),  // Vancouver - West Kitsilano
            Arguments.of(Locale.CANADA,"V6R 2G2"),  // Vancouver - West Point Grey
            Arguments.of(Locale.CANADA,"V6T 1Z4"),  // Vancouver - University of British Columbia
            Arguments.of(Locale.CANADA,"V7G 1T9"),  // North Vancouver
            Arguments.of(Locale.CANADA,"V7L 4J4"),  // North Vancouver - Lower Lonsdale
            Arguments.of(Locale.CANADA,"V7M 3H9"),  // North Vancouver - Lynn Creek
            Arguments.of(Locale.CANADA,"V7P 1T5"),  // North Vancouver - Upper Lonsdale
            Arguments.of(Locale.CANADA,"V7T 2Y8"),  // West Vancouver
            Arguments.of(Locale.CANADA,"V7V 4K3"),  // West Vancouver - Ambleside
            Arguments.of(Locale.CANADA,"V7W 2T8"),  // West Vancouver - Horseshoe Bay
            Arguments.of(Locale.CANADA,"V8A 1K7"),  // Duncan
            Arguments.of(Locale.CANADA,"V8K 2A6"),  // Salt Spring Island
            Arguments.of(Locale.CANADA,"V8P 5C2"),  // Victoria - Oak Bay
            Arguments.of(Locale.CANADA,"V8T 5G5"),  // Victoria - North Park
            Arguments.of(Locale.CANADA,"V8V 3V2"),  // Victoria - James Bay
            Arguments.of(Locale.CANADA,"V8W 1W5"),  // Victoria - Downtown
            Arguments.of(Locale.CANADA,"V8X 1Z3"),  // Victoria - Saanich
            Arguments.of(Locale.CANADA,"V8Y 3Z7"),  // Victoria - Cordova Bay
            Arguments.of(Locale.CANADA,"V9A 1L5"),  // Victoria - Esquimalt
            Arguments.of(Locale.CANADA,"V9L 6W3"),  // Duncan - Cowichan Valley
            Arguments.of(Locale.CANADA,"V9R 6R3"),  // Nanaimo - South
            Arguments.of(Locale.CANADA,"V9S 5T8"),  // Nanaimo - North
            Arguments.of(Locale.CANADA,"V9T 1Z7"),  // Parksville
            Arguments.of(Locale.CANADA,"V9W 3N3"),  // Campbell River
            Arguments.of(Locale.CANADA,"V9Y 7L8"),  // Port Alberni

            // Alberta (T)
            Arguments.of(Locale.CANADA,"T0A 0A1"),  // Rural Central Alberta
            Arguments.of(Locale.CANADA,"T0B 0B0"),  // Rural East Central Alberta
            Arguments.of(Locale.CANADA,"T0C 0C0"),  // Rural West Central Alberta
            Arguments.of(Locale.CANADA,"T0E 0E0"),  // Rural Northwest Alberta
            Arguments.of(Locale.CANADA,"T0G 0G0"),  // Rural Northeast Alberta
            Arguments.of(Locale.CANADA,"T0J 0J0"),  // Rural South Alberta
            Arguments.of(Locale.CANADA,"T0K 0K0"),  // Rural Southwest Alberta
            Arguments.of(Locale.CANADA,"T0L 0L0"),  // Rural Southeast Alberta
            Arguments.of(Locale.CANADA,"T0M 0M0"),  // Rural Central Alberta
            Arguments.of(Locale.CANADA,"T0P 0P0"),  // Rural Northern Alberta
            Arguments.of(Locale.CANADA,"T1H 0A1"),  // Lethbridge
            Arguments.of(Locale.CANADA,"T1J 4P4"),  // Lethbridge - South
            Arguments.of(Locale.CANADA,"T1K 7G8"),  // Lethbridge - North
            Arguments.of(Locale.CANADA,"T1W 3L1"),  // Canmore
            Arguments.of(Locale.CANADA,"T1Y 1A1"),  // Calgary - Northeast
            Arguments.of(Locale.CANADA,"T2A 0A8"),  // Calgary - Northeast Industrial
            Arguments.of(Locale.CANADA,"T2B 2N9"),  // Calgary - Forest Lawn
            Arguments.of(Locale.CANADA,"T2E 6Z3"),  // Calgary - Airport
            Arguments.of(Locale.CANADA,"T2G 0P3"),  // Calgary - Downtown
            Arguments.of(Locale.CANADA,"T2H 0K5"),  // Calgary - Macleod Trail
            Arguments.of(Locale.CANADA,"T2J 6T5"),  // Calgary - Lake Bonavista
            Arguments.of(Locale.CANADA,"T2N 1N4"),  // Calgary - University of Calgary
            Arguments.of(Locale.CANADA,"T2P 2G8"),  // Calgary - Financial District
            Arguments.of(Locale.CANADA,"T2R 0S7"),  // Calgary - Beltline
            Arguments.of(Locale.CANADA,"T2T 5J9"),  // Calgary - Marda Loop
            Arguments.of(Locale.CANADA,"T2V 5A7"),  // Calgary - Canyon Meadows
            Arguments.of(Locale.CANADA,"T2Y 4J2"),  // Calgary - Somerset
            Arguments.of(Locale.CANADA,"T3A 5K8"),  // Calgary - Northwest
            Arguments.of(Locale.CANADA,"T3B 5Y7"),  // Calgary - Bowness
            Arguments.of(Locale.CANADA,"T3E 6K8"),  // Calgary - Southwest
            Arguments.of(Locale.CANADA,"T3G 5T9"),  // Calgary - Scenic Acres
            Arguments.of(Locale.CANADA,"T3H 4T8"),  // Calgary - Signal Hill
            Arguments.of(Locale.CANADA,"T3J 3R5"),  // Calgary - Falconridge
            Arguments.of(Locale.CANADA,"T3K 6K7"),  // Calgary - Hidden Valley
            Arguments.of(Locale.CANADA,"T3L 2N7"),  // Calgary - Rocky Ridge
            Arguments.of(Locale.CANADA,"T3M 1P3"),  // Calgary - Springbank
            Arguments.of(Locale.CANADA,"T3Z 3N8"),  // Calgary - Elbow Valley
            Arguments.of(Locale.CANADA,"T4A 0V5"),  // Airdrie
            Arguments.of(Locale.CANADA,"T4B 3G5"),  // Airdrie - East
            Arguments.of(Locale.CANADA,"T4N 6N6"),  // Red Deer
            Arguments.of(Locale.CANADA,"T4R 2N4"),  // Red Deer - South
            Arguments.of(Locale.CANADA,"T4S 2C4"),  // Sylvan Lake
            Arguments.of(Locale.CANADA,"T5A 0A1"),  // Edmonton - Northeast
            Arguments.of(Locale.CANADA,"T5G 2Z5"),  // Edmonton - Westmount
            Arguments.of(Locale.CANADA,"T5H 3Z7"),  // Edmonton - Downtown
            Arguments.of(Locale.CANADA,"T5J 0N3"),  // Edmonton - Financial District
            Arguments.of(Locale.CANADA,"T5K 2M4"),  // Edmonton - Legislature
            Arguments.of(Locale.CANADA,"T5M 3B7"),  // Edmonton - North Central
            Arguments.of(Locale.CANADA,"T5N 3A1"),  // Edmonton - Glenora
            Arguments.of(Locale.CANADA,"T5T 6A6"),  // Edmonton - West
            Arguments.of(Locale.CANADA,"T6A 1S6"),  // Edmonton - Southeast
            Arguments.of(Locale.CANADA,"T6B 2X2"),  // Edmonton - Industrial Southeast
            Arguments.of(Locale.CANADA,"T6E 5T5"),  // Edmonton - South Central
            Arguments.of(Locale.CANADA,"T6G 2R3"),  // Edmonton - University of Alberta
            Arguments.of(Locale.CANADA,"T6H 5J8"),  // Edmonton - Southwest
            Arguments.of(Locale.CANADA,"T6J 6T7"),  // Edmonton - Southgate
            Arguments.of(Locale.CANADA,"T6L 6M8"),  // Edmonton - Mill Woods
            Arguments.of(Locale.CANADA,"T6R 3L5"),  // Edmonton - Riverbend
            Arguments.of(Locale.CANADA,"T6T 0E5"),  // Edmonton - Ellerslie
            Arguments.of(Locale.CANADA,"T6V 1P1"),  // Edmonton - West Industrial
            Arguments.of(Locale.CANADA,"T6W 3A1"),  // Edmonton - Heritage Valley
            Arguments.of(Locale.CANADA,"T7A 0A1"),  // Fort McMurray
            Arguments.of(Locale.CANADA,"T7E 3X5"),  // Drayton Valley
            Arguments.of(Locale.CANADA,"T7N 1N9"),  // Wetaskiwin
            Arguments.of(Locale.CANADA,"T7P 2Z9"),  // Peace River
            Arguments.of(Locale.CANADA,"T7S 1N5"),  // Fort McMurray - Downtown
            Arguments.of(Locale.CANADA,"T7X 3A7"),  // Spruce Grove
            Arguments.of(Locale.CANADA,"T7Y 1A1"),  // Stony Plain
            Arguments.of(Locale.CANADA,"T7Z 1N4"),  // Fort Saskatchewan
            Arguments.of(Locale.CANADA,"T8A 1L1"),  // Sherwood Park
            Arguments.of(Locale.CANADA,"T8H 0H3"),  // Sherwood Park - East
            Arguments.of(Locale.CANADA,"T8N 6N3"),  // St. Albert
            Arguments.of(Locale.CANADA,"T8R 1N9"),  // St. Albert - North
            Arguments.of(Locale.CANADA,"T8V 1G4"),  // Grande Prairie
            Arguments.of(Locale.CANADA,"T8W 2G5"),  // Grande Prairie - South
            Arguments.of(Locale.CANADA,"T8X 0B9"),  // Grande Prairie - West
            Arguments.of(Locale.CANADA,"T9A 1L7"),  // Leduc
            Arguments.of(Locale.CANADA,"T9C 1N9"),  // Edmonton International Airport
            Arguments.of(Locale.CANADA,"T9E 1J9"),  // Leduc - East
            Arguments.of(Locale.CANADA,"T9G 1K6"),  // Beaumont
            Arguments.of(Locale.CANADA,"T9H 1A1"),  // Slave Lake
            Arguments.of(Locale.CANADA,"T9K 0V1"),  // Fort McMurray - Thickwood
            Arguments.of(Locale.CANADA,"T9N 1N9"),  // High River
            Arguments.of(Locale.CANADA,"T9S 1K5"),  // Cold Lake
            Arguments.of(Locale.CANADA,"T9V 1K7"),  // Lloydminster
            Arguments.of(Locale.CANADA,"T9W 1H9"),  // Lloydminster - West
            Arguments.of(Locale.CANADA,"T9X 1G7"),  // Bonnyville

            // Saskatchewan (S)
            Arguments.of(Locale.CANADA,"S0A 0A1"),  // Rural Southern Saskatchewan
            Arguments.of(Locale.CANADA,"S0C 0C0"),  // Rural Southwest Saskatchewan
            Arguments.of(Locale.CANADA,"S0E 0E0"),  // Rural Southeast Saskatchewan
            Arguments.of(Locale.CANADA,"S0G 5C0"),  // Rural Central Saskatchewan
            Arguments.of(Locale.CANADA,"S0H 0H0"),  // Rural West Central Saskatchewan
            Arguments.of(Locale.CANADA,"S0J 0J0"),  // Rural East Central Saskatchewan
            Arguments.of(Locale.CANADA,"S0K 0K0"),  // Rural East Saskatchewan
            Arguments.of(Locale.CANADA,"S0L 0L0"),  // Rural Northwest Saskatchewan
            Arguments.of(Locale.CANADA,"S0M 0M0"),  // Rural Northeast Saskatchewan
            Arguments.of(Locale.CANADA,"S0N 0N0"),  // Rural North Saskatchewan
            Arguments.of(Locale.CANADA,"S0P 0P0"),  // Rural Northern Saskatchewan
            Arguments.of(Locale.CANADA,"S4P 3V7"),  // Regina - Downtown
            Arguments.of(Locale.CANADA,"S4R 1X8"),  // Regina - North Central
            Arguments.of(Locale.CANADA,"S4S 6W4"),  // Regina - South
            Arguments.of(Locale.CANADA,"S4T 7K9"),  // Regina - East
            Arguments.of(Locale.CANADA,"S4V 3A7"),  // Regina - East
            Arguments.of(Locale.CANADA,"S4W 0B7"),  // Regina - Southeast
            Arguments.of(Locale.CANADA,"S4X 4P7"),  // White City
            Arguments.of(Locale.CANADA,"S4Y 1B3"),  // Emerald Park
            Arguments.of(Locale.CANADA,"S4Z 1C3"),  // Pilot Butte
            Arguments.of(Locale.CANADA,"S6H 7K9"),  // Moose Jaw
            Arguments.of(Locale.CANADA,"S6J 1N4"),  // Moose Jaw - South
            Arguments.of(Locale.CANADA,"S6K 0A1"),  // Moose Jaw - West
            Arguments.of(Locale.CANADA,"S6V 7K8"),  // Prince Albert
            Arguments.of(Locale.CANADA,"S6W 1A9"),  // Prince Albert - West
            Arguments.of(Locale.CANADA,"S6X 1B5"),  // Prince Albert - East
            Arguments.of(Locale.CANADA,"S7H 0A1"),  // Saskatoon - East
            Arguments.of(Locale.CANADA,"S7J 0A1"),  // Saskatoon - Southeast
            Arguments.of(Locale.CANADA,"S7K 0J5"),  // Saskatoon - Downtown
            Arguments.of(Locale.CANADA,"S7L 6A7"),  // Saskatoon - West
            Arguments.of(Locale.CANADA,"S7M 4K7"),  // Saskatoon - Southwest
            Arguments.of(Locale.CANADA,"S7N 1B1"),  // Saskatoon - University of Saskatchewan
            Arguments.of(Locale.CANADA,"S7P 0K1"),  // Saskatoon - North Industrial
            Arguments.of(Locale.CANADA,"S7R 1A4"),  // Saskatoon - Northwest
            Arguments.of(Locale.CANADA,"S7S 1N5"),  // Saskatoon - Northeast
            Arguments.of(Locale.CANADA,"S7T 1A1"),  // Saskatoon - Lakewood
            Arguments.of(Locale.CANADA,"S7V 1K4"),  // Saskatoon - Stonebridge
            Arguments.of(Locale.CANADA,"S7W 1B1"),  // Martensville
            Arguments.of(Locale.CANADA,"S9A 0A1"),  // Swift Current
            Arguments.of(Locale.CANADA,"S9H 0A1"),  // North Battleford
            Arguments.of(Locale.CANADA,"S9V 1K9"),  // Humboldt
            Arguments.of(Locale.CANADA,"S9X 1A9"),  // Melfort

            // Newfoundland and Labrador (A)
            Arguments.of(Locale.CANADA,"A0A 1B0"),  // Rural Eastern Newfoundland
            Arguments.of(Locale.CANADA,"A0B 1C0"),  // Rural Central Newfoundland
            Arguments.of(Locale.CANADA,"A0E 1E0"),  // Burin Peninsula
            Arguments.of(Locale.CANADA,"A0G 1G0"),  // Northern Peninsula
            Arguments.of(Locale.CANADA,"A0H 1H0"),  // Southern Labrador
            Arguments.of(Locale.CANADA,"A0J 1J0"),  // Northern Labrador
            Arguments.of(Locale.CANADA,"A0K 1K0"),  // Western Newfoundland
            Arguments.of(Locale.CANADA,"A0L 1L0"),  // Western Newfoundland
            Arguments.of(Locale.CANADA,"A0M 1M0"),  // Baie Verte Peninsula
            Arguments.of(Locale.CANADA,"A0N 1N0"),  // Southwest Newfoundland
            Arguments.of(Locale.CANADA,"A0P 1P0"),  // Gander/Grand Falls-Windsor
            Arguments.of(Locale.CANADA,"A0R 1R0"),  // Lewisporte/Twillingate
            Arguments.of(Locale.CANADA,"A1A 1A1"),  // St. John's - Downtown
            Arguments.of(Locale.CANADA,"A1B 2C3"),  // St. John's - East End
            Arguments.of(Locale.CANADA,"A1C 5S4"),  // St. John's - Central
            Arguments.of(Locale.CANADA,"A1E 1J5"),  // St. John's - North
            Arguments.of(Locale.CANADA,"A1G 1Z7"),  // Mount Pearl
            Arguments.of(Locale.CANADA,"A1H 1N8"),  // Torbay
            Arguments.of(Locale.CANADA,"A1K 1K7"),  // Conception Bay South
            Arguments.of(Locale.CANADA,"A1L 1L8"),  // Paradise
            Arguments.of(Locale.CANADA,"A1M 4N1"),  // Portugal Cove-St. Philip's
            Arguments.of(Locale.CANADA,"A1N 2N9"),  // Bay Roberts
            Arguments.of(Locale.CANADA,"A1S 1S9"),  // Carbonear
            Arguments.of(Locale.CANADA,"A1V 1V7"),  // Clarenville
            Arguments.of(Locale.CANADA,"A1W 1W8"),  // Bonavista
            Arguments.of(Locale.CANADA,"A1Y 1Y5"),  // Marystown
            Arguments.of(Locale.CANADA,"A2A 1A1"),  // Gander
            Arguments.of(Locale.CANADA,"A2B 1J4"),  // Corner Brook
            Arguments.of(Locale.CANADA,"A2H 6J8"),  // Stephenville
            Arguments.of(Locale.CANADA,"A2N 2N7"),  // Grand Falls-Windsor
            Arguments.of(Locale.CANADA,"A5A 5A5"),  // Labrador City
            Arguments.of(Locale.CANADA,"A8A 3K7"),  // Happy Valley-Goose Bay

            // Prince Edward Island (C)
            Arguments.of(Locale.CANADA,"C0A 1H0"),  // Rural PEI
            Arguments.of(Locale.CANADA,"C0B 1B0"),  // Eastern PEI
            Arguments.of(Locale.CANADA,"C1A 4P3"),  // Charlottetown - Downtown
            Arguments.of(Locale.CANADA,"C1B 1J9"),  // Charlottetown - East
            Arguments.of(Locale.CANADA,"C1C 1K7"),  // Charlottetown - West
            Arguments.of(Locale.CANADA,"C1E 1Z3"),  // Stratford
            Arguments.of(Locale.CANADA,"C1N 4J8"),  // Summerside
            Arguments.of(Locale.CANADA,"C9A 1S5"),  // Cornwall

            // Northwest Territories (X)
            Arguments.of(Locale.CANADA,"X0E 0V0"),  // Rural NWT
            Arguments.of(Locale.CANADA,"X0G 0G0"),  // Western NWT
            Arguments.of(Locale.CANADA,"X1A 2P3"),  // Yellowknife
            Arguments.of(Locale.CANADA,"X1A 3T6"),  // Yellowknife - Downtown
            Arguments.of(Locale.CANADA,"X1A 3X7"),  // Yellowknife - Frame Lake

            // Yukon (Y)
            Arguments.of(Locale.CANADA,"Y0A 1C0"),  // Rural Central Yukon
            Arguments.of(Locale.CANADA,"Y0B 1N0"),  // Rural Northwest Yukon
            Arguments.of(Locale.CANADA,"Y1A 5Y7"),  // Whitehorse
            Arguments.of(Locale.CANADA,"Y1A 6L6"),  // Whitehorse - Downtown
            Arguments.of(Locale.CANADA,"Y1A 6N6"),  // Whitehorse - Riverdale

            // Nunavut (X)
            Arguments.of(Locale.CANADA,"X0A 0H0"),  // Iqaluit
            Arguments.of(Locale.CANADA,"X0A 1H0"),  // Baffin Region
            Arguments.of(Locale.CANADA,"X0B 0C0"),  // Rural Nunavut
            Arguments.of(Locale.CANADA,"X0C 0G0"),  // Kivalliq Region

            // Special and unique codes
            Arguments.of(Locale.CANADA,"H0H 0H0"),  // Santa Claus (Christmas letters)
            Arguments.of(Locale.CANADA,"K0K 3K0"),  // Campbellford, unique K0K code
            Arguments.of(Locale.CANADA,"T0H 1A0")   // Special code for High Prairie, AB
        );
    }


    public static Stream<Arguments>
    invalidInputs()
    {
        return Stream.of(
            // United States - invalid formats
            Arguments.of(Locale.US,"9070"),         // Too short (needs 5 digits)
            Arguments.of(Locale.US,"907031"),       // Invalid length
            Arguments.of(Locale.US,"90703-123"),    // Incomplete ZIP+4
            Arguments.of(Locale.US,"9O703"),        // Contains letter
            Arguments.of(Locale.US,"ABCDE"),        // All letters

            // Canada - invalid formats
            Arguments.of(Locale.CANADA,"K1A0B"),    // Incomplete
            Arguments.of(Locale.CANADA,"K1A 0B"),   // Incomplete
            Arguments.of(Locale.CANADA,"K10 0B1"),  // Digit in wrong position
            Arguments.of(Locale.CANADA,"1K1 0B1"),  // Starts with digit
            Arguments.of(Locale.CANADA,"KIAOB1"),   // Contains "I" (not used)

            // UK - invalid formats
            Arguments.of(new Locale("en","GB"),"SW1A 1A"),   // Incomplete inward code
            Arguments.of(new Locale("en","GB"),"SW1A1A"),    // Missing space
            Arguments.of(new Locale("en","GB"),"SW1A 1AAA"), // Too long
            Arguments.of(new Locale("en","GB"),"1234 567"),  // All numeric

            // Germany - invalid formats
            Arguments.of(Locale.GERMANY,"1234"),     // Too short
            Arguments.of(Locale.GERMANY,"123456"),   // Too long
            Arguments.of(Locale.GERMANY,"1234A"),    // Contains letter
            Arguments.of(Locale.GERMANY,"0"),        // Too short

            // France - invalid formats
            Arguments.of(Locale.FRANCE,"7500"),     // Too short
            Arguments.of(Locale.FRANCE,"750011"),   // Too long
            Arguments.of(Locale.FRANCE,"A5001"),    // Contains letter

            // Japan - invalid formats
            Arguments.of(Locale.JAPAN,"123-45"),    // Wrong segment lengths
            Arguments.of(Locale.JAPAN,"1234-567"),  // Wrong segment lengths
            Arguments.of(Locale.JAPAN,"12345678"),  // Too long
            Arguments.of(Locale.JAPAN,"123-ABC"),   // Contains letters

            // Poland - invalid formats
            Arguments.of(new Locale("pl","PL"),"123-45"),    // Wrong segment lengths
            Arguments.of(new Locale("pl","PL"),"12-34"),     // Wrong segment lengths
            Arguments.of(new Locale("pl","PL"),"1-12345"),   // Wrong segment lengths
            Arguments.of(new Locale("pl","PL"),"AB-123"),    // Contains letters

            // Netherlands - invalid formats
            Arguments.of(new Locale("nl","NL"),"123 ABC"),  // Wrong number format
            Arguments.of(new Locale("nl","NL"),"12345 AB"), // Too many digits
            Arguments.of(new Locale("nl","NL"),"1234 A"),   // Incomplete letters
            Arguments.of(new Locale("nl","NL"),"1234 ABC"), // Too many letters

            // General invalid patterns
            Arguments.of(Locale.US,""),              // Empty string
            Arguments.of(Locale.GERMANY," "),        // Just whitespace
            Arguments.of(Locale.FRANCE,"ABC-DEF"),   // All letters with separator
            Arguments.of(Locale.JAPAN,"#12345"),     // Special characters
            Arguments.of(Locale.UK,"!@#$%"),         // Special characters
            Arguments.of(Locale.CANADA,"K1A 0B1!"),  // Valid with extra character

            // Special cases - non-existent postal codes
            Arguments.of(Locale.US,"00000"),         // Non-existent code
            Arguments.of(new Locale("en","GB"),"QQ9 9QQ")  // Non-existent area code
        );
    }
}

//////////////////////////////////////////////////////////////////////////////
