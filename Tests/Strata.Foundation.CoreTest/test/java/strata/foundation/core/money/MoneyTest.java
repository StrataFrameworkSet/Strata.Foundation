//////////////////////////////////////////////////////////////////////////////
// MoneyTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.money;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import strata.foundation.core.mapper.ObjectMapperSupplier;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.MonetaryException;
import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class MoneyTest
{
    // -----------------------------------------------------------------------
    // factory methods
    // -----------------------------------------------------------------------

    @Test
    public void
    testOfCurrencyAndBigDecimal()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("100.50"));

        assertEquals("USD",money.getCurrency().getCurrencyCode());
        assertEquals(
            0,
            new BigDecimal("100.50").compareTo(
                money.getNumber().numberValue(BigDecimal.class)));
    }

    @Test
    public void
    testOfCurrencyAndDouble()
    {
        Money money = Money.of(BasicCurrencyUnit.EUR,49.99);

        assertEquals("EUR",money.getCurrency().getCurrencyCode());
        assertEquals(49.99,money.getNumber().doubleValue(),0.001);
    }

    @Test
    public void
    testOfCurrencyAndLong()
    {
        Money money = Money.of(BasicCurrencyUnit.GBP,250L);

        assertEquals("GBP",money.getCurrency().getCurrencyCode());
        assertEquals(250L,money.getNumber().longValue());
    }

    @Test
    public void
    testOfCurrencyAndNumberValue()
    {
        BigDecimalValue nv = BigDecimalValue.of(new BigDecimal("75.25"));
        Money money = Money.of(BasicCurrencyUnit.CAD,nv);

        assertEquals("CAD",money.getCurrency().getCurrencyCode());
        assertEquals(
            0,
            new BigDecimal("75.25").compareTo(
                money.getNumber().numberValue(BigDecimal.class)));
    }

    // -----------------------------------------------------------------------
    // getContext / getFactory
    // -----------------------------------------------------------------------

    @Test
    public void
    testGetContextNotNull()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,BigDecimal.TEN);

        assertNotNull(money.getContext());
    }

    @Test
    public void
    testGetFactoryNotNull()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,BigDecimal.TEN);

        assertNotNull(money.getFactory());
    }

    @Test
    public void
    testGetFactoryCreatesMoney()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,BigDecimal.TEN);
        Money created = money.getFactory().setNumber(20).create();

        assertEquals("USD",created.getCurrency().getCurrencyCode());
        assertEquals(20L,created.getNumber().longValue());
    }

    // -----------------------------------------------------------------------
    // signum
    // -----------------------------------------------------------------------

    @Test
    public void
    testSignumPositive()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));

        assertEquals(1,money.signum());
    }

    @Test
    public void
    testSignumZero()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,BigDecimal.ZERO);

        assertEquals(0,money.signum());
    }

    @Test
    public void
    testSignumNegative()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("-5.00"));

        assertEquals(-1,money.signum());
    }

    // -----------------------------------------------------------------------
    // comparison methods
    // -----------------------------------------------------------------------

    @Test
    public void
    testIsGreaterThan()
    {
        Money a = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        Money b = Money.of(BasicCurrencyUnit.USD,new BigDecimal("5.00"));

        assertTrue(a.isGreaterThan(b));
        assertFalse(b.isGreaterThan(a));
    }

    @Test
    public void
    testIsGreaterThanOrEqualTo()
    {
        Money a = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        Money b = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        Money c = Money.of(BasicCurrencyUnit.USD,new BigDecimal("5.00"));

        assertTrue(a.isGreaterThanOrEqualTo(b));
        assertTrue(a.isGreaterThanOrEqualTo(c));
        assertFalse(c.isGreaterThanOrEqualTo(a));
    }

    @Test
    public void
    testIsLessThan()
    {
        Money a = Money.of(BasicCurrencyUnit.USD,new BigDecimal("3.00"));
        Money b = Money.of(BasicCurrencyUnit.USD,new BigDecimal("7.00"));

        assertTrue(a.isLessThan(b));
        assertFalse(b.isLessThan(a));
    }

    @Test
    public void
    testIsLessThanOrEqualTo()
    {
        Money a = Money.of(BasicCurrencyUnit.USD,new BigDecimal("7.00"));
        Money b = Money.of(BasicCurrencyUnit.USD,new BigDecimal("7.00"));
        Money c = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));

        assertTrue(a.isLessThanOrEqualTo(b));
        assertTrue(a.isLessThanOrEqualTo(c));
        assertFalse(c.isLessThanOrEqualTo(a));
    }

    @Test
    public void
    testIsEqualTo()
    {
        Money a = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        Money b = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        Money c = Money.of(BasicCurrencyUnit.USD,new BigDecimal("5.00"));

        assertTrue(a.isEqualTo(b));
        assertFalse(a.isEqualTo(c));
    }

    @Test
    public void
    testIsEqualToIgnoresScale()
    {
        Money a = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.0"));
        Money b = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));

        assertTrue(a.isEqualTo(b));
    }

    @Test
    public void
    testComparisonWithDifferentCurrencyThrows()
    {
        Money usd = Money.of(BasicCurrencyUnit.USD,BigDecimal.TEN);
        Money eur = Money.of(BasicCurrencyUnit.EUR,BigDecimal.TEN);

        assertThrows(MonetaryException.class,() -> usd.isGreaterThan(eur));
        assertThrows(MonetaryException.class,() -> usd.isLessThan(eur));
        assertThrows(MonetaryException.class,() -> usd.isEqualTo(eur));
    }

    // -----------------------------------------------------------------------
    // add / subtract
    // -----------------------------------------------------------------------

    @Test
    public void
    testAdd()
    {
        Money a = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.50"));
        Money b = Money.of(BasicCurrencyUnit.USD,new BigDecimal("4.25"));
        MonetaryAmount result = a.add(b);

        assertEquals(
            0,
            new BigDecimal("14.75").compareTo(
                result.getNumber().numberValue(BigDecimal.class)));
        assertEquals("USD",result.getCurrency().getCurrencyCode());
    }

    @Test
    public void
    testSubtract()
    {
        Money a = Money.of(BasicCurrencyUnit.USD,new BigDecimal("20.00"));
        Money b = Money.of(BasicCurrencyUnit.USD,new BigDecimal("7.50"));
        MonetaryAmount result = a.subtract(b);

        assertEquals(
            0,
            new BigDecimal("12.50").compareTo(
                result.getNumber().numberValue(BigDecimal.class)));
    }

    @Test
    public void
    testAddDifferentCurrencyThrows()
    {
        Money usd = Money.of(BasicCurrencyUnit.USD,BigDecimal.TEN);
        Money eur = Money.of(BasicCurrencyUnit.EUR,BigDecimal.ONE);

        assertThrows(MonetaryException.class,() -> usd.add(eur));
    }

    @Test
    public void
    testSubtractDifferentCurrencyThrows()
    {
        Money usd = Money.of(BasicCurrencyUnit.USD,BigDecimal.TEN);
        Money eur = Money.of(BasicCurrencyUnit.EUR,BigDecimal.ONE);

        assertThrows(MonetaryException.class,() -> usd.subtract(eur));
    }

    // -----------------------------------------------------------------------
    // multiply
    // -----------------------------------------------------------------------

    @Test
    public void
    testMultiplyByLong()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("5.00"));
        MonetaryAmount result = money.multiply(3L);

        assertEquals(
            0,
            new BigDecimal("15.00").compareTo(
                result.getNumber().numberValue(BigDecimal.class)));
    }

    @Test
    public void
    testMultiplyByDouble()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        MonetaryAmount result = money.multiply(1.5);

        assertEquals(
            0,
            new BigDecimal("15.000").compareTo(
                result.getNumber().numberValue(BigDecimal.class)));
    }

    @Test
    public void
    testMultiplyByNumber()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("4.00"));
        MonetaryAmount result = money.multiply(new BigDecimal("2.5"));

        assertEquals(
            0,
            new BigDecimal("10.000").compareTo(
                result.getNumber().numberValue(BigDecimal.class)));
    }

    @Test
    public void
    testMultiplyByZero()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("99.99"));
        MonetaryAmount result = money.multiply(0L);

        assertEquals(0,result.signum());
    }

    // -----------------------------------------------------------------------
    // divide
    // -----------------------------------------------------------------------

    @Test
    public void
    testDivideByLong()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("15.00"));
        MonetaryAmount result = money.divide(3L);

        assertEquals(
            0,
            new BigDecimal("5").compareTo(
                result.getNumber().numberValue(BigDecimal.class)));
    }

    @Test
    public void
    testDivideByDouble()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        MonetaryAmount result = money.divide(2.5);

        assertEquals(
            0,
            new BigDecimal("4").compareTo(
                result.getNumber().numberValue(BigDecimal.class)));
    }

    @Test
    public void
    testDivideByNumber()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("100.00"));
        MonetaryAmount result = money.divide(new BigDecimal("4"));

        assertEquals(
            0,
            new BigDecimal("25").compareTo(
                result.getNumber().numberValue(BigDecimal.class)));
    }

    // -----------------------------------------------------------------------
    // remainder
    // -----------------------------------------------------------------------

    @Test
    public void
    testRemainderByLong()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        MonetaryAmount result = money.remainder(3L);

        assertEquals(
            0,
            new BigDecimal("1.00").compareTo(
                result.getNumber().numberValue(BigDecimal.class)));
    }

    @Test
    public void
    testRemainderByDouble()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        MonetaryAmount result = money.remainder(3.0);

        assertEquals(
            0,
            new BigDecimal("1").compareTo(
                result.getNumber().numberValue(BigDecimal.class)));
    }

    @Test
    public void
    testRemainderByNumber()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        MonetaryAmount result = money.remainder(new BigDecimal("3"));

        assertEquals(
            0,
            new BigDecimal("1").compareTo(
                result.getNumber().numberValue(BigDecimal.class)));
    }

    // -----------------------------------------------------------------------
    // divideAndRemainder
    // -----------------------------------------------------------------------

    @Test
    public void
    testDivideAndRemainderByLong()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        MonetaryAmount[] result = money.divideAndRemainder(3L);

        assertEquals(2,result.length);
        assertEquals(
            0,
            new BigDecimal("3").compareTo(
                result[0].getNumber().numberValue(BigDecimal.class)));
        assertEquals(
            0,
            new BigDecimal("1.00").compareTo(
                result[1].getNumber().numberValue(BigDecimal.class)));
    }

    @Test
    public void
    testDivideAndRemainderByDouble()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        MonetaryAmount[] result = money.divideAndRemainder(3.0);

        assertEquals(2,result.length);
    }

    @Test
    public void
    testDivideAndRemainderByNumber()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        MonetaryAmount[] result =
            money.divideAndRemainder(new BigDecimal("3"));

        assertEquals(2,result.length);
        assertEquals("USD",result[0].getCurrency().getCurrencyCode());
        assertEquals("USD",result[1].getCurrency().getCurrencyCode());
    }

    // -----------------------------------------------------------------------
    // divideToIntegralValue
    // -----------------------------------------------------------------------

    @Test
    public void
    testDivideToIntegralValueByLong()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        MonetaryAmount result = money.divideToIntegralValue(3L);

        assertEquals(
            0,
            new BigDecimal("3").compareTo(
                result.getNumber().numberValue(BigDecimal.class)));
    }

    @Test
    public void
    testDivideToIntegralValueByDouble()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        MonetaryAmount result = money.divideToIntegralValue(3.0);

        assertEquals(3L,result.getNumber().longValue());
    }

    @Test
    public void
    testDivideToIntegralValueByNumber()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        MonetaryAmount result =
            money.divideToIntegralValue(new BigDecimal("3"));

        assertEquals(3L,result.getNumber().longValue());
    }

    // -----------------------------------------------------------------------
    // scaleByPowerOfTen
    // -----------------------------------------------------------------------

    @Test
    public void
    testScaleByPowerOfTen()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("1.50"));
        MonetaryAmount result = money.scaleByPowerOfTen(2);

        assertEquals(
            0,
            new BigDecimal("150").compareTo(
                result.getNumber().numberValue(BigDecimal.class)));
    }

    @Test
    public void
    testScaleByPowerOfTenNegative()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("150"));
        MonetaryAmount result = money.scaleByPowerOfTen(-2);

        assertEquals(
            0,
            new BigDecimal("1.50").compareTo(
                result.getNumber().numberValue(BigDecimal.class)));
    }

    // -----------------------------------------------------------------------
    // abs / negate / plus / stripTrailingZeros
    // -----------------------------------------------------------------------

    @Test
    public void
    testAbsPositive()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        MonetaryAmount result = money.abs();

        assertEquals(
            0,
            new BigDecimal("10.00").compareTo(
                result.getNumber().numberValue(BigDecimal.class)));
    }

    @Test
    public void
    testAbsNegative()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("-10.00"));
        MonetaryAmount result = money.abs();

        assertEquals(
            0,
            new BigDecimal("10.00").compareTo(
                result.getNumber().numberValue(BigDecimal.class)));
    }

    @Test
    public void
    testNegate()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        MonetaryAmount result = money.negate();

        assertEquals(
            0,
            new BigDecimal("-10.00").compareTo(
                result.getNumber().numberValue(BigDecimal.class)));
    }

    @Test
    public void
    testNegateNegative()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("-5.00"));
        MonetaryAmount result = money.negate();

        assertEquals(
            0,
            new BigDecimal("5.00").compareTo(
                result.getNumber().numberValue(BigDecimal.class)));
    }

    @Test
    public void
    testPlus()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        MonetaryAmount result = money.plus();

        assertEquals(
            0,
            new BigDecimal("10.00").compareTo(
                result.getNumber().numberValue(BigDecimal.class)));
    }

    @Test
    public void
    testStripTrailingZeros()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        MonetaryAmount result = money.stripTrailingZeros();

        assertEquals(
            0,
            new BigDecimal("10").compareTo(
                result.getNumber().numberValue(BigDecimal.class)));
    }

    // -----------------------------------------------------------------------
    // compareTo
    // -----------------------------------------------------------------------

    @Test
    public void
    testCompareTo()
    {
        Money a = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        Money b = Money.of(BasicCurrencyUnit.USD,new BigDecimal("5.00"));
        Money c = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));

        assertTrue(a.compareTo(b) > 0);
        assertTrue(b.compareTo(a) < 0);
        assertEquals(0,a.compareTo(c));
    }

    @Test
    public void
    testCompareToDifferentCurrencyThrows()
    {
        Money usd = Money.of(BasicCurrencyUnit.USD,BigDecimal.TEN);
        Money eur = Money.of(BasicCurrencyUnit.EUR,BigDecimal.TEN);

        assertThrows(MonetaryException.class,() -> usd.compareTo(eur));
    }

    // -----------------------------------------------------------------------
    // equals / hashCode
    // -----------------------------------------------------------------------

    @Test
    public void
    testEqualsSameValues()
    {
        Money a = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        Money b = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));

        assertEquals(a,b);
    }

    @Test
    public void
    testEqualsIgnoresScale()
    {
        Money a = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.0"));
        Money b = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));

        assertEquals(a,b);
    }

    @Test
    public void
    testEqualsDifferentAmounts()
    {
        Money a = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        Money b = Money.of(BasicCurrencyUnit.USD,new BigDecimal("20.00"));

        assertNotEquals(a,b);
    }

    @Test
    public void
    testEqualsDifferentCurrencies()
    {
        Money a = Money.of(BasicCurrencyUnit.USD,BigDecimal.TEN);
        Money b = Money.of(BasicCurrencyUnit.EUR,BigDecimal.TEN);

        assertNotEquals(a,b);
    }

    @Test
    public void
    testEqualsNull()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,BigDecimal.TEN);

        assertNotEquals(null,money);
    }

    @Test
    public void
    testEqualsSameInstance()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,BigDecimal.TEN);

        assertEquals(money,money);
    }

    @Test
    public void
    testHashCodeConsistentWithEquals()
    {
        Money a = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));
        Money b = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));

        assertEquals(a.hashCode(),b.hashCode());
    }

    @Test
    public void
    testHashCodeIgnoresScale()
    {
        Money a = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.0"));
        Money b = Money.of(BasicCurrencyUnit.USD,new BigDecimal("10.00"));

        assertEquals(a.hashCode(),b.hashCode());
    }

    // -----------------------------------------------------------------------
    // toString
    // -----------------------------------------------------------------------

    @Test
    public void
    testToString()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("99.95"));

        assertEquals("USD99.95",money.toString());
    }

    @Test
    public void
    testToStringWholeNumber()
    {
        Money money = Money.of(BasicCurrencyUnit.EUR,new BigDecimal("100"));

        assertEquals("EUR100",money.toString());
    }

    @Test
    public void
    testToStringNegative()
    {
        Money money = Money.of(BasicCurrencyUnit.GBP,new BigDecimal("-25.50"));

        assertEquals("GBP-25.50",money.toString());
    }

    // -----------------------------------------------------------------------
    // arithmetic preserves currency
    // -----------------------------------------------------------------------

    @Test
    public void
    testAllArithmeticPreservesCurrency()
    {
        Money money = Money.of(BasicCurrencyUnit.JPY,new BigDecimal("1000"));
        Money other = Money.of(BasicCurrencyUnit.JPY,new BigDecimal("200"));

        assertEquals("JPY",money.add(other).getCurrency().getCurrencyCode());
        assertEquals("JPY",money.subtract(other).getCurrency().getCurrencyCode());
        assertEquals("JPY",money.multiply(2L).getCurrency().getCurrencyCode());
        assertEquals("JPY",money.multiply(1.5).getCurrency().getCurrencyCode());
        assertEquals("JPY",money.multiply(BigDecimal.TWO).getCurrency().getCurrencyCode());
        assertEquals("JPY",money.divide(2L).getCurrency().getCurrencyCode());
        assertEquals("JPY",money.divide(2.0).getCurrency().getCurrencyCode());
        assertEquals("JPY",money.divide(BigDecimal.TWO).getCurrency().getCurrencyCode());
        assertEquals("JPY",money.remainder(3L).getCurrency().getCurrencyCode());
        assertEquals("JPY",money.abs().getCurrency().getCurrencyCode());
        assertEquals("JPY",money.negate().getCurrency().getCurrencyCode());
        assertEquals("JPY",money.plus().getCurrency().getCurrencyCode());
        assertEquals("JPY",money.stripTrailingZeros().getCurrency().getCurrencyCode());
        assertEquals("JPY",money.scaleByPowerOfTen(1).getCurrency().getCurrencyCode());
    }

    // -----------------------------------------------------------------------
    // edge cases
    // -----------------------------------------------------------------------

    @Test
    public void
    testZeroAmount()
    {
        Money money = Money.of(BasicCurrencyUnit.USD,BigDecimal.ZERO);

        assertEquals(0,money.signum());
        assertTrue(money.isEqualTo(Money.of(BasicCurrencyUnit.USD,BigDecimal.ZERO)));
    }

    @Test
    public void
    testLargeAmount()
    {
        Money money = Money.of(
            BasicCurrencyUnit.USD,
            new BigDecimal("999999999999999.99"));

        assertEquals(
            0,
            new BigDecimal("999999999999999.99").compareTo(
                money.getNumber().numberValue(BigDecimal.class)));
    }

    // -----------------------------------------------------------------------
    // JSON serialization / deserialization
    // -----------------------------------------------------------------------

    private static final List<CurrencyUnit> ALL_CURRENCIES = List.of(
        BasicCurrencyUnit.USD,BasicCurrencyUnit.EUR,BasicCurrencyUnit.GBP,
        BasicCurrencyUnit.JPY,BasicCurrencyUnit.CHF,BasicCurrencyUnit.CAD,
        BasicCurrencyUnit.AUD,BasicCurrencyUnit.CNY,BasicCurrencyUnit.INR,
        BasicCurrencyUnit.KRW,BasicCurrencyUnit.BRL,BasicCurrencyUnit.MXN,
        BasicCurrencyUnit.SGD,BasicCurrencyUnit.HKD,BasicCurrencyUnit.NZD,
        BasicCurrencyUnit.SEK,BasicCurrencyUnit.NOK,BasicCurrencyUnit.DKK,
        BasicCurrencyUnit.ZAR,BasicCurrencyUnit.PLN);

    private static final List<BigDecimal> AMOUNT_CASES = List.of(
        BigDecimal.ZERO,
        new BigDecimal("0.01"),
        new BigDecimal("1.00"),
        new BigDecimal("99.95"),
        new BigDecimal("1234.56"),
        new BigDecimal("999999999999.99"),
        new BigDecimal("-0.01"),
        new BigDecimal("-1.00"),
        new BigDecimal("-99.95"),
        new BigDecimal("-1234.56"),
        new BigDecimal("-999999999999.99"));

    public static Stream<Arguments>
    serializeDeserializeInputs()
    {
        return ALL_CURRENCIES
            .stream()
            .flatMap(
                currency -> AMOUNT_CASES
                    .stream()
                    .map(amount -> Arguments.of(currency,amount)));
    }

    @ParameterizedTest
    @MethodSource("serializeDeserializeInputs")
    public void
    testSerializeAndDeserializeRoundTrip(
        CurrencyUnit currency,
        BigDecimal   amount) throws JsonProcessingException
    {
        ObjectMapper mapper   = new ObjectMapperSupplier().get();
        Money        expected = Money.of(currency,amount);
        String       json     = mapper.writeValueAsString(expected);
        Money        actual   = mapper.readValue(json,Money.class);

        assertEquals(expected,actual);
        assertEquals(
            currency.getCurrencyCode(),
            actual.getCurrency().getCurrencyCode());
        assertEquals(
            0,
            amount.compareTo(
                actual.getNumber().numberValue(BigDecimal.class)));
    }

    @Test
    public void
    testSerializeToJson() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();
        Money money = Money.of(BasicCurrencyUnit.USD,new BigDecimal("99.95"));
        String json = mapper.writeValueAsString(money);

        assertTrue(json.contains("\"money\""));
        assertTrue(json.contains("USD99.95"));
    }

    @Test
    public void
    testDeserializeFromJson() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();
        String json = "{\"money\":\"USD99.95\"}";
        Money money = mapper.readValue(json,Money.class);

        assertEquals("USD",money.getCurrency().getCurrencyCode());
        assertEquals(
            0,
            new BigDecimal("99.95").compareTo(
                money.getNumber().numberValue(BigDecimal.class)));
    }


    @Test
    public void
    testDeserializePreservesCurrency() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();
        String json = "{\"money\":\"AUD250.00\"}";
        Money money = mapper.readValue(json,Money.class);

        assertEquals("AUD",money.getCurrency().getCurrencyCode());
    }

    @Test
    public void
    testDeserializePreservesAmount() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();
        String json = "{\"money\":\"EUR1000.50\"}";
        Money money = mapper.readValue(json,Money.class);

        assertEquals(
            0,
            new BigDecimal("1000.50").compareTo(
                money.getNumber().numberValue(BigDecimal.class)));
    }

    // -----------------------------------------------------------------------
    // Java serialization / deserialization
    // -----------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("serializeDeserializeInputs")
    public void
    testJavaSerializationRoundTrip(
        CurrencyUnit currency,
        BigDecimal   amount) throws IOException,ClassNotFoundException
    {
        Money expected = Money.of(currency,amount);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream    oos  = new ObjectOutputStream(baos);

        oos.writeObject(expected);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream    ois  = new ObjectInputStream(bais);
        Money                actual = (Money)ois.readObject();

        ois.close();

        assertEquals(expected,actual);
        assertEquals(
            currency.getCurrencyCode(),
            actual.getCurrency().getCurrencyCode());
        assertEquals(
            0,
            amount.compareTo(
                actual.getNumber().numberValue(BigDecimal.class)));
        assertNotNull(actual.getContext());
    }
}

//////////////////////////////////////////////////////////////////////////////

