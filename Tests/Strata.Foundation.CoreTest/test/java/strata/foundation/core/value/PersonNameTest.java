//////////////////////////////////////////////////////////////////////////////
// PhoneNumberTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import strata.foundation.core.container.Quadruple;
import strata.foundation.core.mapper.ObjectMapperSupplier;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Tag("CommitStage")
public
class PersonNameTest
{

    public static Stream<Quadruple<String,String,String,String>>
    valid()
    {
        return
            Stream.of(
                Quadruple.create(null,"John","Friedrich","Liebenau"),
                Quadruple.create("Dr","Ayham",null,"Al-Zoebi"),
                Quadruple.create(null,"Aghyan",null,"Al-Zuabi"));
    }


    public static Stream<Quadruple<String,String,String,String>>
    invalid()
    {
        return
            Stream.of(
                Quadruple.create(null,null,"Friedrich","Liebenau"),
                Quadruple.create("Dr","Ayham",null,null),
                Quadruple.create(null,null,null,null));
    }

    @ParameterizedTest
    @MethodSource("valid")
    public void
    testValidInput(Quadruple<String,String,String,String> input)
    {
        new PersonNameBuilder()
            .setTitle(input.getFirst())
            .setFirstName(input.getSecond())
            .setMiddleName(input.getThird())
            .setLastName(input.getFourth())
            .build();
    }


    @ParameterizedTest
    @MethodSource("invalid")
    public void
    testInvalidInput(Quadruple<String,String,String,String> input)
    {
        try
        {
            new PersonNameBuilder()
                .setTitle(input.getFirst())
                .setFirstName(input.getSecond())
                .setMiddleName(input.getThird())
                .setLastName(input.getFourth())
                .build();
            fail("Should have thrown exception: " + input + " is invalid");
        }
        catch (Exception e) {}
    }


    @ParameterizedTest
    @MethodSource("valid")
    public void
    testMapping(Quadruple<String,String,String,String> input) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();
        PersonName  expected = buildPersonName(input);
        PersonName  actual =
            mapper.readValue(
                mapper.writeValueAsString(expected),PersonName.class);

        assertEquals(expected,actual);
    }

    private static PersonName
    buildPersonName(Quadruple<String,String,String,String> input)
    {
        return
            new PersonNameBuilder()
                .setTitle(input.getFirst())
                .setFirstName(input.getSecond())
                .setMiddleName(input.getThird())
                .setLastName(input.getFourth())
                .build();
    }
}

//////////////////////////////////////////////////////////////////////////////
