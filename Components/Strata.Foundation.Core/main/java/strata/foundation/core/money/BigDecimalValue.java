//////////////////////////////////////////////////////////////////////////////
// BigDecimalValue.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.money;

import javax.money.NumberValue;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * <p>
 * Implementation of {@link javax.money.NumberValue} that wraps a
 * {@link java.math.BigDecimal} to provide numeric value access
 * for monetary amounts with full precision and type conversion
 * support.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * BigDecimalValue value = BigDecimalValue.of(new BigDecimal("29.99"));
 *
 * // Type conversions
 * BigDecimal exact  = value.numberValue(BigDecimal.class);
 * double     approx = value.doubleValue();
 * int        whole  = value.intValue();
 *
 * // Precision and scale
 * int precision = value.getPrecision();
 * int scale     = value.getScale();
 * </pre>
 */
public
class BigDecimalValue
    extends NumberValue
{
    private final BigDecimal number;

    public
    BigDecimalValue(BigDecimal number)
    {
        this.number = number;
    }

    @Override
    public Class<?>
    getNumberType()
    {
        return BigDecimal.class;
    }

    @Override
    public int
    getPrecision()
    {
        return number.precision();
    }

    @Override
    public int
    getScale()
    {
        return number.scale();
    }

    @Override
    public int
    intValueExact()
    {
        return number.intValueExact();
    }

    @Override
    public long
    longValueExact()
    {
        return number.longValueExact();
    }

    @Override
    public double
    doubleValueExact()
    {
        double d = number.doubleValue();

        if (BigDecimal.valueOf(d).compareTo(number) != 0)
            throw new ArithmeticException(
                "Cannot exactly convert to double: " + number);

        return d;
    }

    @Override
    public <T extends Number> T
    numberValue(Class<T> numberType)
    {
        if (numberType == BigDecimal.class)
            return numberType.cast(number);

        if (numberType == BigInteger.class)
            return numberType.cast(number.toBigInteger());

        if (numberType == Double.class)
            return numberType.cast(number.doubleValue());

        if (numberType == Float.class)
            return numberType.cast(number.floatValue());

        if (numberType == Long.class)
            return numberType.cast(number.longValue());

        if (numberType == Integer.class)
            return numberType.cast(number.intValue());

        if (numberType == Short.class)
            return numberType.cast(number.shortValue());

        if (numberType == Byte.class)
            return numberType.cast(number.byteValue());

        if (numberType.isAssignableFrom(BigDecimal.class))
            return numberType.cast(number);

        throw
            new ClassCastException(
                "Cannot cast BigDecimal to " + numberType.getName());
    }

    @Override
    public NumberValue
    round(MathContext mathContext)
    {
        return new BigDecimalValue(number.round(mathContext));
    }

    @Override
    public <T extends Number> T
    numberValueExact(Class<T> numberType)
    {
        if (numberType == BigDecimal.class)
            return numberType.cast(number);

        if (numberType == BigInteger.class)
            return numberType.cast(number.toBigIntegerExact());

        if (numberType == Long.class)
            return numberType.cast(number.longValueExact());

        if (numberType == Integer.class)
            return numberType.cast(number.intValueExact());

        if (numberType == Short.class)
            return numberType.cast(number.shortValueExact());

        if (numberType == Byte.class)
            return numberType.cast(number.byteValueExact());

        if (numberType == Double.class)
        {
            double d = number.doubleValue();

            if (BigDecimal.valueOf(d).compareTo(number) != 0)
                throw new ArithmeticException(
                    "Cannot exactly convert to double: " + number);

            return numberType.cast(d);
        }

        if (numberType == Float.class)
        {
            float f = number.floatValue();

            if (new BigDecimal(String.valueOf(f)).compareTo(number) != 0)
                throw new ArithmeticException(
                    "Cannot exactly convert to float: " + number);

            return numberType.cast(f);
        }

        throw
            new ClassCastException(
                "Cannot cast BigDecimal to " + numberType.getName());
    }

    @Override
    public long
    getAmountFractionNumerator()
    {
        return
            number
                .remainder(BigDecimal.ONE)
                .movePointRight(number.scale())
                .abs()
                .longValue();
    }

    @Override
    public long
    getAmountFractionDenominator()
    {
        return (long)Math.pow(10,number.scale());
    }

    @Override
    public int
    intValue()
    {
        return number.intValue();
    }

    @Override
    public long
    longValue()
    {
        return number.longValue();
    }

    @Override
    public float
    floatValue()
    {
        return number.floatValue();
    }

    @Override
    public double
    doubleValue()
    {
        return number.doubleValue();
    }

    public static BigDecimalValue
    of(BigDecimal number)
    {
        return new BigDecimalValue(number);
    }
}

//////////////////////////////////////////////////////////////////////////////
