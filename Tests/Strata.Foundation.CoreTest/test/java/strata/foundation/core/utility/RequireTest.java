//////////////////////////////////////////////////////////////////////////////
// RequireTest.java
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
class RequireTest
{
    @Test
    public void
    testTrueConditionDoesNotThrow()
    {
        assertDoesNotThrow(
            () -> Require.isTrue(true,() -> new IllegalStateException("x")));
    }

    @Test
    public void
    testTrueConditionDoesNotInvokeSupplier()
    {
        AtomicBoolean invoked = new AtomicBoolean(false);

        Require.isTrue(
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
                () -> Require.isTrue(false,() -> expected));

        assertSame(expected,actual);
    }

    @Test
    public void
    testFalseConditionInvokesSupplier()
    {
        AtomicBoolean invoked = new AtomicBoolean(false);

        assertThrows(
            IllegalStateException.class,
            () -> Require.isTrue(
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
                () -> Require.isTrue(false,() -> expected));

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
                () -> Require.isTrue(false,() -> expected));

        assertSame(expected,actual);
    }

    @Test
    public void
    testIsFalseFalseConditionDoesNotThrow()
    {
        assertDoesNotThrow(
            () -> Require.isFalse(false,() -> new IllegalStateException("x")));
    }

    @Test
    public void
    testIsFalseFalseConditionDoesNotInvokeSupplier()
    {
        AtomicBoolean invoked = new AtomicBoolean(false);

        Require.isFalse(
            false,
            () ->
            {
                invoked.set(true);
                return new IllegalStateException("x");
            });

        assertFalse(invoked.get());
    }

    @Test
    public void
    testIsFalseTrueConditionThrowsSuppliedInstance()
    {
        IllegalArgumentException expected = new IllegalArgumentException("bad");
        IllegalArgumentException actual =
            assertThrows(
                IllegalArgumentException.class,
                () -> Require.isFalse(true,() -> expected));

        assertSame(expected,actual);
    }

    @Test
    public void
    testIsFalseTrueConditionInvokesSupplier()
    {
        AtomicBoolean invoked = new AtomicBoolean(false);

        assertThrows(
            IllegalStateException.class,
            () -> Require.isFalse(
                true,
                () ->
                {
                    invoked.set(true);
                    return new IllegalStateException("x");
                }));

        assertTrue(invoked.get());
    }

    @Test
    public void
    testIsFalseCheckedExceptionPropagates()
    {
        IOException expected = new IOException("io");
        IOException actual =
            assertThrows(
                IOException.class,
                () -> Require.isFalse(true,() -> expected));

        assertSame(expected,actual);
    }

    @Test
    public void
    testIsFalseUncheckedExceptionPropagates()
    {
        UnsupportedOperationException expected =
            new UnsupportedOperationException("nope");
        UnsupportedOperationException actual =
            assertThrows(
                UnsupportedOperationException.class,
                () -> Require.isFalse(true,() -> expected));

        assertSame(expected,actual);
    }

    @Test
    public void
    testIsFalseRejectsNullSupplierWhenConditionFalse()
    {
        NullPointerException actual =
            assertThrows(
                NullPointerException.class,
                () -> Require.isFalse(false,(Supplier<IllegalStateException>)null));

        assertEquals("supplier is null",actual.getMessage());
    }

    @Test
    public void
    testIsTrueNonNullReturnsValue()
    {
        String value = new String("value");

        assertSame(value,Require.notNull(value));
        assertSame(value,Require.notNull(value,"message"));
    }

    @Test
    public void
    testIsTrueNonNullThrowsOnNull()
    {
        assertThrows(
            NullPointerException.class,
            () -> Require.notNull(null));
    }

    @Test
    public void
    testIsTrueNonNullWithMessageThrowsOnNull()
    {
        NullPointerException actual =
            assertThrows(
                NullPointerException.class,
                () -> Require.notNull(null,"value is null"));

        assertEquals("value is null",actual.getMessage());
    }

