//////////////////////////////////////////////////////////////////////////////
// FiscalYearMonthTest.java
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.UnsupportedTemporalTypeException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("CommitStage")
public
class FiscalYearMonthTest
{
    private static final FiscalCalendar US  = FiscalCalendar.US_FEDERAL;
    private static final FiscalCalendar CAL = FiscalCalendar.CALENDAR_YEAR;
    private static final FiscalCalendar UK  = FiscalCalendar.UK_GOVERNMENT;

    private FiscalYearMonth
    roundTrip(FiscalYearMonth value) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(value);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (FiscalYearMonth)in.readObject();
        }
    }

    @Test
    public void
    testOf()
    {
        FiscalYearMonth value = FiscalYearMonth.of(US,2025,1);

        assertEquals(2025,value.getYear());
        assertEquals(1,value.getMonth());
        assertEquals(LocalDate.of(2024,10,1),value.getFirstDay());
        assertEquals(LocalDate.of(2024,10,31),value.getLastDay());
        assertEquals(31,value.lengthInDays());
        assertEquals("2025-M01",value.format());
        assertEquals("2025-M01[US_FEDERAL]",value.toString());
        assertEquals(YearMonth.of(2024,10),value.toYearMonth());
    }

    @Test
    public void
    testOfFebruary()
    {
        assertEquals(LocalDate.of(2025,2,1),FiscalYearMonth.of(US,2025,5).getFirstDay());
        assertEquals(28,FiscalYearMonth.of(US,2025,5).lengthInDays());
        assertEquals(29,FiscalYearMonth.of(US,2024,5).lengthInDays());
    }

    @Test
    public void
    testOfInvalid()
    {
        assertThrows(DateTimeException.class,() -> FiscalYearMonth.of(US,2025,13));
    }

    @Test
    public void
    testFrom()
    {
        assertEquals(FiscalYearMonth.of(US,2025,1),FiscalYearMonth.from(US,LocalDate.of(2024,10,15)));
        assertEquals(FiscalYearMonth.of(US,2024,12),FiscalYearMonth.from(US,LocalDate.of(2024,9,30)));
        assertEquals(FiscalYearMonth.of(US,2025,5),FiscalYearMonth.from(US,YearMonth.of(2025,2)));
        assertEquals(FiscalYearMonth.of(US,2025,4),FiscalYearMonth.from(US,Year.of(2025)));
    }

    @Test
    public void
    testFromUnsupported()
    {
        assertThrows(
            DateTimeException.class,
            () -> FiscalYearMonth.from(US,LocalTime.of(10,0)));
    }

    @Test
    public void
    testPlusAndMinus()
    {
        assertEquals(FiscalYearMonth.of(US,2026,1),FiscalYearMonth.of(US,2025,10).plusMonths(3));
        assertEquals(FiscalYearMonth.of(US,2024,12),FiscalYearMonth.of(US,2025,1).minusMonths(1));
        assertEquals(FiscalYearMonth.of(US,2026,1),FiscalYearMonth.of(US,2025,1).plusYears(1));
        assertEquals(
            FiscalYearMonth.of(US,2025,4),
            FiscalYearMonth.of(US,2025,1).plus(1,IsoFields.QUARTER_YEARS));
    }

    @Test
    public void
    testPlusUnsupportedUnit()
    {
        assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> FiscalYearMonth.of(US,2025,1).plus(1,ChronoUnit.WEEKS));
    }

    @Test
    public void
    testUntil()
    {
        FiscalYearMonth start = FiscalYearMonth.of(US,2025,1);
        FiscalYearMonth end   = FiscalYearMonth.of(US,2026,1);

        assertEquals(12L,start.until(end,ChronoUnit.MONTHS));
        assertEquals(4L,start.until(end,IsoFields.QUARTER_YEARS));
        assertEquals(1L,start.until(end,ChronoUnit.YEARS));
    }

    @Test
    public void
    testWith()
    {
        assertEquals(FiscalYearMonth.of(US,2025,7),FiscalYearMonth.of(US,2025,1).with(US.monthOfYear(),7));
        assertEquals(FiscalYearMonth.of(US,2025,8),FiscalYearMonth.of(US,2025,2).with(US.quarterOfYear(),3));
        assertEquals(FiscalYearMonth.of(US,2025,3),FiscalYearMonth.of(US,2025,1).with(US.monthOfQuarter(),3));
        assertEquals(FiscalYearMonth.of(US,2026,1),FiscalYearMonth.of(US,2025,1).withYear(2026));
    }

    @Test
    public void
    testWithInvalid()
    {
        assertThrows(DateTimeException.class,() -> FiscalYearMonth.of(US,2025,1).withMonth(13));
    }

    @Test
    public void
    testGetLong()
    {
        FiscalYearMonth value = FiscalYearMonth.of(US,2025,5);

        assertEquals(2025L,value.getLong(US.year()));
        assertEquals(2L,value.getLong(US.quarterOfYear()));
        assertEquals(5L,value.getLong(US.monthOfYear()));
        assertEquals(2L,value.getLong(US.monthOfQuarter()));
    }

    @Test
    public void
    testIsSupported()
    {
        FiscalYearMonth value = FiscalYearMonth.of(US,2025,5);

        assertFalse(value.isSupported(US.dayOfYear()));
        assertFalse(value.isSupported(UK.monthOfYear()));
        assertTrue(value.isSupported(ChronoUnit.MONTHS));
        assertTrue(value.isSupported(IsoFields.QUARTER_YEARS));
        assertFalse(value.isSupported(ChronoUnit.DAYS));
    }

    @Test
    public void
    testConversions()
    {
        FiscalYearMonth value = FiscalYearMonth.of(US,2025,5);

        assertEquals(FiscalYear.of(US,2025),value.getFiscalYear());
        assertEquals(FiscalYearQuarter.of(US,2025,2),value.getFiscalYearQuarter());
    }

    @Test
    public void
    testAtDay()
    {
        assertEquals(LocalDate.of(2025,2,28),FiscalYearMonth.of(US,2025,5).atDay(28));
        assertThrows(DateTimeException.class,() -> FiscalYearMonth.of(US,2025,5).atDay(29));
        assertEquals(LocalDate.of(2024,2,29),FiscalYearMonth.of(US,2024,5).atDay(29));
    }

    @Test
    public void
    testAdjustInto()
    {
        assertEquals(
            LocalDate.of(2025,2,28),
            LocalDate.of(2025,1,31).with(FiscalYearMonth.of(US,2025,5)));
    }

    @Test
    public void
    testNow()
    {
        Clock clock = Clock.fixed(Instant.parse("2024-10-10T12:00:00Z"),ZoneOffset.UTC);

        assertEquals(FiscalYearMonth.of(US,2025,1),FiscalYearMonth.now(US,clock));
    }

    @Test
    public void
    testParse()
    {
        assertEquals(FiscalYearMonth.of(US,2025,1),FiscalYearMonth.parse("2025-M01[US_FEDERAL]"));
        assertEquals(FiscalYearMonth.of(CAL,2025,1),FiscalYearMonth.parse("2025-M01"));
        assertEquals(LocalDate.of(2025,1,1),FiscalYearMonth.parse("2025-M01").getFirstDay());
        assertEquals(FiscalYearMonth.of(US,2025,1),FiscalYearMonth.parse("2025-M01",US));
    }

    @Test
    public void
    testParseCalendarMismatch()
    {
        assertThrows(
            DateTimeParseException.class,
            () -> FiscalYearMonth.parse("2025-M01[UK_GOVERNMENT]",US));
    }

    @Test
    public void
    testParseInvalid()
    {
        assertThrows(DateTimeParseException.class,() -> FiscalYearMonth.parse("2025-M01[NOPE]"));
        assertThrows(DateTimeParseException.class,() -> FiscalYearMonth.parse("2025-M13[US_FEDERAL]"));
        assertThrows(DateTimeParseException.class,() -> FiscalYearMonth.parse("2025-1"));
    }

    @Test
    public void
    testCompareTo()
    {
        FiscalYearMonth first  = FiscalYearMonth.of(US,2025,1);
        FiscalYearMonth second = FiscalYearMonth.of(US,2025,2);
        FiscalYearMonth third  = FiscalYearMonth.of(US,2026,1);

        assertTrue(first.compareTo(second) < 0);
        assertTrue(second.compareTo(third) < 0);
        assertTrue(third.compareTo(first) > 0);
        assertEquals(0,first.compareTo(FiscalYearMonth.of(US,2025,1)));
    }

    @Test
    public void
    testCompareToDifferentCalendar()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> FiscalYearMonth.of(US,2025,1).compareTo(FiscalYearMonth.of(UK,2025,1)));

        assertEquals("cannot compare values from different calendars: US_FEDERAL and UK_GOVERNMENT",e.getMessage());
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        FiscalYearMonth value = FiscalYearMonth.of(US,2025,1);

        assertEquals(value,FiscalYearMonth.of(US,2025,1));
        assertEquals(value.hashCode(),FiscalYearMonth.of(US,2025,1).hashCode());
        assertNotEquals(value,FiscalYearMonth.of(US,2025,2));
        assertNotEquals(value,FiscalYearMonth.of(UK,2025,1));
        assertNotEquals(null,value);
    }

    @Test
    public void
    testSerialization() throws IOException, ClassNotFoundException
    {
        FiscalYearMonth expected = FiscalYearMonth.of(US,2025,1);

        assertEquals(expected,roundTrip(expected));
    }

    @Test
    public void
    testJsonWrite() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();

        assertEquals(
            "{\"calendar\":\"US_FEDERAL\",\"value\":\"2025-M01\"}",
            mapper.writeValueAsString(FiscalYearMonth.of(US,2025,1)));
    }

    @Test
    public void
    testJsonRead() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();

        assertEquals(
            FiscalYearMonth.of(US,2025,1),
            mapper.readValue(
                "{\"calendar\":\"US_FEDERAL\",\"value\":\"2025-M01\"}",
                FiscalYearMonth.class));
    }

    @Test
    public void
    testDefaultCalendarOverloads()
    {
        assertEquals(CAL,FiscalYearMonth.of(2025,1).getCalendar());

        try
        {
            FiscalCalendarRegistry.setDefault(US);

            assertEquals(US,FiscalYearMonth.of(2025,1).getCalendar());
            assertEquals(US,FiscalYearMonth.parse("2025-M01").getCalendar());
        }
        finally
        {
            FiscalCalendarRegistry.setDefault(FiscalCalendar.CALENDAR_YEAR);
        }
    }

    @Test
    public void
    testTemporalRange()
    {
        TemporalRange<FiscalYearMonth> range =
            new TemporalRange<>(FiscalYearMonth.of(US,2025,1),FiscalYearMonth.of(US,2025,3));

        assertEquals(FiscalYearMonth.of(US,2025,1),range.getBeginning());
        assertEquals(FiscalYearMonth.of(US,2025,3),range.getEnd());
    }

    @Test
    public void
    testTemporalRangeInvalid()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new TemporalRange<>(FiscalYearMonth.of(US,2025,3),FiscalYearMonth.of(US,2025,1)));

        assertEquals("beginning must be before end",e.getMessage());
    }
}

//////////////////////////////////////////////////////////////////////////////
