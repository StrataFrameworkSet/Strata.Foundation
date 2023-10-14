//////////////////////////////////////////////////////////////////////////////
// PhoneNumberTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import strata.foundation.core.mapper.ObjectMapperSupplier;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Tag("CommitStage")
public
class PostalAddressTest
{

    public static Stream<Map<String,String>>
    valid()
    {
        return
            Stream.of(
                Map.of(
                    "Address","12407",
                    "Street","Creekwood Ave.",
                    "City","Cerritos",
                    "State","CA",
                    "CountryCode","USA",
                    "PostalCode","90703"),
                Map.of(
                    "Address","11 Unit 12b",
                    "Street","Mozartstraße",
                    "City","Leipzig",
                    "State","Sachsen",
                    "CountryCode","DE",
                    "PostalCode","04107"),
                Map.of(
                    "Address","5558",
                    "Street","Buckingham Ave",
                    "City","Burnaby",
                    "State","BC",
                    "CountryCode","CA",
                    "PostalCode","V5E 2A1"));
    }


    @ParameterizedTest
    @MethodSource("valid")
    public void
    testValidInput(Map<String,String> input)
    {
        new PostalAddressBuilder()
            .setAddress(input.get("Address"))
            .setStreet(input.get("Street"))
            .setCity(input.get("City"))
            .setState(input.get("State"))
            .setCountryCode(input.get("CountryCode"))
            .setPostalCode(input.get("PostalCode"))
            .build();
    }

    @ParameterizedTest
    @MethodSource("valid")
    public void
    testMapping(Map<String,String> input) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();
        PostalAddress  expected = buildPostalAddress(input);
        PostalAddress  actual =
            mapper.readValue(
                mapper.writeValueAsString(expected),PostalAddress.class);

        assertEquals(expected,actual);
    }

    private static PostalAddress
    buildPostalAddress(Map<String,String> input)
    {
        return
            new PostalAddressBuilder()
                .setAddress(input.get("Address"))
                .setStreet(input.get("Street"))
                .setCity(input.get("City"))
                .setState(input.get("State"))
                .setCountryCode(input.get("CountryCode"))
                .setPostalCode(input.get("PostalCode"))
                .build();
    }
}

//////////////////////////////////////////////////////////////////////////////
