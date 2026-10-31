//////////////////////////////////////////////////////////////////////////////
// FiscalYearTest.java
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
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.UnsupportedTemporalTypeException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("CommitStage")
public
class FiscalYearTest
{
    private static final FiscalCalendar US  = FiscalCalendar.US_FEDERAL;
    private static final FiscalCalendar CAL = FiscalCalendar.CALENDAR_YEAR;
    private static final FiscalCalendar UK  = FiscalCalendar.UK_GOVERNMENT;

    private FiscalYear
    roundTrip(FiscalYear value) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(value);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (FiscalYear)in.readObject();
        }
    }

    @Test
    public void
    testOf()
    {
        FiscalYear value = FiscalYear.of(US,2025);

        assertEquals(2025,value.getYear());
        assertEquals(LocalDate.of(2024,10,1),value.getFirstDay());
        assertEquals(LocalDate.of(2025,9,30),value.getLastDay());
        assertEquals(365,value.lengthInDays());
        assertFalse(value.isLeap());
        assertEquals("2025",value.format());
        assertEquals("2025[US_FEDERAL]",value.toString());
    }

    @Test
    public void
    testOfLeap()
    {
        FiscalYear value = FiscalYear.of(US,2024);

        assertEquals(366,value.lengthInDays());
        assertTrue(value.isLeap());
    }

    @Test
    public void
    testOfCalendarYear()
    {
        assertEquals(LocalDate.of(2025,1,1),FiscalYear.of(CAL,2025).getFirstDay());
    }

    @Test
    public void
    testFrom()
    {
        assertEquals(FiscalYear.of(US,2025),FiscalYear.from(US,LocalDate.of(2024,10,1)));
        assertEquals(FiscalYear.of(US,2024),FiscalYear.from(US,LocalDate.of(2024,9,30)));
    }

    @Test
    public void
    testConversions()
    {
        FiscalYear value = FiscalYear.of(US,2025);

        assertEquals(FiscalYearQuarter.of(US,2025,2),value.atQuarter(2));
        assertEquals(FiscalYearMonth.of(US,2025,12),value.atMonth(12));
    }

    @Test
    public void
    testAtDay()
    {
        FiscalYear value = FiscalYear.of(US,2025);

        assertEquals(LocalDate.of(2024,10,1),value.atDay(1));
        assertEquals(LocalDate.of(2025,9,30),value.atDay(365));
        assertThrows(DateTimeException.class,() -> value.atDay(366));
    }

    @Test
    public void
    testPlusAndMinus()
    {
        assertEquals(FiscalYear.of(US,2026),FiscalYear.of(US,2025).plusYears(1));
        assertEquals(FiscalYear.of(US,2024),FiscalYear.of(US,2025).minusYears(1));
    }

    @Test
    public void
    testPlusUnsupportedUnit()
    {
        assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> FiscalYear.of(US,2025).plus(1,ChronoUnit.MONTHS));
    }

    @Test
    public void
    testUntil()
    {
        assertEquals(2L,FiscalYear.of(US,2025).until(FiscalYear.of(US,2027),ChronoUnit.YEARS));
    }

    @Test
    public void
    testAdjustInto()
    {
        assertEquals(
            LocalDate.of(2026,2,28),
            LocalDate.of(2024,2,29).with(FiscalYear.of(US,2026)));
    }

    @Test
    public void
    testParse()
    {
        assertEquals(FiscalYear.of(US,2025),FiscalYear.parse("2025[US_FEDERAL]"));
        assertEquals(FiscalYear.of(CAL,2025),FiscalYear.parse("2025"));
    }

    @Test
    public void
    testParseInvalid()
    {
        assertThrows(DateTimeParseException.class,() -> FiscalYear.parse("25"));
    }

    @Test
    public void
    testCompareTo()
    {
        FiscalYear first  = FiscalYear.of(US,2025);
        FiscalYear second = FiscalYear.of(US,2026);

        assertTrue(first.compareTo(second) < 0);
        assertTrue(second.compareTo(first) > 0);
        assertEquals(0,first.compareTo(FiscalYear.of(US,2025)));
    }

    @Test
    public void
    testCompareToDifferentCalendar()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> FiscalYear.of(US,2025).compareTo(FiscalYear.of(UK,2025)));
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        FiscalYear value = FiscalYear.of(US,2025);

        assertEquals(value,FiscalYear.of(US,2025));
        assertEquals(value.hashCode(),FiscalYear.of(US,2025).hashCode());
        assertNotEquals(value,FiscalYear.of(US,2026));
        assertNotEquals(value,FiscalYear.of(UK,2025));
        assertNotEquals(null,value);
    }

    @Test
    public void
    testSerialization() throws IOException, ClassNotFoundException
    {
        FiscalYear expected = FiscalYear.of(US,2025);

        assertEquals(expected,roundTrip(expected));
    }

    @Test
    public void
    testJsonWrite() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();

        assertEquals(
            "{\"calendar\":\"US_FEDERAL\",\"value\":\"2025\"}",
            mapper.writeValueAsString(FiscalYear.of(US,2025)));
    }

    @Test
    public void
    testJsonRead() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();

        assertEquals(
            FiscalYear.of(US,2025),
            mapper.readValue(
                "{\"calendar\":\"US_FEDERAL\",\"value\":\"2025\"}",
                FiscalYear.class));
    }
}

//////////////////////////////////////////////////////////////////////////////
