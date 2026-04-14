/// ///////////////////////////////////////////////////////////////////////////
// BasicCurrencyUnit.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.money;

import javax.money.CurrencyContext;
import javax.money.CurrencyContextBuilder;
import javax.money.CurrencyUnit;
import java.io.Serializable;
import java.util.Currency;
import java.util.Objects;

public
class BasicCurrencyUnit
    implements
        CurrencyUnit,
        Serializable,
        Comparable<CurrencyUnit>
{
    private static final long serialVersionUID = 1L;

    private final String          currencyCode;
    private final int             numericCode;
    private final int             defaultFractionDigits;
    private final CurrencyContext context;

    public
    BasicCurrencyUnit(
        String          currencyCode,
        int             numericCode,
        int             defaultFractionDigits)
    {
        this.currencyCode         = Objects.requireNonNull(currencyCode,"currencyCode must not be null");
        this.numericCode          = numericCode;
        this.defaultFractionDigits = defaultFractionDigits;
        this.context =
            CurrencyContextBuilder
                .of(BasicCurrencyUnit.class.getSimpleName())
                .build();
    }

    public
    BasicCurrencyUnit(Currency javaCurrency)
    {
        this(
            javaCurrency.getCurrencyCode(),
            javaCurrency.getNumericCode(),
            javaCurrency.getDefaultFractionDigits());
    }

    @Override
    public String
    getCurrencyCode()
    {
        return currencyCode;
    }

    @Override
    public int
    getNumericCode()
    {
        return numericCode;
    }

    @Override
    public int
    getDefaultFractionDigits()
    {
        return defaultFractionDigits;
    }

    @Override
    public CurrencyContext
    getContext()
    {
        return context;
    }

    @Override
    public int
    compareTo(CurrencyUnit other)
    {
        return currencyCode.compareTo(other.getCurrencyCode());
    }

    @Override
    public boolean
    equals(Object o)
    {
        if (this == o) return true;
        if (o == null) return false;

        if (o instanceof CurrencyUnit other)
            return currencyCode.equals(other.getCurrencyCode());

        return false;
    }

    @Override
    public int
    hashCode()
    {
        return currencyCode.hashCode();
    }

    @Override
    public String
    toString()
    {
        return currencyCode;
    }

    public static BasicCurrencyUnit
    of(String currencyCode)
    {
        return new BasicCurrencyUnit(Currency.getInstance(currencyCode));
    }

    public static BasicCurrencyUnit
    of(Currency javaCurrency)
    {
        return new BasicCurrencyUnit(javaCurrency);
    }

    public static final BasicCurrencyUnit USD = of("USD");
    public static final BasicCurrencyUnit EUR = of("EUR");
    public static final BasicCurrencyUnit GBP = of("GBP");
    public static final BasicCurrencyUnit JPY = of("JPY");
    public static final BasicCurrencyUnit CHF = of("CHF");
    public static final BasicCurrencyUnit CAD = of("CAD");
    public static final BasicCurrencyUnit AUD = of("AUD");
    public static final BasicCurrencyUnit CNY = of("CNY");
    public static final BasicCurrencyUnit INR = of("INR");
    public static final BasicCurrencyUnit KRW = of("KRW");
    public static final BasicCurrencyUnit BRL = of("BRL");
    public static final BasicCurrencyUnit MXN = of("MXN");
    public static final BasicCurrencyUnit SGD = of("SGD");
    public static final BasicCurrencyUnit HKD = of("HKD");
    public static final BasicCurrencyUnit NZD = of("NZD");
    public static final BasicCurrencyUnit SEK = of("SEK");
    public static final BasicCurrencyUnit NOK = of("NOK");
    public static final BasicCurrencyUnit DKK = of("DKK");
    public static final BasicCurrencyUnit ZAR = of("ZAR");
    public static final BasicCurrencyUnit PLN = of("PLN");
}

//////////////////////////////////////////////////////////////////////////////
