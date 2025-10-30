//////////////////////////////////////////////////////////////////////////////
// PhoneNumberTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import strata.foundation.core.mapper.ObjectMapperSupplier;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("CommitStage")
public
class PhoneNumberTest
{



    @ParameterizedTest
    @MethodSource("data")
    public void
    testNanpFormat(String input)
    {
        new PhoneNumber(input);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void
    testGetDigitsOnly(String input)
    {
        System
            .out
            .println(new PhoneNumber(input).getDigitsOnly());

        Assertions.assertTrue(
            new PhoneNumber(input)
                .getDigitsOnly()
                .chars()
                .allMatch(Character::isDigit));
    }


    @ParameterizedTest
    @MethodSource("allFormats")
    public void
    testMapping(String input) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();
        PhoneNumber  expected = PhoneNumber.of(input);
        PhoneNumber  actual =
            mapper.readValue(
                mapper.writeValueAsString(expected),PhoneNumber.class);

        System.out.println(mapper.writeValueAsString(expected));
        assertEquals(expected,actual);
    }

    private static Stream<String>
    data()
    {
        return
            Stream.of(
                "2355551234",
                "235-555-1234",
                "235.555.1234",
                "235 555 1234",
                "(235)5551234",
                "(235)555-1234",
                "(235)555.1234",
                "(235) 555 1234",
                "12355551234",
                "1-235-555-1234",
                "+1-235-555-1234",
                "1.235.555.1234",
                "+1.235.555.1234",
                "1 235 555 1234",
                "+1 235 555 1234",
                "1(235)5551234",
                "+1(235)5551234",
                "1(235)555-1234",
                "+1(235)555-1234",
                "1(235)555.1234",
                "+1(235)555.1234",
                "1 (235) 555 1234",
                "+1 (235) 555 1234");
    }

    private static Stream<String>
    allFormats()
    {
        return
            Stream.of(
                "011813116437555",
                // NANP format examples (North American Numbering Plan)
                "2355551234",
                "235-555-1234",
                "235.555.1234",
                "235 555 1234",
                "(235)5551234",
                "(235)555-1234",
                "(235)555.1234",
                "(235) 555 1234",
                "12355551234",
                "1-235-555-1234",
                "+1-235-555-1234",
                "1.235.555.1234",
                "+1.235.555.1234",
                "1 235 555 1234",
                "+1 235 555 1234",
                "1(235)5551234",
                "+1(235)5551234",
                "1(235)555-1234",
                "+1(235)555-1234",
                "1(235)555.1234",
                "+1(235)555.1234",
                "1 (235) 555 1234",
                "+1 (235) 555 1234",

                // ITU-T format examples (International format)
                "+011 813 116 437 555",
                "+44 20 7946 0958",     // UK
                "+33 1 42 86 83 26",    // France
                "+49 30 12345678",      // Germany
                "+81 3 1234 5678",      // Japan
                "+86 10 1234 5678",     // China
                "+7 495 123 4567",      // Russia
                "+61 2 1234 5678",      // Australia
                "+55 11 1234 5678",     // Brazil
                "+91 11 1234 5678",     // India
                "+39 06 1234 5678",     // Italy
                "+34 91 123 4567",      // Spain
                "+31 20 123 4567",      // Netherlands

                // EPP format examples (Extensible Provisioning Protocol)
                "+1.2345678901",
                "+44.2079460958",
                "+33.142868326",
                "+49.3012345678",
                "+81.312345678",
                "+86.1012345678",
                "+7.4951234567",
                "+61.212345678",
                "+55.1112345678",
                "+91.1112345678",
                "+39.0612345678",
                "+34.911234567",
                "+31.201234567",
                "+1.2345678901x123",     // with extension
                "+44.2079460958x456",    // with extension
                "+33.142868326x789"      // with extension
            );
    }
}

//////////////////////////////////////////////////////////////////////////////
