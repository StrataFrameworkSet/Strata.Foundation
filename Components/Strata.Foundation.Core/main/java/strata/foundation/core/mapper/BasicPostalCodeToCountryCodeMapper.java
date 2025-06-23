//////////////////////////////////////////////////////////////////////////////
// BasicPostalCodeToCountryCodeMapper.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import strata.foundation.core.container.Pair;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public
class BasicPostalCodeToCountryCodeMapper
    implements IPostalCodeToCountryCodeMapper
{
    private static final Map<String,Set<String>>
    POSTAL_CODE_REGEX_TO_COUNTRIES = initializeRegexMap();

    private static final Map<String,Set<Pair<Integer,Integer>>>
    COUNTRY_SPECIFIC_RANGES = initializeCountryRanges();

    private final Locale locale;

    public
    BasicPostalCodeToCountryCodeMapper()
    {
        this(Locale.getDefault());
    }

    public
    BasicPostalCodeToCountryCodeMapper(Locale locale)
    {
        this.locale = locale;
    }

    @Override
    public Set<String>
    map(String postalCode)
    {
        if (isNullOrEmpty(postalCode))
            return Set.of();

        Set<String> possibleCountryCodes =
            getCountryCodesForPostalCode(postalCode);

        if (possibleCountryCodes.contains(locale.getCountry()))
        {
            if (isValidForCountry(postalCode,locale.getCountry()))
                return Set.of(locale.getCountry());

            return
                possibleCountryCodes
                    .stream()
                    .filter(countryCode -> isValidForCountry(postalCode,countryCode))
                    .collect(Collectors.toSet());
        }

        return possibleCountryCodes;
    }

    private static Set<String>
    getCountryCodesForPostalCode(String postalCode)
    {
        String normalizedPostalCode = normalize(postalCode);

        return
            POSTAL_CODE_REGEX_TO_COUNTRIES
                .entrySet()
                .stream()
                .filter(entry -> normalizedPostalCode.matches(entry.getKey()))
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.toSet());
    }

    private static boolean
    isValidForCountry(String postalCode,String countryCode)
    {
        String normalizedPostalCode = normalize(postalCode);

        if (isNullOrEmpty(countryCode))
            return false;

        if (
            postalCode.matches("\\d{5}") &&
            COUNTRY_SPECIFIC_RANGES.containsKey(countryCode))
        {
            try
            {
                int numericCode = Integer.parseInt(normalizedPostalCode);

                return
                    COUNTRY_SPECIFIC_RANGES
                        .get(countryCode)
                        .stream()
                        .anyMatch(range -> isInRange(numericCode,range));
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        }

        return true;
    }

    private static Map<String,Set<String>>
    initializeRegexMap()
    {
        Map<String,Set<String>> map = new HashMap<>();

        // UK and dependencies
        final String OUTWARD_CODE_REGEX =
            "(" +
            // common outward codes
            "((A[BL]?|B[ABDHLNRSTX]?|C[ABFHMORTVW]|D[ADEGHLNTY]|E[HNX]|F[KY]|G[LU]?|H[ADGPRSUX]|I[GPV]|K[ATWY]|L[ADELNSU]?|M[EKL]?|N[EGNPRW]|O[LX]|P[AEHLOR]|R[GHM]|S[AEGKLMNOPRSTY]?|T[ADFNQRSW]|UB|W[ADFNRSV]|XX|YO|ZE)\\d{1,2})|" +
            // London specific outward codes
            "((E[1-9][0-9]?|E1W|EC[1-4][AMNPRVY]|EC50)|N[1-9][0-9]?|N1[CP]|NW[1-9][0-9]?|NW1W|SE[1-9][0-9]?|SE1P|SW1[AEHPVWXY]|SW[2-9][0-9]?|W1[ABCDFGHJKSTUW]|W[2-9][0-9]?|WC1[ABEHNRVX]|WC2[ABEHNR])" +
            ")";

        final String INWARD_CODE_REGEX = "([0-9][ABD-HJLN-UW-Z]{2})";
        final String SPECIAL_CODE_REGEX = "(GIR[ ]?0AA|BFPO[ ]?\\d{1,4}|XM4[ ]?5HQ)";
        final String OVERSEAS_TERRITORIES_REGEX = "((AI[ \\-]?2640)|(ASCN|STHL|TDCU|BBND|BIQQ|FIQQ|PCRN|SIQQ|TKCA)[ ]?1ZZ|GX11[ ]?1AA)";
        final String UK_POSTAL_CODE_REGEX =
            String.format(
                "%s[ ]?%s|%s|%s",
                OUTWARD_CODE_REGEX,
                INWARD_CODE_REGEX,
                SPECIAL_CODE_REGEX,
                OVERSEAS_TERRITORIES_REGEX);

        map.put(
            //"GIR[ ]?0AA|((AB|AL|B|BA|BB|BD|BH|BL|BN|BR|BS|BT|BX|CA|CB|CF|CH|CM|CO|CR[0-9]?|CT|CV|CW|DA|DD|DE|DG|DH|DL|DN|DT|DY|E[1-9CN]|EC[1-4]|EH|EN|EX|FK|FY|G[1-9]|GL|GY|GU|HA|HD|HG|HP|HR|HS|HU|HX|IG|IM|IP|IV|JE|KA|KT|KW|KY|L[1-9]|LA|LD|LE|LL|LN|LS|LU|M[1-9]|ME|MK|ML|N[1-9CEGW]|NE|NG|NN|NP|NR|NW|OL|OX|PA|PE|PH|PL|PO|PR|RG|RH|RM|S[1-9EOYW]|SA|SE|SG|SK|SL|SM|SN|SO|SP|SR|SS|ST|SW|SY|TA|TD|TF|TN|TQ|TR|TS|TW|UB|W[1-9C]|WA|WC[1-2]|WD|WF|WN|WR|WS|WV|YO|ZE)(\\d[\\dA-Z]?))[ ]?(\\d[ABD-HJLN-UW-Z]{2})|BFPO[ ]?\\d{1,4}",
            UK_POSTAL_CODE_REGEX,
            Set.of("GB"));
        map.put(
            "JE\\d[\\dA-Z]?[ ]?\\d[ABD-HJLN-UW-Z]{2}",
            Set.of("JE","GB"));
        map.put(
            "GY\\d[\\dA-Z]?[ ]?\\d[ABD-HJLN-UW-Z]{2}",
            Set.of("GG","GB"));
        map.put(
            "IM\\d[\\dA-Z]?[ ]?\\d[ABD-HJLN-UW-Z]{2}",
            Set.of("IM","GB"));

        // North America
        map.put(
            "\\d{5}([ \\-]\\d{4})?",
            Set.of("US"));
        map.put(
            "[ABCEGHJKLMNPRSTVXY]\\d[ABCEGHJ-NPRSTV-Z][ ]?\\d[ABCEGHJ-NPRSTV-Z]\\d",
            Set.of("CA"));

        // Europe
        map.put(
            "\\d{5}",
            Set.of(
                "DE","ES","FR","IT","TR","UA","FI","DZ","EG","IL","JO","KW",
                "LT","MY","NP","PK","SA","SN","TH","UY","HR","BA","KH","CV",
                "DO","EE","GT","ID","KE","LA","MA","MX","XK"));
        map.put(
            "\\d{4}",
            Set.of(
                "AU","AT","BE","DK","HU","LU","NZ","NO","PY","PH","SI","CH",
                "BG","BY","CY","FO","GE","HT","LV","MK","MD"));
        map.put(
            "\\d{4}[ ]?[A-Z]{2}",
            Set.of("NL"));
        map.put(
            "\\d{2}[-]?\\d{3}",
            Set.of("PL"));
        map.put(
            "\\d{3}[ ]?\\d{2}",
            Set.of("SE","CZ","SK","GR"));

        // Asia
        map.put(
            "\\d{3}-\\d{4}",
            Set.of("JP"));
        map.put(
            "\\d{3}[\\-]\\d{3}",
            Set.of("KR"));
        map.put(
            "\\d{6}",
            Set.of("CN","IN","ID","KZ","PK","RO","RU","SG","RS","KG","LK","TJ","TM","UZ"));
        map.put(
            "\\d{3}(\\d{2})?",
            Set.of("TW"));

        // Special regions
        map.put(
            "980\\d{2}",
            Set.of("MC"));
        map.put(
            "4789\\d",
            Set.of("SM"));
        map.put(
            "00120",
            Set.of("VA"));
        map.put(
            "22\\d{3}",
            Set.of("AX"));

        // South America
        map.put(
            "\\d{5}[\\-]?\\d{3}",
            Set.of("BR"));
        map.put(
            "\\d{4}([\\-]\\d{3})?",
            Set.of("PT","EC"));

        return map;
    }

    private static Map<String,Set<Pair<Integer,Integer>>>
    initializeCountryRanges()
    {
        Map<String,Set<Pair<Integer,Integer>>> ranges = new HashMap<>();

        ranges.put("US",Set.of(new Pair<>(1,99999)));
        ranges.put("DE",Set.of(new Pair<>(1067,99998)));
        ranges.put("FR",Set.of(new Pair<>(1000,95999),new Pair<>(97100,98890)));
        ranges.put("ES",Set.of(new Pair<>(1001,52999)));
        ranges.put("IT",Set.of(new Pair<>(10,98168)));
        ranges.put("MC",Set.of(new Pair<>(98000,98099)));
        ranges.put("SM",Set.of(new Pair<>(47890,47899)));
        ranges.put("VA",Set.of(new Pair<>(120,120)));
        ranges.put("AX",Set.of(new Pair<>(22000,22999)));

        return ranges;
    }

    private static boolean
    isNullOrEmpty(String str)
    {
        return str == null || str.isEmpty();
    }

    private static boolean
    isInRange(int value,Pair<Integer,Integer> range)
    {
        return value >= range.getFirst() && value <= range.getSecond();
    }

    private static String
    normalize(String input)
    {
        return input == null ? "" : input.trim().toUpperCase();
    }
}

//////////////////////////////////////////////////////////////////////////////
