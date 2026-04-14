//////////////////////////////////////////////////////////////////////////////
// BigDecimalValueTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.money;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.money.NumberValue;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class BigDecimalValueTest
{
    // -----------------------------------------------------------------------
    // getNumberType
    // -----------------------------------------------------------------------

    @Test
    public void
    testGetNumberTypeReturnsBigDecimal()
    {
        BigDecimalValue value = BigDecimalValue.of(BigDecimal.TEN);

        assertEquals(BigDecimal.class,value.getNumberType());
    }

    // -----------------------------------------------------------------------
    // getPrecision / getScale
    // -----------------------------------------------------------------------

    @Test
    public void
    testGetPrecision()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("123.45"));

        assertEquals(5,value.getPrecision());
    }

    @Test
    public void
    testGetScale()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("123.45"));

        assertEquals(2,value.getScale());
    }

    @Test
    public void
    testGetScaleZeroForInteger()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("100"));

        assertEquals(0,value.getScale());
    }

    // -----------------------------------------------------------------------
    // intValue / longValue / floatValue / doubleValue
    // -----------------------------------------------------------------------

    @Test
    public void
    testIntValue()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("42.99"));

        assertEquals(42,value.intValue());
    }

    @Test
    public void
    testLongValue()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("123456789012345.67"));

        assertEquals(123456789012345L,value.longValue());
    }

    @Test
    public void
    testFloatValue()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("3.14"));

        assertEquals(3.14f,value.floatValue(),0.001f);
    }

    @Test
    public void
    testDoubleValue()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("3.141592653589793"));

        assertEquals(3.141592653589793,value.doubleValue(),0.0000000000001);
    }

    // -----------------------------------------------------------------------
    // intValueExact / longValueExact / doubleValueExact
    // -----------------------------------------------------------------------

    @Test
    public void
    testIntValueExact()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("42"));

        assertEquals(42,value.intValueExact());
    }

    @Test
    public void
    testIntValueExactThrowsOnFraction()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("42.5"));

        assertThrows(ArithmeticException.class,value::intValueExact);
    }

    @Test
    public void
    testLongValueExact()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("9876543210"));

        assertEquals(9876543210L,value.longValueExact());
    }

    @Test
    public void
    testLongValueExactThrowsOnFraction()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("42.1"));

        assertThrows(ArithmeticException.class,value::longValueExact);
    }

    @Test
    public void
    testDoubleValueExactSucceeds()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("0.5"));

        assertEquals(0.5,value.doubleValueExact());
    }

    // -----------------------------------------------------------------------
    // numberValue(Class<T>)
    // -----------------------------------------------------------------------

    @Test
    public void
    testNumberValueBigDecimal()
    {
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimalValue value = BigDecimalValue.of(expected);

        assertSame(expected,value.numberValue(BigDecimal.class));
    }

    @Test
    public void
    testNumberValueBigInteger()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("999"));

        assertEquals(
            BigInteger.valueOf(999),
            value.numberValue(BigInteger.class));
    }

    @Test
    public void
    testNumberValueDouble()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("3.14"));

        assertEquals(3.14,value.numberValue(Double.class),0.001);
    }

    @Test
    public void
    testNumberValueFloat()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("2.5"));

        assertEquals(2.5f,value.numberValue(Float.class),0.001f);
    }

    @Test
    public void
    testNumberValueLong()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("100"));

        assertEquals(100L,value.numberValue(Long.class));
    }

    @Test
    public void
    testNumberValueInteger()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("42"));

        assertEquals(42,value.numberValue(Integer.class));
    }

    @Test
    public void
    testNumberValueShort()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("7"));

        assertEquals((short)7,value.numberValue(Short.class));
    }

    @Test
    public void
    testNumberValueByte()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("3"));

        assertEquals((byte)3,value.numberValue(Byte.class));
    }

    // -----------------------------------------------------------------------
    // numberValueExact(Class<T>)
    // -----------------------------------------------------------------------

    @Test
    public void
    testNumberValueExactBigDecimal()
    {
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimalValue value = BigDecimalValue.of(expected);

        assertSame(expected,value.numberValueExact(BigDecimal.class));
    }

    @Test
    public void
    testNumberValueExactBigIntegerSuccess()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("500"));

        assertEquals(
            BigInteger.valueOf(500),
            value.numberValueExact(BigInteger.class));
    }

    @Test
    public void
    testNumberValueExactBigIntegerThrowsOnFraction()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("500.1"));

        assertThrows(
            ArithmeticException.class,
            () -> value.numberValueExact(BigInteger.class));
    }

    @Test
    public void
    testNumberValueExactLongSuccess()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("42"));

        assertEquals(42L,value.numberValueExact(Long.class));
    }

    @Test
    public void
    testNumberValueExactLongThrowsOnFraction()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("42.5"));

        assertThrows(
            ArithmeticException.class,
            () -> value.numberValueExact(Long.class));
    }

    @Test
    public void
    testNumberValueExactIntegerSuccess()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("100"));

        assertEquals(100,value.numberValueExact(Integer.class));
    }

    @Test
    public void
    testNumberValueExactIntegerThrowsOnFraction()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("100.9"));

        assertThrows(
            ArithmeticException.class,
            () -> value.numberValueExact(Integer.class));
    }

    @Test
    public void
    testNumberValueExactShortSuccess()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("7"));

        assertEquals((short)7,value.numberValueExact(Short.class));
    }

    @Test
    public void
    testNumberValueExactByteSuccess()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("3"));

        assertEquals((byte)3,value.numberValueExact(Byte.class));
    }

    @Test
    public void
    testNumberValueExactDoubleSuccess()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("0.5"));

        assertEquals(0.5,value.numberValueExact(Double.class));
    }

    @Test
    public void
    testNumberValueExactFloatSuccess()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("0.5"));

        assertEquals(0.5f,value.numberValueExact(Float.class));
    }

    // -----------------------------------------------------------------------
    // round
    // -----------------------------------------------------------------------

    @Test
    public void
    testRound()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("123.456"));
        NumberValue rounded =
            value.round(new MathContext(4));

        assertEquals(
            new BigDecimal("123.5"),
            rounded.numberValue(BigDecimal.class));
    }

    @Test
    public void
    testRoundToInteger()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("99.9"));
        NumberValue rounded =
            value.round(new MathContext(3));

        assertEquals(
            new BigDecimal("99.9"),
            rounded.numberValue(BigDecimal.class));
    }

    // -----------------------------------------------------------------------
    // getAmountFractionNumerator / getAmountFractionDenominator
    // -----------------------------------------------------------------------

    @Test
    public void
    testGetAmountFractionNumerator()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("1.23"));

        assertEquals(23,value.getAmountFractionNumerator());
    }

    @Test
    public void
    testGetAmountFractionNumeratorZero()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("5.00"));

        assertEquals(0,value.getAmountFractionNumerator());
    }

    @Test
    public void
    testGetAmountFractionNumeratorThreeDigits()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("7.456"));

        assertEquals(456,value.getAmountFractionNumerator());
    }

    @Test
    public void
    testGetAmountFractionNumeratorNegative()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("-3.75"));

        assertEquals(75,value.getAmountFractionNumerator());
    }

    @Test
    public void
    testGetAmountFractionDenominator()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("1.23"));

        assertEquals(100,value.getAmountFractionDenominator());
    }

    @Test
    public void
    testGetAmountFractionDenominatorThreeDigits()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("7.456"));

        assertEquals(1000,value.getAmountFractionDenominator());
    }

    @Test
    public void
    testGetAmountFractionDenominatorForWholeNumber()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("100"));

        assertEquals(1,value.getAmountFractionDenominator());
    }

    // -----------------------------------------------------------------------
    // of factory method
    // -----------------------------------------------------------------------

    @Test
    public void
    testOfFactoryMethod()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("42.00"));

        assertEquals(42.0,value.doubleValue());
        assertEquals(BigDecimal.class,value.getNumberType());
    }

    // -----------------------------------------------------------------------
    // zero and negative values
    // -----------------------------------------------------------------------

    @Test
    public void
    testZeroValue()
    {
        BigDecimalValue value =
            BigDecimalValue.of(BigDecimal.ZERO);

        assertEquals(0,value.intValue());
        assertEquals(0L,value.longValue());
        assertEquals(0.0,value.doubleValue());
        assertEquals(0.0f,value.floatValue());
    }

    @Test
    public void
    testNegativeValue()
    {
        BigDecimalValue value =
            BigDecimalValue.of(new BigDecimal("-99.99"));

        assertEquals(-99,value.intValue());
        assertEquals(-99L,value.longValue());
        assertEquals(-99.99,value.doubleValue(),0.001);
    }
}

//////////////////////////////////////////////////////////////////////////////

