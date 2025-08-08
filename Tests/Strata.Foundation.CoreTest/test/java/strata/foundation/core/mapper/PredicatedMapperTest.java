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
    testGet()
    {
        PredicatedMapper<String,String> mapper =
            PredicatedMapper
                .of("input X",String.class)
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
                    input -> "mapped C");

                String expected = "mapped X";
                Optional<String> actual = mapper.get();

        assertTrue(actual.isPresent());
        assertEquals(expected,actual.get());

    }
    @Test
    public void
    testGetWhenEmpty()
    {
        PredicatedMapper<String,String> mapper =
            PredicatedMapper
                .of("input X",String.class)
                .addMapping(
                    input -> input.equals("input A"),
                    input -> "mapped A")
                .addMapping(
                    input -> input.equals("input B"),
                    input -> "mapped B")
                .addMapping(
                    input -> input.equals("input C"),
                    input -> "mapped C");

        Optional<String> actual = mapper.get();

        assertFalse(actual.isPresent());
    }

    @Test
    public void
    testMapInputFromConstructor()
    {
        PredicatedMapper<String,String> mapper =
            PredicatedMapper
                .of("input X",String.class)
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
                    input -> "mapped C");

        String expected = "mapped X";
        Optional<String> actual = mapper.map();

        assertTrue(actual.isPresent());
        assertEquals(expected,actual.get());

    }

    @Test
    public void
    testWhenEmptyInputFromConstructor()
    {
        PredicatedMapper<String,String> mapper =
            PredicatedMapper
                .of("input X",String.class)
                .addMapping(
                    input -> input.equals("input A"),
                    input -> "mapped A")
                .addMapping(
                    input -> input.equals("input B"),
                    input -> "mapped B")
                .addMapping(
                    input -> input.equals("input C"),
                    input -> "mapped C");

        Optional<String> actual = mapper.map();

        assertFalse(actual.isPresent());
    }

    @Test
    public void
    testMap()
    {
        PredicatedMapper<String,String> mapper =
            PredicatedMapper
                .of(String.class,String.class)
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
                    input -> "mapped C");

        String expected = "mapped X";
        Optional<String> actual = mapper.map("input X");

        assertTrue(actual.isPresent());
        assertEquals(expected,actual.get());

        mapper
            .map("inpup X")
            .ifPresent(actualValue -> assertEquals(expected,actualValue));
    }

    @Test
    public void
    testWhenEmpty()
    {
        PredicatedMapper<String,String> mapper =
            PredicatedMapper
                .of(String.class,String.class)
                .addMapping(
                    input -> input.equals("input A"),
                    input -> "mapped A")
                .addMapping(
                    input -> input.equals("input B"),
                    input -> "mapped B")
                .addMapping(
                    input -> input.equals("input C"),
                    input -> "mapped C");

        Optional<String> actual = mapper.map("input X");

        assertFalse(actual.isPresent());
    }

    @Test
    public void
    testConstructors()
    {
        PredicatedMapper<String,String> mapper1 =
            new PredicatedMapper<>();

        mapper1
            .addMapping(
                input -> input.equals("input A"),
                input -> "mapped A")
            .addMapping(
                input -> input.equals("input B"),
                input -> "mapped B")
            .addMapping(
                input -> input.equals("input C"),
                input -> "mapped C");

        assertNotNull(mapper1);

        PredicatedMapper<String,String> mapper2 =
            new PredicatedMapper<String,String>()
                .addMapping(
                    input -> input.equals("input A"),
                    input -> "mapped A")
                .addMapping(
                    input -> input.equals("input B"),
                    input -> "mapped B")
                .addMapping(
                    input -> input.equals("input C"),
                    input -> "mapped C");



        assertNotNull(mapper2);
    }


    @Test
    public void
    testOf()
    {
        PredicatedMapper<String,String> mapper1 =
            PredicatedMapper
                .<String,String>of()
                .addMapping(
                    input -> input.equals("input A"),
                    input -> "mapped A")
                .addMapping(
                    input -> input.equals("input B"),
                    input -> "mapped B")
                .addMapping(
                    input -> input.equals("input C"),
                    input -> "mapped C");

        assertNotNull(mapper1);

        PredicatedMapper<String,String> mapper2 =
            PredicatedMapper
                .<String,String>of("input X")
                .addMapping(
                    input -> input.equals("input A"),
                    input -> "mapped A")
                .addMapping(
                    input -> input.equals("input B"),
                    input -> "mapped B")
                .addMapping(
                    input -> input.equals("input C"),
                    input -> "mapped C");



        assertNotNull(mapper2);
    }

}

//////////////////////////////////////////////////////////////////////////////
