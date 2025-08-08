/// ///////////////////////////////////////////////////////////////////////////
// LimitedTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class ExpendableTest
{
    @Test
    public void
    testConstructors()
    {
        Expendable<String> subject1 = new Expendable<>();
        Expendable<String> subject2 = new Expendable<>("subject2");
        Expendable<String> subject3 = new Expendable<>("subject3",5);

        assertEquals(0, subject1.getAllowed());
        assertEquals(0,subject1.getRemaining());

        try
        {
            subject1.get();
            fail("Expected an exception to be thrown");
        }
        catch (Exception e) {}

        assertEquals(1, subject2.getAllowed());
        assertEquals(1,subject2.getRemaining());
        assertEquals("subject2",subject2.get());

        assertEquals(5, subject3.getAllowed());

        for (int i = 0; i < 5; i++)
        {
            System.out.println("i = " + i);
            assertEquals(5-i, subject3.getRemaining());
            assertEquals("subject3",subject3.get());
        }

        assertEquals(0, subject3.getRemaining());

        try
        {
            subject3.get();
            fail("Expected an exception to be thrown");
        }
        catch (Exception e) {}

    }

    @Test
    public void
    testEmptyAndOf()
    {
        Expendable<String> subject1 = Expendable.empty();
        Expendable<String> subject2 = Expendable.of("subject2");
        Expendable<String> subject3 = Expendable.of("subject3",5);

        assertEquals(0, subject1.getAllowed());
        assertEquals(0,subject1.getRemaining());

        try
        {
            subject1.get();
            fail("Expected an exception to be thrown");
        }
        catch (Exception e) {}

        assertEquals(1, subject2.getAllowed());
        assertEquals(1,subject2.getRemaining());
        assertEquals("subject2",subject2.get());

        assertEquals(5, subject3.getAllowed());

        for (int i = 0; i < 5; i++)
        {
            System.out.println("i = " + i);
            assertEquals(5-i, subject3.getRemaining());
            assertEquals("subject3",subject3.get());
        }

        assertEquals(0, subject3.getRemaining());

        try
        {
            subject3.get();
            fail("Expected an exception to be thrown");
        }
        catch (Exception e) {}

    }

    @Test
    public void
    testIfPresent()
    {
        Expendable<String> subject1 = Expendable.empty();
        Expendable<String> subject2 = Expendable.of("subject2");
        Expendable<String> subject3 = Expendable.of("subject3",5);

        assertFalse(subject1.isPresent());
        assertTrue(subject1.isEmpty());
        subject1.ifPresent(value -> fail("Expected subject1 to be empty"));

        assertTrue(subject2.isPresent());
        assertFalse(subject2.isEmpty());
        subject2.ifPresent(value -> assertEquals("subject2", value));
        assertFalse(subject2.isPresent());
        assertTrue(subject2.isEmpty());
        subject2.ifPresent(value -> fail("Expected subject2 to be empty"));

        for (int i = 0; i < 5; i++)
        {
            System.out.println("i = " + i);
            assertTrue(subject3.isPresent());
            assertFalse(subject3.isEmpty());
            subject3.ifPresent(value -> assertEquals("subject3", value));
        }

        assertFalse(subject3.isPresent());
        assertTrue(subject3.isEmpty());
        subject3.ifPresent(value -> fail("Expected subject3 to be empty"));

    }

    @Test
    public void
    testIfPresentOrElse()
    {
        Expendable<String> subject1 = Expendable.empty();
        Expendable<String> subject2 = Expendable.of("subject2");
        Expendable<String> subject3 = Expendable.of("subject3",5);

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
        assertFalse(subject2.isPresent());
        assertTrue(subject2.isEmpty());
       assertEquals(
            "subject 2 is empty",
            subject2.ifPresentOrElse(value -> value, () -> "subject 2 is empty"));

        for (int i = 0; i < 5; i++)
        {
            System.out.println("i = " + i);
            assertTrue(subject3.isPresent());
            assertFalse(subject3.isEmpty());
            assertEquals(
                "subject3",
                subject3.ifPresentOrElse(value -> value, () -> "subject 3 is empty"));
        }

        assertFalse(subject3.isPresent());
        assertTrue(subject3.isEmpty());
        assertEquals(
            "subject 3 is empty",
            subject3.ifPresentOrElse(value -> value, () -> "subject 3 is empty"));

    }

    @Test
    public void
    testIfPresentOrThrow()
    {
        Expendable<String> subject1 = Expendable.empty();
        Expendable<String> subject2 = Expendable.of("subject2");
        Expendable<String> subject3 = Expendable.of("subject3",5);

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

        assertFalse(subject2.isPresent());
        assertTrue(subject2.isEmpty());
        assertThrows(
            NoSuchElementException.class,
            () -> subject2.ifPresentOrThrow(
                value -> value,
                new NoSuchElementException("subject 2 is empty")));

        for (int i = 0; i < 5; i++)
        {
            System.out.println("i = " + i);
            assertTrue(subject3.isPresent());
            assertFalse(subject3.isEmpty());
            assertEquals(
                "subject3",
                subject3.ifPresentOrThrow(
                    value -> value,
                    new NoSuchElementException("subject 3 is empty")));
        }

        assertFalse(subject3.isPresent());
        assertTrue(subject3.isEmpty());
        assertThrows(
            NoSuchElementException.class,
            () -> subject3.ifPresentOrThrow(
                value -> value,
                new NoSuchElementException("subject 3 is empty")));

    }

    @Test
    public void
    testIfPresentOrElseNoReturn()
    {
        Expendable<String> subject1 = Expendable.empty();
        Expendable<String> subject2 = Expendable.of("subject2");
        Expendable<String> subject3 = Expendable.of("subject3",5);

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
        assertFalse(subject2.isPresent());
        assertTrue(subject2.isEmpty());
        subject2.ifPresentOrElseNoReturn(
            value -> fail("subject 2 is empty"),
            () -> System.out.println("subject 2 is empty"));

        for (int i = 0; i < 5; i++)
        {
            System.out.println("i = " + i);
            assertTrue(subject3.isPresent());
            assertFalse(subject3.isEmpty());
            subject3.ifPresentOrElseNoReturn(
                value -> assertEquals("subject3",value),
                () -> {throw new IllegalStateException("subject 3 is empty");});
        }

        assertFalse(subject3.isPresent());
        assertTrue(subject3.isEmpty());
        subject3.ifPresentOrElseNoReturn(
            value -> fail("subject 3 is empty"),
            () -> System.out.println("subject 3 is empty"));

    }

    @Test
    public void
    testIfPresentOrThrowNoReturn()
    {
        Expendable<String> subject1 = Expendable.empty();
        Expendable<String> subject2 = Expendable.of("subject2");
        Expendable<String> subject3 = Expendable.of("subject3",5);

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

        assertFalse(subject2.isPresent());
        assertTrue(subject2.isEmpty());
        assertThrows(
            NoSuchElementException.class,
            () -> subject2.ifPresentOrThrowNoReturn(
                value -> fail("subject 2 is empty"),
                new NoSuchElementException("subject 2 is empty")));

        for (int i = 0; i < 5; i++)
        {
            System.out.println("i = " + i);
            assertTrue(subject3.isPresent());
            assertFalse(subject3.isEmpty());
            subject3.ifPresentOrThrowNoReturn(
                value -> assertEquals("subject3",value),
                new IllegalStateException("subject 3 is empty"));
        }

        assertFalse(subject3.isPresent());
        assertTrue(subject3.isEmpty());
        assertThrows(
            NoSuchElementException.class,
            () -> subject3.ifPresentOrThrowNoReturn(
                value -> fail("subject 3 is empty"),
                new NoSuchElementException("subject 3 is empty")));
    }

    @Test
    public void
    testFilter()
    {
        Expendable<String> subject1 = Expendable.empty();
        Expendable<String> subject2 = Expendable.of("subject2");
        Expendable<String> subject3 = Expendable.of("subject3",5);

        assertTrue(subject1.filter(value -> value.equals("subject1")).isEmpty());
        assertTrue(subject2.filter(value -> value.equals("subject1")).isEmpty());
        assertTrue(subject2.filter(value -> value.equals("subject2")).isPresent());
        assertEquals("subject2", subject2.filter(value -> value.equals("subject2")).get());

        for (int i = 0; i < 5; i++)
        {
            System.out.println("i = " + i);
            assertTrue(subject3.filter(value -> value.equals("subject1")).isEmpty());
            assertTrue(subject3.filter(value -> value.equals("subject3")).isPresent());
            assertEquals("subject3", subject3.filter(value -> value.equals("subject3")).get());
        }
    }

    @Test
    public void
    testMap()
    {
        Expendable<String> subject1 = Expendable.empty();
        Expendable<String> subject2 = Expendable.of("subject2");
        Expendable<String> subject3 = Expendable.of("subject3",5);

        assertTrue(subject1.map(value -> value.toUpperCase()).isEmpty());
        assertEquals(
            "SUBJECT2+extra",
            subject2
                .map(value -> value.toUpperCase())
                .map(value -> value + "+extra").get());
        assertTrue(subject2.isEmpty());


        for (int i = 0; i < 5; i++)
        {
            System.out.println("i = " + i);
            assertEquals("SUBJECT3", subject3.map(value -> value.toUpperCase()).get());
        }

        assertTrue(subject3.isEmpty());

    }

    @Test
    public void
    testFlatMap()
    {
        Expendable<String> subject1 = Expendable.empty();
        Expendable<String> subject2 = Expendable.of("subject2");
        Expendable<String> subject3 = Expendable.of("subject3",5);

        assertTrue(subject1.flatMap(value -> Expendable.of(value.toUpperCase())).isEmpty());
        assertEquals(
            "SUBJECT2+extra",
            subject2
                .flatMap(value -> Expendable.of(value.toUpperCase()))
                .flatMap(value -> Expendable.of(value + "+extra"))
                .get());
        assertTrue(subject2.isEmpty());

        for (int i = 0; i < 5; i++)
        {
            System.out.println("i = " + i);
            assertEquals("SUBJECT3", subject3.flatMap(value -> Expendable.of(value.toUpperCase())).get());
        }

        assertTrue(subject3.isEmpty());
    }

    @Test
    public void
    testOrElse()
    {
        Expendable<String> subject1 = Expendable.empty();
        Expendable<String> subject2 = Expendable.of("subject2");
        Expendable<String> subject3 = Expendable.of("subject3",5);

        assertEquals("default", subject1.orElse("default"));
        assertEquals("subject2", subject2.orElse("default"));
        assertEquals("default", subject2.orElse("default"));

        for (int i = 0; i < 5; i++)
        {
            System.out.println("i = " + i);
            assertEquals("subject3", subject3.orElse("default"));
        }

        assertEquals("default", subject3.orElse("default"));

    }

    @Test
    public void
    testOrElseGet()
    {
        Expendable<String> subject1 = Expendable.empty();
        Expendable<String> subject2 = Expendable.of("subject2");
        Expendable<String> subject3 = Expendable.of("subject3",5);

        assertEquals("default", subject1.orElseGet(() -> "default"));
        assertEquals("subject2", subject2.orElseGet(() -> "default"));
        assertEquals("default", subject2.orElseGet(() -> "default"));

        for (int i = 0; i < 5; i++)
        {
            System.out.println("i = " + i);
            assertEquals("subject3", subject3.orElseGet(() -> "default"));
        }

        assertEquals("default", subject3.orElseGet(() -> "default"));

    }

    @Test
    public void
    testOrElseThrow()
    {
        Expendable<String> subject1 = Expendable.empty();
        Expendable<String> subject2 = Expendable.of("subject2");
        Expendable<String> subject3 = Expendable.of("subject3",5);

        assertThrows(
            NoSuchElementException.class,
            () -> subject1.orElseThrow(
                () -> new NoSuchElementException("subject 1 is empty")));

        assertEquals("subject2", subject2.orElseThrow(
            () -> new NoSuchElementException("subject 2 is empty")));

        assertThrows(
            NoSuchElementException.class,
            () -> subject2.orElseThrow(
                () -> new NoSuchElementException("subject 2 is empty")));

        for (int i = 0; i < 5; i++)
        {
            System.out.println("i = " + i);
            assertEquals("subject3", subject3.orElseThrow(
                () -> new NoSuchElementException("subject 3 is empty")));
        }

        assertThrows(
            NoSuchElementException.class,
            () -> subject3.orElseThrow(
                () -> new NoSuchElementException("subject 3 is empty")));
    }

    @Test
    public void
    testIsExpended()
    {
        Expendable<String> subject1 = Expendable.empty();
        Expendable<String> subject2 = Expendable.of("subject2");
        Expendable<String> subject3 = Expendable.of("subject3",5);

        assertTrue(subject1.isExpended());
        assertFalse(subject2.isExpended());
        subject2.get();
        assertTrue(subject2.isExpended());

        for (int i = 0; i < 5; i++)
        {
            System.out.println("i = " + i);
            assertFalse(subject3.isExpended());
            subject3.get();
        }

        assertTrue(subject3.isExpended());
    }
}

//////////////////////////////////////////////////////////////////////////////
