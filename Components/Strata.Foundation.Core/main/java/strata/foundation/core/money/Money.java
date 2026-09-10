//////////////////////////////////////////////////////////////////////////////
// Money.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.money;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.money.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

/**
 * <p>
 * Immutable implementation of {@link javax.money.MonetaryAmount} that
 * represents a monetary value with a {@link javax.money.CurrencyUnit}
 * and a {@link java.math.BigDecimal} amount. Supports arithmetic
 * operations, comparison, and JSON serialization.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation using factory methods
 * Money price = Money.of(BasicCurrencyUnit.USD,29.99);
 * Money tax   = Money.of(BasicCurrencyUnit.USD,2.50);
 *
 * // Arithmetic
 * Money total    = price.add(tax);
 * Money doubled  = price.multiply(2);
 * Money negated  = price.negate();
 *
 * // Comparison
 * boolean cheaper = price.isLessThan(total);
 *
 * // JSON deserialization
 * Money parsed = new Money("USD29.99");
 * </pre>
 */
public
class Money
    implements MonetaryAmount,Serializable
{
    private CurrencyUnit    currency;
    private BigDecimal      number;
    private MonetaryContext context;

    public
    Money()
    {
        this(BasicCurrencyUnit.USD,BigDecimal.ZERO);
    }

    public
    @JsonCreator
    Money(@JsonProperty("money") String currencyCodeAndNumber)
    {
        this(
            getCurrencyUnit(currencyCodeAndNumber),
            getNumber(currencyCodeAndNumber));
    }

    Money(CurrencyUnit currency,BigDecimal number)
    {
        this.currency = currency;
        this.number = number;
        this.context =
            MonetaryContextBuilder
                .of(Money.class)
                .setPrecision(0)
                .set("amountType",Money.class)
                .build();
    }

    @Override
    @JsonIgnore
    public CurrencyUnit
    getCurrency()
    {
        return currency;
    }

    @Override
    @JsonIgnore
    public NumberValue
    getNumber()
    {
        return new BigDecimalValue(number);
    }

    @Override
    @JsonIgnore
    public MonetaryContext
    getContext()
    {
        return context;
    }

    @Override
    @JsonIgnore
    public MonetaryAmountFactory<Money>
    getFactory()
    {
        return new MoneyFactory().setCurrency(currency);
    }

    @Override
    public boolean
    isGreaterThan(MonetaryAmount monetaryAmount)
    {
        requireSameCurrency(monetaryAmount);
        return number.compareTo(toBigDecimal(monetaryAmount)) > 0;
    }

    @Override
    public boolean
    isGreaterThanOrEqualTo(MonetaryAmount monetaryAmount)
    {
        requireSameCurrency(monetaryAmount);
        return number.compareTo(toBigDecimal(monetaryAmount)) >= 0;
    }

    @Override
    public boolean
    isLessThan(MonetaryAmount monetaryAmount)
    {
        requireSameCurrency(monetaryAmount);
        return number.compareTo(toBigDecimal(monetaryAmount)) < 0;
    }

    @Override
    public boolean
    isLessThanOrEqualTo(MonetaryAmount monetaryAmount)
    {
        requireSameCurrency(monetaryAmount);
        return number.compareTo(toBigDecimal(monetaryAmount)) <= 0;
    }

    @Override
    public boolean
    isEqualTo(MonetaryAmount monetaryAmount)
    {
        requireSameCurrency(monetaryAmount);
        return number.compareTo(toBigDecimal(monetaryAmount)) == 0;
    }

    @Override
    @JsonIgnore
    public int
    signum()
    {
        return number.signum();
    }

    @Override
    @JsonIgnore
    public boolean
    isZero() { return MonetaryAmount.super.isZero(); }

    @Override
    @JsonIgnore
    public boolean
    isNegative() { return MonetaryAmount.super.isNegative(); }

    @Override
    @JsonIgnore
    public boolean
    isNegativeOrZero() { return MonetaryAmount.super.isNegativeOrZero(); }

    @Override
    @JsonIgnore
    public boolean
    isPositive() { return MonetaryAmount.super.isPositive(); }

    @Override
    @JsonIgnore
    public boolean
    isPositiveOrZero() { return MonetaryAmount.super.isPositiveOrZero(); }

    @Override
    public Money
    add(MonetaryAmount monetaryAmount)
    {
        requireSameCurrency(monetaryAmount);
        return new Money(currency,number.add(toBigDecimal(monetaryAmount)));
    }

    @Override
    public Money
    subtract(MonetaryAmount monetaryAmount)
    {
        requireSameCurrency(monetaryAmount);
        return new Money(currency,number.subtract(toBigDecimal(monetaryAmount)));
    }

    @Override
    public Money
    multiply(long l)
    {
        return new Money(currency,number.multiply(BigDecimal.valueOf(l)));
    }

    @Override
    public Money
    multiply(double v)
    {
        return new Money(currency,number.multiply(BigDecimal.valueOf(v)));
    }

    @Override
    public Money
    multiply(Number multiplicand)
    {
        return new Money(currency,number.multiply(toBigDecimal(multiplicand)));
    }

    @Override
    public Money
    divide(long l)
    {
        return new Money(
            currency,
            number.divide(
                BigDecimal.valueOf(l),
                MathContext.DECIMAL128));
    }

    @Override
    public Money
    divide(double v)
    {
        return new Money(
            currency,
            number.divide(
                BigDecimal.valueOf(v),
                MathContext.DECIMAL128));
    }

    @Override
    public Money
    divide(Number divisor)
    {
        return new Money(
            currency,
            number.divide(
                toBigDecimal(divisor),
                MathContext.DECIMAL128));
    }

    @Override
    public Money
    remainder(long l)
    {
        return new Money(currency,number.remainder(BigDecimal.valueOf(l)));
    }

    @Override
    public Money
    remainder(double v)
    {
        return new Money(currency,number.remainder(BigDecimal.valueOf(v)));
    }

    @Override
    public Money
    remainder(Number divisor)
    {
        return new Money(currency,number.remainder(toBigDecimal(divisor)));
    }

    @Override
    public MonetaryAmount[]
    divideAndRemainder(long l)
    {
        BigDecimal divisor = BigDecimal.valueOf(l);
        BigDecimal[] result = number.divideAndRemainder(divisor);

        return new MonetaryAmount[]
            {
                new Money(currency,result[0]),
                new Money(currency,result[1])
            };
    }

    @Override
    public MonetaryAmount[]
    divideAndRemainder(double v)
    {
        BigDecimal divisor = BigDecimal.valueOf(v);
        BigDecimal[] result = number.divideAndRemainder(divisor);

        return new MonetaryAmount[]
            {
                new Money(currency,result[0]),
                new Money(currency,result[1])
            };
    }

    @Override
    public MonetaryAmount[]
    divideAndRemainder(Number divisor)
    {
        BigDecimal[] result = number.divideAndRemainder(toBigDecimal(divisor));

        return new MonetaryAmount[]
            {
                new Money(currency,result[0]),
                new Money(currency,result[1])
            };
    }

    @Override
    public Money
    divideToIntegralValue(long l)
    {
        return new Money(
            currency,
            number.divideToIntegralValue(BigDecimal.valueOf(l)));
    }

    @Override
    public Money
    divideToIntegralValue(double v)
    {
        return new Money(
            currency,
            number.divideToIntegralValue(BigDecimal.valueOf(v)));
    }

    @Override
    public Money
    divideToIntegralValue(Number divisor)
    {
        return new Money(
            currency,
            number.divideToIntegralValue(toBigDecimal(divisor)));
    }

    @Override
    public Money
    scaleByPowerOfTen(int i)
    {
        return new Money(currency,number.scaleByPowerOfTen(i));
    }

    @Override
    public Money
    abs()
    {
        return new Money(currency,number.abs());
    }

    @Override
    public Money
    negate()
    {
        return new Money(currency,number.negate());
    }

    @Override
    public Money
    plus()
    {
        return new Money(currency,number.plus());
    }

    @Override
    public Money
    stripTrailingZeros()
    {
        return new Money(currency,number.stripTrailingZeros());
    }

    @Override
    public int
    compareTo(MonetaryAmount o)
    {
        requireSameCurrency(o);
        return number.compareTo(toBigDecimal(o));
    }

    @Override
    public boolean
    equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Money other = (Money)o;

        return
            Objects.equals(currency,other.currency) &&
                number.compareTo(other.number) == 0;
    }

    @Override
    public int
    hashCode()
    {
        return Objects.hash(currency,number.stripTrailingZeros());
    }

    @Override
    @JsonProperty("money")
    public String
    toString()
    {
        return currency.getCurrencyCode() + number.toPlainString();
    }

    public static Money
    of(CurrencyUnit currency,NumberValue number)
    {
        return new Money(
            currency,
            number.numberValue(BigDecimal.class));
    }

    public static Money
    of(CurrencyUnit currency,BigDecimal number)
    {
        return new Money(currency,number);
    }

    public static Money
    of(CurrencyUnit currency,double number)
    {
        return new Money(currency,BigDecimal.valueOf(number));
    }

    public static Money
    of(CurrencyUnit currency,long number)
    {
        return new Money(currency,BigDecimal.valueOf(number));
    }

    @Serial
    private void
    writeObject(ObjectOutputStream out)
        throws IOException
    {
        out.writeUTF(currency.getCurrencyCode());
        out.writeObject(number);
    }

    @Serial
    private void
    readObject(ObjectInputStream in)
        throws IOException,ClassNotFoundException
    {
        this.currency = BasicCurrencyUnit.of(in.readUTF());
        this.number   = (BigDecimal)in.readObject();
        this.context  =
            MonetaryContextBuilder
                .of(Money.class)
                .setPrecision(0)
                .set("amountType",Money.class)
                .build();
    }

    private void
    requireSameCurrency(MonetaryAmount other)
    {
        if (!currency.getCurrencyCode().equals(
            other.getCurrency().getCurrencyCode()))
            throw new MonetaryException(
                "Currency mismatch: " +
                    currency.getCurrencyCode() +
                    " vs " +
                    other.getCurrency().getCurrencyCode());
    }

    private static BigDecimal
    toBigDecimal(MonetaryAmount amount)
    {
        return amount.getNumber().numberValue(BigDecimal.class);
    }

    private static BigDecimal
    toBigDecimal(Number value)
    {
        if (value instanceof BigDecimal bd)
            return bd;

        if (value instanceof Long || value instanceof Integer ||
            value instanceof Short || value instanceof Byte)
            return BigDecimal.valueOf(value.longValue());

        if (value instanceof Double || value instanceof Float)
            return BigDecimal.valueOf(value.doubleValue());

        return new BigDecimal(value.toString());
    }

    private static CurrencyUnit
    getCurrencyUnit(String currencyCodeAndNumber)
    {
        String currencyCode = currencyCodeAndNumber.substring(0,3);

        return BasicCurrencyUnit.of(currencyCode);
    }

    private static BigDecimal
    getNumber(String currencyCodeAndNumber)
    {
        return new BigDecimal(currencyCodeAndNumber.substring(3));
    }
}

//////////////////////////////////////////////////////////////////////////////
