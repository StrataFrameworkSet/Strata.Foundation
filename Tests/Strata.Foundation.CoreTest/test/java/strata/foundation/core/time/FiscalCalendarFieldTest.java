//////////////////////////////////////////////////////////////////////////////
// FiscalCalendarFieldTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("CommitStage")
public
class FiscalCalendarFieldTest
{
    private static final FiscalCalendar US  = FiscalCalendar.US_FEDERAL;
    private static final FiscalCalendar CAL = FiscalCalendar.CALENDAR_YEAR;

    public static Stream<Arguments>
    fieldValueSources()
    {
        return
            Stream.of(
                Arguments.of(LocalDate.of(2025,1,15),2025,2,4,1,107,2025,16),
                Arguments.of(LocalDate.of(2024,10,1),2025,1,1,1,1,2025,1));
    }

    @ParameterizedTest
    @MethodSource("fieldValueSources")
    public void
    testFieldValues(
        LocalDate date,
        int       year,
        int       quarterOfYear,
        int       monthOfYear,
        int       monthOfQuarter,
        int       dayOfYear,
        int       weekBasedYear,
        int       weekOfWeekBasedYear)
    {
        assertEquals(year,date.get(US.year()));
        assertEquals(quarterOfYear,date.get(US.quarterOfYear()));
        assertEquals(monthOfYear,date.get(US.monthOfYear()));
        assertEquals(monthOfQuarter,date.get(US.monthOfQuarter()));
        assertEquals(dayOfYear,date.get(US.dayOfYear()));
        assertEquals(weekBasedYear,date.get(US.weekBasedYear()));
        assertEquals(weekOfWeekBasedYear,date.get(US.weekOfWeekBasedYear()));
    }

    public static Stream<Arguments>
    isoDateSources()
    {
        return
            Stream.of(
                Arguments.of(LocalDate.of(2020,12,28)),
                Arguments.of(LocalDate.of(2021,1,3)),
                Arguments.of(LocalDate.of(2024,12,30)),
                Arguments.of(LocalDate.of(2023,1,1)));
    }

    @ParameterizedTest
    @MethodSource("isoDateSources")
    public void
    testIsoEquivalence(LocalDate date)
    {
        assertEquals(
            date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR),date.get(CAL.weekOfWeekBasedYear()));
        assertEquals(date.get(IsoFields.WEEK_BASED_YEAR),date.get(CAL.weekBasedYear()));
    }

    @Test
    public void
    testRange()
    {
        assertEquals(ValueRange.of(1,365,366),US.dayOfYear().range());
        assertEquals(ValueRange.of(1,52,53),US.weekOfWeekBasedYear().range());
        assertEquals(ValueRange.of(1,12),US.monthOfYear().range());
    }

    @Test
    public void
    testRangeRefinedBy()
    {
        assertEquals(
            ValueRange.of(1,366),LocalDate.of(2024,3,1).range(US.dayOfYear()));
        assertEquals(
            ValueRange.of(1,53),LocalDate.of(2027,1,1).range(US.weekOfWeekBasedYear()));
    }

    @Test
    public void
    testAdjustInto()
    {
        assertEquals(
            LocalDate.of(2025,2,28),LocalDate.of(2025,1,31).with(US.monthOfYear(),5));
        assertEquals(
            LocalDate.of(2026,2,28),LocalDate.of(2024,2,29).with(US.year(),2026));
        assertEquals(
            LocalDate.of(2025,7,15),LocalDate.of(2024,10,15).with(US.quarterOfYear(),4));
        assertEquals(
            LocalDate.of(2024,10,1),LocalDate.of(2024,10,15).with(US.dayOfYear(),1));
        assertEquals(
            LocalDate.of(2024,10,2),LocalDate.of(2025,1,15).with(US.weekOfWeekBasedYear(),1));
        assertEquals(
            LocalDate.of(2021,12,27),LocalDate.of(2020,12,28).with(CAL.weekBasedYear(),2021));
    }

    @Test
    public void
    testAdjustIntoLocalDateTime()
    {
        assertEquals(
            LocalDateTime.of(2025,2,28,8,0),
            LocalDateTime.of(2025,1,31,8,0).with(US.monthOfYear(),5));
    }

    @Test
    public void
    testAdjustIntoSameValue()
    {
        assertEquals(
            LocalDate.of(2025,1,15),LocalDate.of(2025,1,15).with(US.monthOfYear(),4));
    }

    @Test
    public void
    testInvalidValues()
    {
        assertThrows(
            DateTimeException.class,
            () -> LocalDate.of(2025,1,15).with(US.monthOfYear(),13));
        assertThrows(
            DateTimeException.class,
            () -> LocalDate.of(2025,1,15).with(US.dayOfYear(),367));
    }

    @Test
    public void
    testUnsupportedTemporal()
    {
        assertFalse(LocalTime.of(10,0).isSupported(US.year()));
        assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> US.year().getFrom(LocalTime.of(10,0)));
    }

    @Test
    public void
    testMetadata()
    {
        assertEquals(ChronoUnit.MONTHS,US.monthOfYear().getBaseUnit());
        assertEquals(IsoFields.QUARTER_YEARS,US.quarterOfYear().getBaseUnit());
        assertEquals(US.weekBasedYears(),US.weekBasedYear().getBaseUnit());
        assertEquals(ChronoUnit.FOREVER,US.year().getRangeUnit());
        assertEquals(IsoFields.QUARTER_YEARS,US.monthOfQuarter().getRangeUnit());
        assertEquals("WeekOfWeekBasedYear[US_FEDERAL]",US.weekOfWeekBasedYear().toString());
        assertTrue(US.year().isDateBased());
        assertFalse(US.year().isTimeBased());
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        assertEquals(US.year(),US.year());
        assertEquals(US.year().hashCode(),US.year().hashCode());
        assertNotEquals(US.year(),US.monthOfYear());
        assertNotEquals(US.year(),CAL.year());
    }

    @Test
    public void
    testWeekBasedYearsUnit()
    {
        assertEquals(
            LocalDate.of(2024,12,30),LocalDate.of(2024,1,1).plus(1,CAL.weekBasedYears()));
        assertEquals(
            LocalDate.of(2024,1,1),LocalDate.of(2024,12,30).minus(1,CAL.weekBasedYears()));
        assertEquals(
            LocalDate.of(2021,12,27),LocalDate.of(2020,12,28).plus(1,CAL.weekBasedYears()));
        assertEquals(
            1,
            CAL.weekBasedYears().between(LocalDate.of(2024,1,1),LocalDate.of(2024,12,30)));
        assertEquals(
            0,
            CAL.weekBasedYears().between(LocalDate.of(2024,1,1),LocalDate.of(2024,12,29)));
        assertEquals(
            2,
            LocalDate.of(2024,1,1).until(LocalDate.of(2026,1,5),CAL.weekBasedYears()));
        assertTrue(CAL.weekBasedYears().isDurationEstimated());
        assertEquals("WeekBasedYears[CALENDAR_YEAR]",CAL.weekBasedYears().toString());
    }

    @Test
    public void
    testWeekBasedYearsUnsupported()
    {
        assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> LocalTime.of(10,0).plus(1,CAL.weekBasedYears()));
    }

    @Test
    public void
    testUnitEqualsAndHashCode()
    {
        assertEquals(CAL.weekBasedYears(),CAL.weekBasedYears());
        assertEquals(CAL.weekBasedYears().hashCode(),CAL.weekBasedYears().hashCode());
        assertNotEquals(CAL.weekBasedYears(),US.weekBasedYears());
    }
}

//////////////////////////////////////////////////////////////////////////////
