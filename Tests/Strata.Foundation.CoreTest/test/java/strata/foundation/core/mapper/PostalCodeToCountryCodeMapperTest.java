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
}

//////////////////////////////////////////////////////////////////////////////
