//////////////////////////////////////////////////////////////////////////////
// PhoneNumberTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("CommitStage")
public
class PhoneNumberTest
{

    public static Stream<String>
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
                "1.235.555.1234",
                "1 235 555 1234",
                "1(235)5551234",
                "1(235)555-1234",
                "1(235)555.1234",
                "1 (235) 555 1234");
    }

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
}

//////////////////////////////////////////////////////////////////////////////
