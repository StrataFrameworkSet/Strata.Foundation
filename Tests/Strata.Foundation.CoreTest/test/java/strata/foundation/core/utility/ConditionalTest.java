//////////////////////////////////////////////////////////////////////////////
// ConditionalTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class ConditionalTest
{
    private Conditional trueCondition;
    private Conditional falseCondition;

    @BeforeEach
    public void
    setUp()
    {
        trueCondition = Conditional.of(true);
        falseCondition = Conditional.of(false);
    }

    @Test
    public void
    testIfTrueSupplier()
    {
        String expected = "condition is true";

        trueCondition
            .ifTrue(() -> expected)
            .ifPresentOrElse(
                actual -> assertEquals(expected,actual),
                () -> {throw new NoSuchElementException();});
        falseCondition
            .ifTrue(() -> expected)
            .ifPresentOrElse(
                actual -> {throw new IllegalStateException();},
                () -> {});
    }

    @Test
    public void
    testIfFalseSupplier()
    {
        String expected = "condition is true";

        falseCondition
            .ifFalse(() -> expected)
            .ifPresentOrElse(
                actual -> assertEquals(expected,actual),
                () -> {throw new NoSuchElementException();});
        trueCondition
            .ifFalse(() -> expected)
            .ifPresentOrElse(
                actual -> {throw new IllegalStateException();},
                () -> {});
    }

    @Test
    public void
    testIfTrueRunnable()
    {
        String         expected = "condition is true";
        Holder<String> actual = new Holder<>();

        trueCondition
            .ifTrue(() -> {actual.setItem(expected);});

        assertEquals(expected,actual.getItem());

        falseCondition
            .ifTrue(() -> {throw new IllegalStateException();});
    }

    @Test
    public void
    testIfFalseRunnable()
    {
        String         expected = "condition is true";
        Holder<String> actual = new Holder<>();

        falseCondition
            .ifFalse(() -> {actual.setItem(expected);});

        assertEquals(expected,actual.getItem());

        trueCondition
            .ifFalse(() -> {throw new IllegalStateException();});
    }

    @Test
    public void
    testIfTrueOrElseSupplier()
    {
        String expected = "condition is true";
        String error    = "error";

        assertEquals(
            expected,
            trueCondition.ifTrueOrElse(() -> expected,() -> error));

        assertEquals(
            expected,
            falseCondition.ifTrueOrElse(() -> error,() -> expected));

    }

    @Test
    public void
    testIfTrueOrElseRunnable()
    {
        trueCondition.ifTrueOrElse(
            () -> {},
            () -> {throw new IllegalStateException();});

        falseCondition.ifTrueOrElse(
            () -> {throw new IllegalStateException();},
            () -> {});

    }

    @Test
    public void
    testIfTrueOrThrowSupplier() throws Throwable
    {
        String expected = "condition is true";

        assertEquals(
            expected,
            trueCondition.ifTrueOrThrow(
                () -> expected,
                new IllegalStateException()));

        try
        {
            falseCondition.ifTrueOrThrow(
                () -> expected,
                new IllegalStateException());

            fail("Should have thrown an exception");
        }
        catch (IllegalStateException e) {}
    }

    @Test
    public void
    testIfTrueOrThrowRunnable() throws Throwable
    {
        trueCondition.ifTrueOrThrow(
            () -> {},
            new IllegalStateException());

        try
        {
            falseCondition.ifTrueOrThrow(
                () -> {},
                new IllegalStateException());

            fail("Should have thrown an exception");
        }
        catch (IllegalStateException e) {}
    }

    @Test
    public void
    testAnd()
    {
        Conditional andTrue   = trueCondition.and(trueCondition);
        Conditional andFalse1 = falseCondition.and(falseCondition);
        Conditional andFalse2 = trueCondition.and(falseCondition);
        Conditional andFalse3 = falseCondition.and(trueCondition);

        assertTrue(andTrue.get());
        assertFalse(andFalse1.get());
        assertFalse(andFalse2.get());
        assertFalse(andFalse3.get());
    }

    @Test
    public void
    testOr()
    {
        Conditional orTrue1  = trueCondition.or(trueCondition);
        Conditional orTrue2  = trueCondition.or(falseCondition);
        Conditional orTrue3  = falseCondition.or(trueCondition);
        Conditional orFalse  = falseCondition.or(falseCondition);

        assertTrue(orTrue1.get());
        assertTrue(orTrue2.get());
        assertTrue(orTrue3.get());
        assertFalse(orFalse.get());
    }

    @Test
    public void
    testXor()
    {
        Conditional xorTrue1  = trueCondition.xor(falseCondition);
        Conditional xorTrue2  = falseCondition.xor(trueCondition);
        Conditional xorFalse1 = trueCondition.xor(trueCondition);
        Conditional xorFalse2 = falseCondition.xor(falseCondition);

        assertTrue(xorTrue1.get());
        assertTrue(xorTrue2.get());
        assertFalse(xorFalse1.get());
        assertFalse(xorFalse2.get());
    }

    @Test
    public void
    testNot()
    {
        Conditional isTrue  = falseCondition.not();
        Conditional isFalse = trueCondition.not();

        assertTrue(isTrue.get());
        assertFalse(isFalse.get());
    }

    @Test
    public void
    testOf()
    {
        Conditional isTrue = Conditional.of(fooTrue());
        Conditional isFalse = Conditional.of(fooFalse());

        assertTrue(isTrue.get());
        assertFalse(isFalse.get());

        assertEquals(Conditional.TRUE, isTrue);
        assertEquals(Conditional.FALSE, isFalse);
    }

    @Test
    public void
    testUsage()
    {
        assertEquals(
            "THIS IS TRUE",
            Conditional
                .of(fooTrue())
                .ifTrueOrElse(
                    () -> "this is true",
                    () -> "this is false")
                .toUpperCase());
        assertEquals(
            "THIS IS FALSE",
            Conditional
                .of(fooFalse())
                .ifTrueOrElse(
                    () -> "this is true",
                    () -> "this is false")
                .toUpperCase());
    }

    private boolean
    fooTrue() { return true; }

    private Boolean
    fooFalse() { return false; }
}

//////////////////////////////////////////////////////////////////////////////
