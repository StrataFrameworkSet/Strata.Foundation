//////////////////////////////////////////////////////////////////////////////
// RetailYearTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import strata.foundation.core.mapper.ObjectMapperSupplier;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.time.temporal.UnsupportedTemporalTypeException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("CommitStage")
public
class RetailYearTest
{
    private static final RetailCalendar NRF = RetailCalendar.NRF;

    private RetailYear
    roundTrip(RetailYear value) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(value);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (RetailYear)in.readObject();
        }
    }

    @Test
    public void
    testOf()
    {
        RetailYear value = RetailYear.of(NRF,2024);

        assertEquals(2024,value.getYear());
        assertEquals(LocalDate.of(2024,2,4),value.getFirstDay());
        assertEquals(LocalDate.of(2025,2,1),value.getLastDay());
        assertEquals(52,value.lengthInWeeks());
        assertFalse(value.is53WeekYear());
        assertEquals("2024",value.format());
        assertEquals("2024[NRF]",value.toString());
    }

    @Test
    public void
    testOf53WeekYear()
    {
        RetailYear value = RetailYear.of(NRF,2023);

        assertEquals(LocalDate.of(2024,2,3),value.getLastDay());
        assertEquals(53,value.lengthInWeeks());
        assertTrue(value.is53WeekYear());
    }

    @Test
    public void
    testFrom()
    {
        assertEquals(RetailYear.of(NRF,2023),RetailYear.from(NRF,LocalDate.of(2024,2,3)));
        assertEquals(RetailYear.of(NRF,2024),RetailYear.from(NRF,LocalDate.of(2024,2,4)));
    }

    @Test
    public void
    testConversions()
    {
        assertEquals(RetailYearQuarter.of(NRF,2024,4),RetailYear.of(NRF,2024).atQuarter(4));
        assertEquals(RetailYearMonth.of(NRF,2024,12),RetailYear.of(NRF,2024).atMonth(12));
        assertEquals(RetailYearWeek.of(NRF,2023,53),RetailYear.of(NRF,2023).atWeek(53));
        assertThrows(DateTimeException.class,() -> RetailYear.of(NRF,2024).atWeek(53));
    }

    @Test
    public void
    testAtDay()
    {
        assertEquals(LocalDate.of(2024,2,4),RetailYear.of(NRF,2024).atDay(1));
        assertEquals(LocalDate.of(2025,2,1),RetailYear.of(NRF,2024).atDay(364));
        assertEquals(LocalDate.of(2024,2,3),RetailYear.of(NRF,2023).atDay(371));
        assertThrows(DateTimeException.class,() -> RetailYear.of(NRF,2024).atDay(365));
    }

    @Test
    public void
    testArithmetic()
    {
        assertEquals(RetailYear.of(NRF,2024),RetailYear.of(NRF,2023).plusYears(1));
        assertEquals(RetailYear.of(NRF,2023),RetailYear.of(NRF,2024).minusYears(1));
        assertEquals(2L,RetailYear.of(NRF,2024).until(RetailYear.of(NRF,2026),NRF.years()));
        assertEquals(RetailYear.of(NRF,2025),RetailYear.of(NRF,2024).with(NRF.year(),2025));
    }

    @Test
    public void
    testPlusUnsupportedUnit()
    {
        assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> RetailYear.of(NRF,2024).plus(1,NRF.months()));
    }

    @Test
    public void
    testIsSupported()
    {
        RetailYear value = RetailYear.of(NRF,2024);

        assertTrue(value.isSupported(NRF.year()));
        assertFalse(value.isSupported(NRF.monthOfYear()));
        assertTrue(value.isSupported(NRF.years()));
        assertFalse(value.isSupported(NRF.months()));
    }

    @Test
    public void
    testGetLong()
    {
        assertEquals(2024L,RetailYear.of(NRF,2024).getLong(NRF.year()));
    }

    @Test
    public void
    testAdjustInto()
    {
        assertEquals(
            LocalDate.of(2025,4,9),
            LocalDate.of(2024,4,10).with(RetailYear.of(NRF,2025)));
    }

    @Test
    public void
    testNow()
    {
        Clock clock = Clock.fixed(Instant.parse("2024-02-04T12:00:00Z"),ZoneOffset.UTC);

        assertEquals(RetailYear.of(NRF,2024),RetailYear.now(NRF,clock));
    }

    @Test
    public void
    testParse()
    {
        assertEquals(RetailYear.of(NRF,2024),RetailYear.parse("2024[NRF]"));
        assertEquals(RetailYear.of(NRF,2024),RetailYear.parse("2024"));
    }

    @Test
    public void
    testParseInvalid()
    {
        assertThrows(DateTimeParseException.class,() -> RetailYear.parse("24"));
        assertThrows(DateTimeParseException.class,() -> RetailYear.parse("2024[UNKNOWN_CAL]"));
    }

    @Test
    public void
    testCompareToDifferentCalendar()
    {
        RetailCalendar other =
            RetailCalendar.of(
                "Y_T445",
                RetailWeekPattern.FOUR_FOUR_FIVE,
                Month.JANUARY,
                DayOfWeek.SATURDAY,
                RetailYearEndRule.NEAREST_END_OF_MONTH,
                CalendarYearLabel.START_YEAR);

        assertThrows(
            IllegalArgumentException.class,
            () -> RetailYear.of(NRF,2024).compareTo(RetailYear.of(other,2024)));
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        RetailYear value = RetailYear.of(NRF,2024);

        assertEquals(value,RetailYear.of(NRF,2024));
        assertEquals(value.hashCode(),RetailYear.of(NRF,2024).hashCode());
        assertNotEquals(value,RetailYear.of(NRF,2025));
        assertNotEquals(null,value);
    }

    @Test
    public void
    testSerialization() throws IOException, ClassNotFoundException
    {
        RetailYear expected = RetailYear.of(NRF,2024);

        assertEquals(expected,roundTrip(expected));
    }

    @Test
    public void
    testJsonWrite() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();

        assertEquals(
            "{\"calendar\":\"NRF\",\"value\":\"2024\"}",
            mapper.writeValueAsString(RetailYear.of(NRF,2024)));
    }

    @Test
    public void
    testJsonRead() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();

        assertEquals(
            RetailYear.of(NRF,2024),
            mapper.readValue("{\"calendar\":\"NRF\",\"value\":\"2024\"}",RetailYear.class));
    }
}

//////////////////////////////////////////////////////////////////////////////
