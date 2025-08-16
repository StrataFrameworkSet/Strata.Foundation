/// ///////////////////////////////////////////////////////////////////////////
// ExtendedOptionalTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class ExtendedOptionalTest
{
    @Test
    public void
    testConstructors()
    {
        ExtendedOptional<String> subject1 = new ExtendedOptional<>();
        ExtendedOptional<String> subject2 = new ExtendedOptional<>("subject2");

        try
        {
            subject1.get();
            fail("Expected an exception to be thrown");
        }
        catch (Exception e) {}

        assertEquals("subject2",subject2.get());

    }

    @Test
    public void
    testEmptyAndOf()
    {
        ExtendedOptional<String> subject1 = ExtendedOptional.empty();
        ExtendedOptional<String> subject2 = ExtendedOptional.of("subject2");

        try
        {
            subject1.get();
            fail("Expected an exception to be thrown");
        }
        catch (Exception e) {}

        assertEquals("subject2",subject2.get());

    }

    @Test
    public void
    testIfPresent()
    {
        ExtendedOptional<String> subject1 = ExtendedOptional.empty();
        ExtendedOptional<String> subject2 = ExtendedOptional.of("subject2");

        assertFalse(subject1.isPresent());
        assertTrue(subject1.isEmpty());
        subject1.ifPresent(value -> fail("Expected subject1 to be empty"));

        assertTrue(subject2.isPresent());
        assertFalse(subject2.isEmpty());
        subject2.ifPresent(value -> assertEquals("subject2", value));
    }

    @Test
    public void
    testIfPresentOrElse()
    {
        ExtendedOptional<String> subject1 = ExtendedOptional.empty();
        ExtendedOptional<String> subject2 = ExtendedOptional.of("subject2");

        assertFalse(subject1.isPresent());
        assertTrue(subject1.isEmpty());
        assertEquals(
            "subject 1 is empty",
            subject1.ifPresentOrElse(value -> value, () -> "subject 1 is empty"));

        assertTrue(subject2.isPresent());
        assertFalse(subject2.isEmpty());
        assertEquals(
            "subject2",
            subject2.ifPresentOrElse(value -> value, () -> "subject 2 is empty"));
    }

    @Test
    public void
    testIfPresentOrThrow()
    {
        ExtendedOptional<String> subject1 = ExtendedOptional.empty();
        ExtendedOptional<String> subject2 = ExtendedOptional.of("subject2");

        assertFalse(subject1.isPresent());
        assertTrue(subject1.isEmpty());
        assertThrows(
            NoSuchElementException.class,
            () -> subject1.ifPresentOrThrow(
                value -> value,
                new NoSuchElementException("subject 1 is empty")));

        assertTrue(subject2.isPresent());
        assertFalse(subject2.isEmpty());
        assertEquals(
            "subject2",
            subject2.ifPresentOrThrow(
                value -> value,
                new NoSuchElementException("subject 2 is empty")));
    }

    @Test
    public void
    testIfPresentOrElseNoReturn()
    {
        ExtendedOptional<String> subject1 = ExtendedOptional.empty();
        ExtendedOptional<String> subject2 = ExtendedOptional.of("subject2");

        assertFalse(subject1.isPresent());
        assertTrue(subject1.isEmpty());
        subject1.ifPresentOrElseNoReturn(
            value -> fail("subject 1 should be empty"),
            () -> System.out.println("subject 1 is empty"));

        assertTrue(subject2.isPresent());
        assertFalse(subject2.isEmpty());
        subject2.ifPresentOrElseNoReturn(
            value -> assertEquals("subject2",value),
            () -> {throw new IllegalStateException("subject 2 is empty");});
    }

    @Test
    public void
    testIfPresentOrThrowNoReturn()
    {
        ExtendedOptional<String> subject1 = ExtendedOptional.empty();
        ExtendedOptional<String> subject2 = ExtendedOptional.of("subject2");

        assertFalse(subject1.isPresent());
        assertTrue(subject1.isEmpty());
        assertThrows(
            NoSuchElementException.class,
            () -> subject1.ifPresentOrThrowNoReturn(
                value -> fail("subject 1 should be empty"),
                new NoSuchElementException("subject 1 is empty")));

        assertTrue(subject2.isPresent());
        assertFalse(subject2.isEmpty());
        subject2.ifPresentOrThrowNoReturn(
            value -> assertEquals("subject2",value),
            new IllegalStateException("subject 2 is empty"));
    }

    @Test
    public void
    testFilter()
    {
        ExtendedOptional<String> subject1 = ExtendedOptional.empty();
        ExtendedOptional<String> subject2 = ExtendedOptional.of("subject2");

        assertTrue(subject1.filter(value -> value.equals("subject1")).isEmpty());
        assertTrue(subject2.filter(value -> value.equals("subject1")).isEmpty());
        assertTrue(subject2.filter(value -> value.equals("subject2")).isPresent());
        assertEquals("subject2", subject2.filter(value -> value.equals("subject2")).get());
    }

    @Test
    public void
    testMap()
    {
        ExtendedOptional<String> subject1 = ExtendedOptional.empty();
        ExtendedOptional<String> subject2 = ExtendedOptional.of("subject2");

        assertTrue(subject1.map(value -> value.toUpperCase()).isEmpty());
        assertEquals(
            "SUBJECT2+extra",
            subject2
                .map(value -> value.toUpperCase())
                .map(value -> value + "+extra").get());

    }

    @Test
    public void
    testFlatMap()
    {
        ExtendedOptional<String> subject1 = ExtendedOptional.empty();
        ExtendedOptional<String> subject2 = ExtendedOptional.of("subject2");

        assertTrue(subject1.flatMap(value -> ExtendedOptional.of(value.toUpperCase())).isEmpty());
        assertEquals(
            "SUBJECT2+extra",
            subject2
                .flatMap(value -> ExtendedOptional.of(value.toUpperCase()))
                .flatMap(value -> ExtendedOptional.of(value + "+extra"))
                .get());

    }

    @Test
    public void
    testOr()
    {
        ExtendedOptional<String> subject1 = ExtendedOptional.empty();
        ExtendedOptional<String> subject2 = ExtendedOptional.of("subject2");

        assertTrue(
            subject1
                .or(() -> ExtendedOptional.of("default"))
                .isPresent());
        assertEquals(
            "default",
            subject1
                .or(() -> ExtendedOptional.of("default"))
                .get());

        assertTrue(
            subject2
                .or(() -> ExtendedOptional.of("default"))
                .isPresent());
        assertEquals(
            "subject2",
            subject2
                .or(() -> ExtendedOptional.of("default"))
                .get());

    }

    @Test
    public void
    testOrElse()
    {
        ExtendedOptional<String> subject1 = ExtendedOptional.empty();
        ExtendedOptional<String> subject2 = ExtendedOptional.of("subject2");

        assertEquals("default", subject1.orElse("default"));
        assertEquals("subject2", subject2.orElse("default"));

    }

    @Test
    public void
    testOrElseGet()
    {
        ExtendedOptional<String> subject1 = ExtendedOptional.empty();
        ExtendedOptional<String> subject2 = ExtendedOptional.of("subject2");

        assertEquals("default", subject1.orElseGet(() -> "default"));
        assertEquals("subject2", subject2.orElseGet(() -> "default"));

    }

    @Test
    public void
    testOrElseThrow()
    {
        ExtendedOptional<String> subject1 = ExtendedOptional.empty();
        ExtendedOptional<String> subject2 = ExtendedOptional.of("subject2");

        assertThrows(
            NoSuchElementException.class,
            () -> subject1.orElseThrow(
                () -> new NoSuchElementException("subject 1 is empty")));

        assertEquals("subject2", subject2.orElseThrow(
            () -> new NoSuchElementException("subject 2 is empty")));
    }
}

//////////////////////////////////////////////////////////////////////////////
