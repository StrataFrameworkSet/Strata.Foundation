//////////////////////////////////////////////////////////////////////////////
// MoneyFactory.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.money;

import javax.money.*;
import java.math.BigDecimal;

/**
 * <p>
 * Implementation of {@link javax.money.MonetaryAmountFactory} that
 * creates {@link Money} instances through a fluent builder pattern,
 * allowing currency, number, and monetary context to be set
 * independently before creation.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation via Money.getFactory()
 * Money amount = Money.of(BasicCurrencyUnit.USD,10.00);
 * Money copy   = amount.getFactory().setNumber(25.00).create();
 *
 * // Direct usage
 * MoneyFactory factory = new MoneyFactory();
 * Money result = factory
 *     .setCurrency("USD")
 *     .setNumber(49.99)
 *     .create();
 * </pre>
 */
public
class MoneyFactory
    implements MonetaryAmountFactory<Money>
{
    private CurrencyUnit    currency;
    private BigDecimal      number;
    private MonetaryContext context;

    MoneyFactory()
    {
        this.currency = null;
        this.number   = BigDecimal.ZERO;
        this.context  = null;
    }

    @Override
    public Class<? extends MonetaryAmount>
    getAmountType()
    {
        return Money.class;
    }

    @Override
    public MoneyFactory
    setCurrency(CurrencyUnit currency)
    {
        this.currency = currency;
        return this;
    }

    @Override
    public MoneyFactory
    setCurrency(String currencyCode)
    {
        this.currency = BasicCurrencyUnit.of(currencyCode);
        return this;
    }

    @Override
    public MoneyFactory
    setNumber(double number)
    {
        this.number = BigDecimal.valueOf(number);
        return this;
    }

    @Override
    public MoneyFactory
    setNumber(long number)
    {
        this.number = BigDecimal.valueOf(number);
        return this;
    }

    @Override
    public MoneyFactory
    setNumber(Number number)
    {
        this.number = toBigDecimal(number);
        return this;
    }

    @Override
    public MoneyFactory
    setContext(MonetaryContext monetaryContext)
    {
        this.context = monetaryContext;
        return this;
    }

    @Override
    public NumberValue
    getMaxNumber()
    {
        return null;
    }

    @Override
    public NumberValue
    getMinNumber()
    {
        return null;
    }

    @Override
    public MonetaryContext
    getDefaultMonetaryContext()
    {
        return
            MonetaryContextBuilder
                .of(Money.class)
                .setPrecision(0)
                .set("amountType",Money.class)
                .build();
    }

    @Override
    public MonetaryContext
    getMaximalMonetaryContext()
    {
        return getDefaultMonetaryContext();
    }

    @Override
    public Money
    create()
    {
        if (currency == null)
            throw new MonetaryException(
                "Currency must be set before creating Money");

        return new Money(currency,number);
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
}

//////////////////////////////////////////////////////////////////////////////
