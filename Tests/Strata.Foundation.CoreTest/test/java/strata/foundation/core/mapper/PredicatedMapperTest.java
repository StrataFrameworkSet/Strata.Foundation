/// ///////////////////////////////////////////////////////////////////////////
// PredicatedMapperTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class PredicatedMapperTest
{
    @Test
    public void
    testMap()
    {
        PredicatedMapper mapper =
            new PredicatedMapper()
                .beginTypeMap(String.class,String.class)
                .addMapping(
                    input -> input.equals("input A"),
                    input -> "mapped A")
                .addMapping(
                    input -> input.equals("input B"),
                    input -> "mapped B")
                .addMapping(
                    input -> input.equals("input X"),
                    input -> "mapped X")
                .addMapping(
                    input -> input.equals("input C"),
                    input -> "mapped C")
                .beginTypeMap(Long.class,String.class)
                .addMapping(
                    input -> input.equals(7L),
                    input -> "mapped 7")
                .toMapper();

        String expected = "mapped X";
        Optional<String> actual = mapper.map("input X",String.class);

        assertTrue(actual.isPresent());
        assertEquals(expected,actual.get());

        mapper
            .map("inpup X", String.class)
            .ifPresent(actualValue -> assertEquals(expected,actualValue));

        assertTrue(mapper.map(7L, String.class).isPresent());
        mapper
            .map(7L, String.class)
            .ifPresent(actualValue -> assertEquals("mapped 7",actualValue));
    }

    @Test
    public void
    testWhenEmpty()
    {
        PredicatedMapper mapper =
            new PredicatedMapper()
                .beginTypeMap(String.class,String.class)
                .addMapping(
                    input -> input.equals("input A"),
                    input -> "mapped A")
                .addMapping(
                    input -> input.equals("input B"),
                    input -> "mapped B")
                .addMapping(
                    input -> input.equals("input C"),
                    input -> "mapped C")
                .toMapper();

        Optional<String> actual = mapper.map("input X", String.class);

        assertFalse(actual.isPresent());
    }

    @Test
    public void
    testMapWithIncorrectInputType()
    {
        PredicatedMapper mapper =
            new PredicatedMapper()
                .beginTypeMap(String.class,String.class)
                .addMapping(
                    input -> input.equals("input A"),
                    input -> "mapped A")
                .addMapping(
                    input -> input.equals("input B"),
                    input -> "mapped B")
                .addMapping(
                    input -> input.equals("input X"),
                    input -> "mapped X")
                .addMapping(
                    input -> input.equals("input C"),
                    input -> "mapped C")
                .toMapper();

        String expected = "mapped X";
        Optional<String> actual = mapper.map(7L, String.class);

        assertFalse(actual.isPresent());
    }

    @Test
    public void
    testMapWithIncorrectOutputType()
    {
        PredicatedMapper mapper =
            new PredicatedMapper()
                .beginTypeMap(String.class,String.class)
                .addMapping(
                    input -> input.equals("input A"),
                    input -> "mapped A")
                .addMapping(
                    input -> input.equals("input B"),
                    input -> "mapped B")
                .addMapping(
                    input -> input.equals("input X"),
                    input -> "mapped X")
                .addMapping(
                    input -> input.equals("input C"),
                    input -> "mapped C")
                .toMapper();

        String expected = "mapped X";
        Optional<Long> actual = mapper.map("input X",Long.class );

        assertFalse(actual.isPresent());

    }

}

//////////////////////////////////////////////////////////////////////////////
