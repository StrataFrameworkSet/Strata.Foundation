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

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Tag("CommitStage")
public
class EmailAddressTest
{

    public static Stream<String>
    valid()
    {
        return
            Stream.of(
                "abc@xyz.com",
                "abc.def@xxyyzz.org",
                "john.liebenau@ayuda.software");
    }

    public static Stream<String>
    invalid()
    {
        return
            Stream.of(
                "www.xyz.com",
                "abc.def.xxyyzz.org",
                "john.liebenau@ayuda");
    }

    @ParameterizedTest
    @MethodSource("valid")
    public void
    testValidInput(String input)
    {
        new EmailAddress(input);
    }

    @ParameterizedTest
    @MethodSource("invalid")
    public void
    testInvalidInput(String input)
    {
        try
        {
            new EmailAddress(input);
            fail("Should have thrown exception: " + input + " is invalid");
        }
        catch (IllegalArgumentException e) {}
    }


    @ParameterizedTest
    @MethodSource("valid")
    public void
    testMapping(String input) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();
        EmailAddress  expected = new EmailAddress(input);
        EmailAddress  actual =
            mapper.readValue(
                mapper.writeValueAsString(expected),EmailAddress.class);

        assertEquals(expected,actual);
    }


}

//////////////////////////////////////////////////////////////////////////////
