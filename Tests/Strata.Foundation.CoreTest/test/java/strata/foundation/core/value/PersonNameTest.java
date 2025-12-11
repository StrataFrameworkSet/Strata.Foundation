//////////////////////////////////////////////////////////////////////////////
// PhoneNumberTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import strata.foundation.core.collection.Quadruple;
import strata.foundation.core.mapper.ObjectMapperSupplier;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Tag("CommitStage")
public
class PersonNameTest
{

    public static Stream<Arguments>
    valid()
    {
        return
            Stream.of(
                // All fields present
                Arguments.of("Dr","John","Friedrich","Liebenau","Jr"),
                Arguments.of("Prof","Sarah","Marie","Thompson","III"),
                Arguments.of("Mr","Robert","Lee","Wilson","Sr"),

                // No title, no suffix
                Arguments.of(null,"John","Friedrich","Liebenau",null),
                Arguments.of(null,"Emily","Rose","Johnson",null),
                Arguments.of(null,"Michael","James","Chen",null),

                // Title, no suffix
                Arguments.of("Dr","Ayham",null,"Al-Zoebi",null),
                Arguments.of("Ms","Jennifer","Lynn","Rodriguez",null),
                Arguments.of("Rev","Thomas","Edward","Jones",null),

                // No title, with suffix
                Arguments.of(null,"Martin","Luther","King","Jr"),
                Arguments.of(null,"Henry","James","Johnson","III"),
                Arguments.of(null,"Elizabeth",null,"Smith","PhD"),

                // Title and suffix, no middle name
                Arguments.of("Dr","Richard",null,"Wilson","MD"),
                Arguments.of("Mrs","Susan",null,"Taylor","Esq"),
                Arguments.of("Rev","William",null,"Brown","PhD"),

                // No middle name, no suffix
                Arguments.of("Dr","John",null,"Doe",null),
                Arguments.of("Ms","Sarah",null,"Thompson",null),
                Arguments.of(null,"Robert",null,"Williams",null),

                // No middle name, no title
                Arguments.of(null,"Alex",null,"Morgan",null),
                Arguments.of(null,"Elton",null,"John",null),

                // Only first and last name (minimum required)
                Arguments.of(null,"John",null,"Smith",null),
                Arguments.of(null,"Jane",null,"Doe",null),

                // Empty/whitespace strings (treated as null)
                Arguments.of("","Aghyan","\t\r\n","Al-Zuabi"," "),
                Arguments.of(" ","Maria","\n","Garcia"," \t"),
                Arguments.of("\t","David","  ","Miller","\r\n"),

                // Hyphenated names
                Arguments.of("Dr","Maria","Elena","Garcia-Rodriguez","PhD"),
                Arguments.of(null,"Jean","Paul","Dubois-Martin",null),

                // Names with apostrophes
                Arguments.of(null,"Patrick","Joseph","O'Brien",null),
                Arguments.of("Dr","Mary","Anne","D'Angelo","MD"),

                // International names
                Arguments.of("Prof","Zhang",null,"Wei",null),
                Arguments.of(null,"Mohammed","bin","Abdullah",null),
                Arguments.of("Dr","José","María","López","PhD"),

                // Multiple suffixes
                Arguments.of("Dr","Richard","Lee","Wilson","PhD, MD"),
                Arguments.of("Prof","James","Robert","Oppenheimer","ScD"),

                // Unusual but valid titles
                Arguments.of("Mx","Alex",null,"Morgan",null),
                Arguments.of("Sir","Elton","Hercules","John",null),
                Arguments.of("Hon","Patricia","Marie","Washington",null),
                Arguments.of("Capt","James","T","Kirk",null),
                Arguments.of("Gen","Douglas",null,"MacArthur",null),

                // Single letter middle names/initials
                Arguments.of("Prof","J","Robert","Oppenheimer",null),
                Arguments.of(null,"Harry","S","Truman",null),
                Arguments.of("Dr","John","Q","Public","MD"),

                // Long names
                Arguments.of("Dr","Christopher","Alexander","Montgomery-Smythe","III"),
                Arguments.of("Prof","Elizabeth","Catherine","Wentworth-Fitzwilliam","PhD"),

                // Short names
                Arguments.of(null,"Li",null,"Wu",null),
                Arguments.of("Dr","Bo",null,"Xi","MD"),

                // Names with spaces (edge case - should be trimmed)
                Arguments.of(" Dr ","John"," Friedrich ","Liebenau"," Jr "),

                // Mixed case (should be preserved)
                Arguments.of("DR","JOHN","FRIEDRICH","LIEBENAU","JR"),
                Arguments.of("dr","john","friedrich","liebenau","jr")
            );
    }


    public static Stream<Quadruple<String,String,String,String>>
    invalid()
    {
        return
            Stream.of(
                Quadruple.of(null,null,"Friedrich","Liebenau"),
                Quadruple.of("Dr","Ayham",null,null),
                Quadruple.of(null,null,null,null));
    }

    @ParameterizedTest
    @MethodSource("valid")
    public void
    testValidInput(
        String title,
        String firstName,
        String middleName,
        String lastName,
        String suffix)
    {
        new PersonNameBuilder()
            .setTitle(title)
            .setFirstName(firstName)
            .setMiddleName(middleName)
            .setLastName(lastName)
            .setSuffix(suffix)
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
    testMapping(
        String title,
        String firstName,
        String middleName,
        String lastName,
        String suffix) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();
        PersonName  expected = buildPersonName(title,firstName,middleName,lastName,suffix);
        PersonName  actual =
            mapper.readValue(
                mapper.writeValueAsString(expected),PersonName.class);

        assertEquals(expected,actual);
    }

    private static PersonName
    buildPersonName(
        String title,
        String firstName,
        String middleName,
        String lastName,
        String suffix)
    {
        return
            new PersonNameBuilder()
                .setTitle(title)
                .setFirstName(firstName)
                .setMiddleName(middleName)
                .setLastName(lastName)
                .setSuffix(suffix)
                .build();
    }
}

//////////////////////////////////////////////////////////////////////////////