    @Test
    public void
    testIsTrueNonNullWithSupplierReturnsValue()
    {
        AtomicBoolean invoked = new AtomicBoolean(false);
        String        value = new String("value");
        String        actual =
            Require.notNull(
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
    testIsTrueNonNullWithSupplierThrowsSuppliedInstance()
    {
        IllegalStateException expected = new IllegalStateException("null");
        IllegalStateException actual =
            assertThrows(
                IllegalStateException.class,
                () -> Require.notNull(null,() -> expected));

        assertSame(expected,actual);
    }

    @Test
    public void
    testIsTrueNonNullWithSupplierThrowsCheckedException()
    {
        IOException expected = new IOException("io");
        IOException actual =
            assertThrows(
                IOException.class,
                () -> Require.notNull(null,() -> expected));

        assertSame(expected,actual);
    }

    @Test
    public void
    testIsTrueNonNullElse()
    {
        String value = new String("value");
        String fallback = new String("fallback");

        assertSame(value,Require.notNullElse(value,fallback));
        assertSame(fallback,Require.notNullElse(null,fallback));
        assertThrows(
            NullPointerException.class,
            () -> Require.notNullElse(null,null));
    }

    @Test
    public void
    testIsTrueNonNullElseWithSupplier()
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

        assertSame(value,Require.notNullElse(value,fallback,supplier));
        assertSame(value,Require.notNullElse(value,null,supplier));
        assertSame(fallback,Require.notNullElse(null,fallback,supplier));
        assertFalse(invoked.get());
    }

    @Test
    public void
    testIsTrueNonNullElseWithSupplierThrowsSuppliedInstance()
    {
        IOException expected = new IOException("io");
        IOException actual =
            assertThrows(
                IOException.class,
                () -> Require.notNullElse(
                    (String)null,
                    (String)null,
                    () -> expected));

        assertSame(expected,actual);
    }

    @Test
    public void
    testIsTrueNonNullElseGet()
    {
        AtomicBoolean invoked = new AtomicBoolean(false);
        String        value = new String("value");
        String        fallback = new String("fallback");

        assertSame(
            value,
            Require.notNullElseGet(
                value,
                () ->
                {
                    invoked.set(true);
                    return fallback;
                }));
        assertFalse(invoked.get());
        assertSame(fallback,Require.notNullElseGet(null,() -> fallback));
        assertThrows(
            NullPointerException.class,
            () -> Require.notNullElseGet(null,() -> null));
        assertThrows(
            NullPointerException.class,
            () -> Require.notNullElseGet(null,null));
    }

    @Test
    public void
    testIsTrueNonNullElseGetWithSupplier()
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

        assertSame(value,Require.notNullElseGet(value,defaultSupplier,supplier));
        assertFalse(defaultInvoked.get());
        assertSame(fallback,Require.notNullElseGet(null,defaultSupplier,supplier));
        assertTrue(defaultInvoked.get());
        assertFalse(invoked.get());
    }

    @Test
    public void
    testIsTrueNonNullElseGetWithSupplierThrowsSuppliedInstance()
    {
        IOException expected = new IOException("io");
        IOException actual =
            assertThrows(
                IOException.class,
                () -> Require.notNullElseGet(
                    (String)null,
                    () -> null,
                    () -> expected));

        assertSame(expected,actual);
    }

    @Test
    public void
    testIsTrueNonNullElseGetWithSupplierRejectsNullDefaultSupplier()
    {
        NullPointerException actual =
            assertThrows(
                NullPointerException.class,
                () -> Require.notNullElseGet(
                    (String)null,
                    null,
                    () -> new IllegalStateException("x")));

        assertEquals("default supplier is null",actual.getMessage());
    }

    @Test
    public void
    testIsTrueRejectsNullSupplierWhenConditionTrue()
    {
        NullPointerException actual =
            assertThrows(
                NullPointerException.class,
                () -> Require.isTrue(true,(Supplier<IllegalStateException>)null));

        assertEquals("supplier is null",actual.getMessage());
    }

    @Test
    public void
    testIsTrueNonNullRejectsNullSupplierWhenValuePresent()
    {
        NullPointerException actual =
            assertThrows(
                NullPointerException.class,
                () -> Require.notNull(
                    "value",
                    (Supplier<IllegalStateException>)null));

        assertEquals("supplier is null",actual.getMessage());
    }

    @Test
    public void
    testIsTrueNonNullElseRejectsNullSupplierWhenValuePresent()
    {
        NullPointerException actual =
            assertThrows(
                NullPointerException.class,
                () -> Require.notNullElse(
                    "value",
                    "fallback",
                    (Supplier<IllegalStateException>)null));

        assertEquals("supplier is null",actual.getMessage());
    }

    @Test
    public void
    testIsTrueNonNullElseGetRejectsNullSupplierWhenValuePresent()
    {
        NullPointerException actual =
            assertThrows(
                NullPointerException.class,
                () -> Require.notNullElseGet("value",(Supplier<String>)null));

        assertEquals("supplier is null",actual.getMessage());
    }

    @Test
    public void
    testIsTrueNonNullElseGetWithSupplierRejectsNullDefaultSupplierWhenValuePresent()
    {
        NullPointerException actual =
            assertThrows(
                NullPointerException.class,
                () -> Require.notNullElseGet(
                    "value",
                    (Supplier<String>)null,
                    () -> new IllegalStateException("x")));

        assertEquals("default supplier is null",actual.getMessage());
    }

    @Test
    public void
    testIsTrueNonNullElseGetWithSupplierRejectsNullExceptionSupplier()
    {
        NullPointerException actual =
            assertThrows(
                NullPointerException.class,
                () -> Require.notNullElseGet(
                    "value",
                    () -> "fallback",
                    (Supplier<IllegalStateException>)null));

        assertEquals("exception supplier is null",actual.getMessage());
    }

}

//////////////////////////////////////////////////////////////////////////////
