//////////////////////////////////////////////////////////////////////////////
// ValidateTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("CommitStage")
public
class ValidateTest
{
    @Test
    public void
    testTrueConditionDoesNotThrow()
    {
        assertDoesNotThrow(
            () -> Validate.require(true,() -> new IllegalStateException("x")));
    }

    @Test
    public void
    testTrueConditionDoesNotInvokeSupplier()
    {
        AtomicBoolean invoked = new AtomicBoolean(false);

        Validate.require(
            true,
            () ->
            {
                invoked.set(true);
                return new IllegalStateException("x");
            });

        assertFalse(invoked.get());
    }

    @Test
    public void
    testFalseConditionThrowsSuppliedInstance()
    {
        IllegalArgumentException expected = new IllegalArgumentException("bad");
        IllegalArgumentException actual =
            assertThrows(
                IllegalArgumentException.class,
                () -> Validate.require(false,() -> expected));

        assertSame(expected,actual);
    }

    @Test
    public void
    testFalseConditionInvokesSupplier()
    {
        AtomicBoolean invoked = new AtomicBoolean(false);

        assertThrows(
            IllegalStateException.class,
            () -> Validate.require(
                false,
                () ->
                {
                    invoked.set(true);
                    return new IllegalStateException("x");
                }));

        assertTrue(invoked.get());
    }

    @Test
    public void
    testCheckedExceptionPropagates()
    {
        IOException expected = new IOException("io");
        IOException actual =
            assertThrows(
                IOException.class,
                () -> Validate.require(false,() -> expected));

        assertSame(expected,actual);
    }

    @Test
    public void
    testUncheckedExceptionPropagates()
    {
        UnsupportedOperationException expected =
            new UnsupportedOperationException("nope");
        UnsupportedOperationException actual =
            assertThrows(
                UnsupportedOperationException.class,
                () -> Validate.require(false,() -> expected));

        assertSame(expected,actual);
    }

    @Test
    public void
    testRequireNonNullReturnsValue()
    {
        String value = new String("value");

        assertSame(value,Validate.requireNonNull(value));
        assertSame(value,Validate.requireNonNull(value,"message"));
    }

    @Test
    public void
    testRequireNonNullThrowsOnNull()
    {
        assertThrows(
            NullPointerException.class,
            () -> Validate.requireNonNull(null));
    }

    @Test
    public void
    testRequireNonNullWithMessageThrowsOnNull()
    {
        NullPointerException actual =
            assertThrows(
                NullPointerException.class,
                () -> Validate.requireNonNull(null,"value is null"));

        assertEquals("value is null",actual.getMessage());
    }

    @Test
    public void
    testRequireNonNullWithSupplierReturnsValue()
    {
        AtomicBoolean invoked = new AtomicBoolean(false);
        String        value = new String("value");
        String        actual =
            Validate.requireNonNull(
                value,
                () ->
                {
                    invoked.set(true);
                    return new IllegalStateException("x");
                });

        assertSame(value,actual);
        assertFalse(invoked.get());
    }

    @Test
    public void
    testRequireNonNullWithSupplierThrowsSuppliedInstance()
    {
        IllegalStateException expected = new IllegalStateException("null");
        IllegalStateException actual =
            assertThrows(
                IllegalStateException.class,
                () -> Validate.requireNonNull(null,() -> expected));

        assertSame(expected,actual);
    }

    @Test
    public void
    testRequireNonNullWithSupplierThrowsCheckedException()
    {
        IOException expected = new IOException("io");
        IOException actual =
            assertThrows(
                IOException.class,
                () -> Validate.requireNonNull(null,() -> expected));

        assertSame(expected,actual);
    }

    @Test
    public void
    testRequireNonNullElse()
    {
        String value = new String("value");
        String fallback = new String("fallback");

        assertSame(value,Validate.requireNonNullElse(value,fallback));
        assertSame(fallback,Validate.requireNonNullElse(null,fallback));
        assertThrows(
            NullPointerException.class,
            () -> Validate.requireNonNullElse(null,null));
    }

    @Test
    public void
    testRequireNonNullElseWithSupplier()
    {
        AtomicBoolean invoked = new AtomicBoolean(false);
        String        value = new String("value");
        String        fallback = new String("fallback");
        Supplier<IllegalStateException> supplier =
            () ->
            {
                invoked.set(true);
                return new IllegalStateException("x");
            };

        assertSame(value,Validate.requireNonNullElse(value,fallback,supplier));
        assertSame(value,Validate.requireNonNullElse(value,null,supplier));
        assertSame(fallback,Validate.requireNonNullElse(null,fallback,supplier));
        assertFalse(invoked.get());
    }

