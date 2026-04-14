//////////////////////////////////////////////////////////////////////////////
// BasicCurrencyUnitTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.money;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.money.CurrencyUnit;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class BasicCurrencyUnitTest
{
    @Test
    public void
    testOfStringReturnsCorrectCurrencyCode()
    {
        BasicCurrencyUnit usd = BasicCurrencyUnit.of("USD");

        assertEquals("USD",usd.getCurrencyCode());
    }

    @Test
    public void
    testOfStringReturnsCorrectNumericCode()
    {
        BasicCurrencyUnit usd = BasicCurrencyUnit.of("USD");

        assertEquals(840,usd.getNumericCode());
    }

    @Test
    public void
    testOfStringReturnsCorrectFractionDigits()
    {
        BasicCurrencyUnit usd = BasicCurrencyUnit.of("USD");

        assertEquals(2,usd.getDefaultFractionDigits());
    }

    @Test
    public void
    testOfJavaCurrencyReturnsCorrectCurrencyCode()
    {
        BasicCurrencyUnit eur =
            BasicCurrencyUnit.of(Currency.getInstance("EUR"));

        assertEquals("EUR",eur.getCurrencyCode());
        assertEquals(978,eur.getNumericCode());
        assertEquals(2,eur.getDefaultFractionDigits());
    }

    @Test
    public void
    testConstructorWithExplicitValues()
    {
        BasicCurrencyUnit custom =
            new BasicCurrencyUnit("XYZ",999,4);

        assertEquals("XYZ",custom.getCurrencyCode());
        assertEquals(999,custom.getNumericCode());
        assertEquals(4,custom.getDefaultFractionDigits());
    }

    @Test
    public void
    testConstructorRejectsNullCurrencyCode()
    {
        assertThrows(
            NullPointerException.class,
            () -> new BasicCurrencyUnit(null,0,2));
    }

    @Test
    public void
    testOfInvalidCurrencyCodeThrows()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> BasicCurrencyUnit.of("INVALID"));
    }

    @Test
    public void
    testJpyHasZeroFractionDigits()
    {
        assertEquals(0,BasicCurrencyUnit.JPY.getDefaultFractionDigits());
    }

    @Test
    public void
    testGetContextNotNull()
    {
        assertNotNull(BasicCurrencyUnit.USD.getContext());
    }

    @Test
    public void
    testEqualsSameCode()
    {
        BasicCurrencyUnit a = BasicCurrencyUnit.of("USD");
        BasicCurrencyUnit b = BasicCurrencyUnit.of("USD");

        assertEquals(a,b);
    }

    @Test
    public void
    testEqualsDifferentCode()
    {
        assertNotEquals(BasicCurrencyUnit.USD,BasicCurrencyUnit.EUR);
    }

    @Test
    public void
    testEqualsNull()
    {
        assertNotEquals(null,BasicCurrencyUnit.USD);
    }

    @Test
    public void
    testEqualsSameInstance()
    {
        assertEquals(BasicCurrencyUnit.USD,BasicCurrencyUnit.USD);
    }

    @Test
    public void
    testHashCodeConsistentWithEquals()
    {
        BasicCurrencyUnit a = BasicCurrencyUnit.of("GBP");
        BasicCurrencyUnit b = BasicCurrencyUnit.of("GBP");

        assertEquals(a.hashCode(),b.hashCode());
    }

    @Test
    public void
    testHashCodeDiffersForDifferentCurrencies()
    {
        assertNotEquals(
            BasicCurrencyUnit.USD.hashCode(),
            BasicCurrencyUnit.EUR.hashCode());
    }

    @Test
    public void
    testToString()
    {
        assertEquals("USD",BasicCurrencyUnit.USD.toString());
        assertEquals("EUR",BasicCurrencyUnit.EUR.toString());
        assertEquals("JPY",BasicCurrencyUnit.JPY.toString());
    }

    @Test
    public void
    testCompareToSameCurrency()
    {
        BasicCurrencyUnit a = BasicCurrencyUnit.of("USD");
        BasicCurrencyUnit b = BasicCurrencyUnit.of("USD");

        assertEquals(0,a.compareTo(b));
    }

    @Test
    public void
    testCompareToOrdering()
    {
        assertTrue(BasicCurrencyUnit.AUD.compareTo(BasicCurrencyUnit.USD) < 0);
        assertTrue(BasicCurrencyUnit.USD.compareTo(BasicCurrencyUnit.AUD) > 0);
    }

    @Test
    public void
    testPreDefinedConstants()
    {
        assertEquals("USD",BasicCurrencyUnit.USD.getCurrencyCode());
        assertEquals("EUR",BasicCurrencyUnit.EUR.getCurrencyCode());
        assertEquals("GBP",BasicCurrencyUnit.GBP.getCurrencyCode());
        assertEquals("JPY",BasicCurrencyUnit.JPY.getCurrencyCode());
        assertEquals("CHF",BasicCurrencyUnit.CHF.getCurrencyCode());
        assertEquals("CAD",BasicCurrencyUnit.CAD.getCurrencyCode());
        assertEquals("AUD",BasicCurrencyUnit.AUD.getCurrencyCode());
        assertEquals("CNY",BasicCurrencyUnit.CNY.getCurrencyCode());
        assertEquals("INR",BasicCurrencyUnit.INR.getCurrencyCode());
        assertEquals("KRW",BasicCurrencyUnit.KRW.getCurrencyCode());
        assertEquals("BRL",BasicCurrencyUnit.BRL.getCurrencyCode());
        assertEquals("MXN",BasicCurrencyUnit.MXN.getCurrencyCode());
        assertEquals("SGD",BasicCurrencyUnit.SGD.getCurrencyCode());
        assertEquals("HKD",BasicCurrencyUnit.HKD.getCurrencyCode());
        assertEquals("NZD",BasicCurrencyUnit.NZD.getCurrencyCode());
        assertEquals("SEK",BasicCurrencyUnit.SEK.getCurrencyCode());
        assertEquals("NOK",BasicCurrencyUnit.NOK.getCurrencyCode());
        assertEquals("DKK",BasicCurrencyUnit.DKK.getCurrencyCode());
        assertEquals("ZAR",BasicCurrencyUnit.ZAR.getCurrencyCode());
        assertEquals("PLN",BasicCurrencyUnit.PLN.getCurrencyCode());
    }

    @Test
    public void
    testEqualsWithDifferentCurrencyUnitImplementation()
    {
        CurrencyUnit anonymous = new CurrencyUnit()
        {
            @Override public String getCurrencyCode() { return "USD"; }
            @Override public int getNumericCode() { return 840; }
            @Override public int getDefaultFractionDigits() { return 2; }
            @Override public javax.money.CurrencyContext getContext() { return null; }
            @Override public int compareTo(CurrencyUnit o) { return 0; }
        };

        assertEquals(BasicCurrencyUnit.USD,anonymous);
    }
}

//////////////////////////////////////////////////////////////////////////////

