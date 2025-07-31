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
        PredicatedMapper<String,String> subject =
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
                Optional<String> actual = subject.get();

        assertTrue(actual.isPresent());
        assertEquals(expected,actual.get());

    }
    @Test
    public void
    testGetWhenEmpty()
    {
        PredicatedMapper<String,String> subject =
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

        Optional<String> actual = subject.get();

        assertFalse(actual.isPresent());
    }

    @Test
    public void
    testMapInputFromConstructor()
    {
        PredicatedMapper<String,String> subject =
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
        Optional<String> actual = subject.map();

        assertTrue(actual.isPresent());
        assertEquals(expected,actual.get());

    }

    @Test
    public void
    testWhenEmptyInputFromConstructor()
    {
        PredicatedMapper<String,String> subject =
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

        Optional<String> actual = subject.map();

        assertFalse(actual.isPresent());
    }

    @Test
    public void
    testMap()
    {
        PredicatedMapper<String,String> subject =
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
        Optional<String> actual = subject.map("input X");

        assertTrue(actual.isPresent());
        assertEquals(expected,actual.get());

    }

    @Test
    public void
    testWhenEmpty()
    {
        PredicatedMapper<String,String> subject =
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

        Optional<String> actual = subject.map("input X");

        assertFalse(actual.isPresent());
    }

}

//////////////////////////////////////////////////////////////////////////////