    @Test
    public void
    testRequireNonNullElseWithSupplierThrowsSuppliedInstance()
    {
        IOException expected = new IOException("io");
        IOException actual =
            assertThrows(
                IOException.class,
                () -> Validate.requireNonNullElse(
                    (String)null,
                    (String)null,
                    () -> expected));

        assertSame(expected,actual);
    }

    @Test
    public void
    testRequireNonNullElseGet()
    {
        AtomicBoolean invoked = new AtomicBoolean(false);
        String        value = new String("value");
        String        fallback = new String("fallback");

        assertSame(
            value,
            Validate.requireNonNullElseGet(
                value,
                () ->
                {
                    invoked.set(true);
                    return fallback;
                }));
        assertFalse(invoked.get());
        assertSame(fallback,Validate.requireNonNullElseGet(null,() -> fallback));
        assertThrows(
            NullPointerException.class,
            () -> Validate.requireNonNullElseGet(null,() -> null));
        assertThrows(
            NullPointerException.class,
            () -> Validate.requireNonNullElseGet(null,null));
    }

    @Test
    public void
    testRequireNonNullElseGetWithSupplier()
    {
        AtomicBoolean defaultInvoked = new AtomicBoolean(false);
        AtomicBoolean invoked = new AtomicBoolean(false);
        String        value = new String("value");
        String        fallback = new String("fallback");
        Supplier<String> defaultSupplier =
            () ->
            {
                defaultInvoked.set(true);
                return fallback;
            };
        Supplier<IllegalStateException> supplier =
            () ->
            {
                invoked.set(true);
                return new IllegalStateException("x");
            };

        assertSame(value,Validate.requireNonNullElseGet(value,defaultSupplier,supplier));
        assertFalse(defaultInvoked.get());
        assertSame(fallback,Validate.requireNonNullElseGet(null,defaultSupplier,supplier));
        assertTrue(defaultInvoked.get());
        assertFalse(invoked.get());
    }

    @Test
    public void
    testRequireNonNullElseGetWithSupplierThrowsSuppliedInstance()
    {
        IOException expected = new IOException("io");
        IOException actual =
            assertThrows(
                IOException.class,
                () -> Validate.requireNonNullElseGet(
                    (String)null,
                    () -> null,
                    () -> expected));

        assertSame(expected,actual);
    }

    @Test
    public void
    testRequireNonNullElseGetWithSupplierRejectsNullDefaultSupplier()
    {
        NullPointerException actual =
            assertThrows(
                NullPointerException.class,
                () -> Validate.requireNonNullElseGet(
                    (String)null,
                    null,
                    () -> new IllegalStateException("x")));

        assertEquals("default supplier is null",actual.getMessage());
    }

    @Test
    public void
    testRequireRejectsNullSupplierWhenConditionTrue()
    {
        NullPointerException actual =
            assertThrows(
                NullPointerException.class,
                () -> Validate.require(true,(Supplier<IllegalStateException>)null));

        assertEquals("supplier is null",actual.getMessage());
    }

    @Test
    public void
    testRequireNonNullRejectsNullSupplierWhenValuePresent()
    {
        NullPointerException actual =
            assertThrows(
                NullPointerException.class,
                () -> Validate.requireNonNull(
                    "value",
                    (Supplier<IllegalStateException>)null));

        assertEquals("supplier is null",actual.getMessage());
    }

    @Test
    public void
    testRequireNonNullElseRejectsNullSupplierWhenValuePresent()
    {
        NullPointerException actual =
            assertThrows(
                NullPointerException.class,
                () -> Validate.requireNonNullElse(
                    "value",
                    "fallback",
                    (Supplier<IllegalStateException>)null));

        assertEquals("supplier is null",actual.getMessage());
    }

    @Test
    public void
    testRequireNonNullElseGetRejectsNullSupplierWhenValuePresent()
    {
        NullPointerException actual =
            assertThrows(
                NullPointerException.class,
                () -> Validate.requireNonNullElseGet("value",(Supplier<String>)null));

        assertEquals("supplier is null",actual.getMessage());
    }

    @Test
    public void
    testRequireNonNullElseGetWithSupplierRejectsNullDefaultSupplierWhenValuePresent()
    {
        NullPointerException actual =
            assertThrows(
                NullPointerException.class,
                () -> Validate.requireNonNullElseGet(
                    "value",
                    (Supplier<String>)null,
                    () -> new IllegalStateException("x")));

        assertEquals("default supplier is null",actual.getMessage());
    }

    @Test
    public void
    testRequireNonNullElseGetWithSupplierRejectsNullExceptionSupplier()
    {
        NullPointerException actual =
            assertThrows(
                NullPointerException.class,
                () -> Validate.requireNonNullElseGet(
                    "value",
                    () -> "fallback",
                    (Supplier<IllegalStateException>)null));

        assertEquals("exception supplier is null",actual.getMessage());
    }

}

//////////////////////////////////////////////////////////////////////////////
