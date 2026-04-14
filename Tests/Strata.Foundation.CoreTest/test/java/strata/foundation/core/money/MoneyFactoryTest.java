//////////////////////////////////////////////////////////////////////////////
// MoneyFactoryTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.money;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.money.MonetaryException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class MoneyFactoryTest
{
    @Test
    public void
    testGetAmountType()
    {
        MoneyFactory factory = new MoneyFactory();

        assertEquals(Money.class,factory.getAmountType());
    }

    @Test
    public void
    testCreateWithCurrencyUnitAndDefaults()
    {
        Money money =
            new MoneyFactory()
                .setCurrency(BasicCurrencyUnit.USD)
                .create();

        assertEquals("USD",money.getCurrency().getCurrencyCode());
        assertEquals(
            0,
            BigDecimal.ZERO.compareTo(
                money.getNumber().numberValue(BigDecimal.class)));
    }

    @Test
    public void
    testCreateWithCurrencyUnitAndDoubleNumber()
    {
        Money money =
            new MoneyFactory()
                .setCurrency(BasicCurrencyUnit.EUR)
                .setNumber(49.99)
                .create();

        assertEquals("EUR",money.getCurrency().getCurrencyCode());
        assertEquals(49.99,money.getNumber().doubleValue(),0.001);
    }

    @Test
    public void
    testCreateWithCurrencyUnitAndLongNumber()
    {
        Money money =
            new MoneyFactory()
                .setCurrency(BasicCurrencyUnit.GBP)
                .setNumber(250L)
                .create();

        assertEquals("GBP",money.getCurrency().getCurrencyCode());
        assertEquals(250L,money.getNumber().longValue());
    }

    @Test
    public void
    testCreateWithCurrencyUnitAndBigDecimalNumber()
    {
        Money money =
            new MoneyFactory()
                .setCurrency(BasicCurrencyUnit.JPY)
                .setNumber(new BigDecimal("1000.50"))
                .create();

        assertEquals("JPY",money.getCurrency().getCurrencyCode());
        assertEquals(
            0,
            new BigDecimal("1000.50").compareTo(
                money.getNumber().numberValue(BigDecimal.class)));
    }

    @Test
    public void
    testCreateWithStringCurrencyCode()
    {
        Money money =
            new MoneyFactory()
                .setCurrency("CAD")
                .setNumber(75.25)
                .create();

        assertEquals("CAD",money.getCurrency().getCurrencyCode());
    }

    @Test
    public void
    testSetCurrencyStringUsesBasicCurrencyUnit()
    {
        Money money =
            new MoneyFactory()
                .setCurrency("CHF")
                .create();

        assertInstanceOf(BasicCurrencyUnit.class,money.getCurrency());
    }

    @Test
    public void
    testCreateWithoutCurrencyThrows()
    {
        MoneyFactory factory = new MoneyFactory();

        assertThrows(MonetaryException.class,factory::create);
    }

    @Test
    public void
    testCreateWithoutCurrencyButWithNumberThrows()
    {
        MoneyFactory factory =
            new MoneyFactory().setNumber(100.0);

        assertThrows(MonetaryException.class,factory::create);
    }

    @Test
    public void
    testFluentChaining()
    {
        MoneyFactory factory = new MoneyFactory();

        assertSame(factory,factory.setCurrency(BasicCurrencyUnit.USD));
        assertSame(factory,factory.setNumber(10.0));
        assertSame(factory,factory.setNumber(10L));
        assertSame(factory,factory.setNumber(BigDecimal.TEN));
    }

    @Test
    public void
    testSetCurrencyStringReturnsSameFactory()
    {
        MoneyFactory factory = new MoneyFactory();

        assertSame(factory,factory.setCurrency("USD"));
    }

    @Test
    public void
    testSetNumberWithInteger()
    {
        Money money =
            new MoneyFactory()
                .setCurrency(BasicCurrencyUnit.USD)
                .setNumber(Integer.valueOf(42))
                .create();

        assertEquals(42,money.getNumber().intValue());
    }

    @Test
    public void
    testSetNumberWithFloat()
    {
        Money money =
            new MoneyFactory()
                .setCurrency(BasicCurrencyUnit.USD)
                .setNumber(Float.valueOf(3.14f))
                .create();

        assertEquals(3.14,money.getNumber().doubleValue(),0.01);
    }

    @Test
    public void
    testOverwriteCurrency()
    {
        Money money =
            new MoneyFactory()
                .setCurrency(BasicCurrencyUnit.USD)
                .setCurrency(BasicCurrencyUnit.EUR)
                .setNumber(100)
                .create();

        assertEquals("EUR",money.getCurrency().getCurrencyCode());
    }

    @Test
    public void
    testOverwriteNumber()
    {
        Money money =
            new MoneyFactory()
                .setCurrency(BasicCurrencyUnit.USD)
                .setNumber(50.0)
                .setNumber(100L)
                .create();

        assertEquals(100L,money.getNumber().longValue());
    }

    @Test
    public void
    testGetDefaultMonetaryContextNotNull()
    {
        assertNotNull(new MoneyFactory().getDefaultMonetaryContext());
    }

    @Test
    public void
    testGetMaximalMonetaryContextNotNull()
    {
        assertNotNull(new MoneyFactory().getMaximalMonetaryContext());
    }

    @Test
    public void
    testGetMaxNumberReturnsNull()
    {
        assertNull(new MoneyFactory().getMaxNumber());
    }

    @Test
    public void
    testGetMinNumberReturnsNull()
    {
        assertNull(new MoneyFactory().getMinNumber());
    }

    @Test
    public void
    testSetContextReturnsSameFactory()
    {
        MoneyFactory factory = new MoneyFactory();

        assertSame(factory,factory.setContext(null));
    }
}

//////////////////////////////////////////////////////////////////////////////

