//////////////////////////////////////////////////////////////////////////////
// RetailCalendarFieldTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
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
class RetailCalendarFieldTest
{
    private static final RetailCalendar NRF = RetailCalendar.NRF;

    private static final RetailCalendar T445 =
        RetailCalendar.of(
            "FIELD_T445",
            RetailWeekPattern.FOUR_FOUR_FIVE,
            Month.JANUARY,
            DayOfWeek.SATURDAY,
            RetailYearEndRule.NEAREST_END_OF_MONTH,
            CalendarYearLabel.START_YEAR);

    public static Stream<Arguments>
    fieldValueSources()
    {
        return
            Stream.of(
                Arguments.of(LocalDate.of(2024,2,4),2024,1,1,1,1,1,1,1),
                Arguments.of(LocalDate.of(2025,2,1),2024,4,3,12,52,13,4,364),
                Arguments.of(LocalDate.of(2024,2,3),2023,4,3,12,53,14,5,371),
                Arguments.of(LocalDate.of(2024,4,7),2024,1,3,3,10,10,1,64),
                Arguments.of(LocalDate.of(2024,11,30),2024,4,1,10,43,4,4,301));
    }

    @ParameterizedTest
    @MethodSource("fieldValueSources")
    public void
    testFieldValues(
        LocalDate date,
        int       year,
        int       quarterOfYear,
        int       monthOfQuarter,
        int       monthOfYear,
        int       weekOfYear,
        int       weekOfQuarter,
        int       weekOfMonth,
        int       dayOfYear)
    {
        assertEquals(year,date.get(NRF.year()));
        assertEquals(quarterOfYear,date.get(NRF.quarterOfYear()));
        assertEquals(monthOfQuarter,date.get(NRF.monthOfQuarter()));
        assertEquals(monthOfYear,date.get(NRF.monthOfYear()));
        assertEquals(weekOfYear,date.get(NRF.weekOfYear()));
        assertEquals(weekOfQuarter,date.get(NRF.weekOfQuarter()));
        assertEquals(weekOfMonth,date.get(NRF.weekOfMonth()));
        assertEquals(dayOfYear,date.get(NRF.dayOfYear()));
        assertEquals(year,date.getLong(NRF.year()));
    }

    @Test
    public void
    testRange()
    {
        assertEquals(ValueRange.of(1,52,53),NRF.weekOfYear().range());
        assertEquals(ValueRange.of(1,13,14),NRF.weekOfQuarter().range());
        assertEquals(ValueRange.of(1,4,5),NRF.weekOfMonth().range());
        assertEquals(ValueRange.of(1,4,6),T445.weekOfMonth().range());
        assertEquals(ValueRange.of(1,364,371),NRF.dayOfYear().range());
        assertEquals(ValueRange.of(1,12),NRF.monthOfYear().range());
        assertEquals(ValueRange.of(1,4),NRF.quarterOfYear().range());
        assertEquals(ValueRange.of(1,3),NRF.monthOfQuarter().range());
    }

    @Test
    public void
    testRangeRefinedBy()
    {
        assertEquals(
            ValueRange.of(1,53),LocalDate.of(2024,2,3).range(NRF.weekOfYear()));
        assertEquals(
            ValueRange.of(1,52),LocalDate.of(2024,6,1).range(NRF.weekOfYear()));
        assertEquals(
            ValueRange.of(1,5),LocalDate.of(2024,2,3).range(NRF.weekOfMonth()));
        assertEquals(
            ValueRange.of(1,5),LocalDate.of(2024,3,10).range(NRF.weekOfMonth()));
        assertEquals(
            ValueRange.of(1,4),LocalDate.of(2024,2,10).range(NRF.weekOfMonth()));
        assertEquals(
            ValueRange.of(1,14),LocalDate.of(2024,2,3).range(NRF.weekOfQuarter()));
        assertEquals(
            ValueRange.of(1,371),LocalDate.of(2024,2,3).range(NRF.dayOfYear()));
    }

    @Test
    public void
    testAdjustInto()
    {
        assertEquals(
            LocalDate.of(2024,3,31),LocalDate.of(2024,3,10).with(NRF.weekOfMonth(),5));
        assertEquals(
            LocalDate.of(2024,4,28),LocalDate.of(2024,3,31).with(NRF.monthOfYear(),3));
        assertEquals(
            LocalDate.of(2023,1,29),LocalDate.of(2024,2,4).with(NRF.year(),2023));
        assertEquals(
            LocalDate.of(2025,2,1),LocalDate.of(2024,2,3).with(NRF.year(),2024));
        assertEquals(
            LocalDate.of(2024,4,9),LocalDate.of(2024,2,6).with(NRF.weekOfYear(),10));
        assertEquals(
            LocalDate.of(2024,2,4),LocalDate.of(2024,2,10).with(NRF.dayOfYear(),1));
        assertEquals(
            LocalDate.of(2024,11,3),LocalDate.of(2024,2,4).with(NRF.quarterOfYear(),4));
        assertEquals(
            LocalDate.of(2024,3,3),LocalDate.of(2024,2,4).with(NRF.monthOfQuarter(),2));
    }

    @Test
    public void
    testAdjustIntoSameValue()
    {
        assertEquals(
            LocalDate.of(2024,2,4),LocalDate.of(2024,2,4).with(NRF.weekOfYear(),1));
    }

    @Test
    public void
    testAdjustIntoLocalDateTime()
    {
        assertEquals(
            LocalDateTime.of(2024,2,11,10,15),
            LocalDateTime.of(2024,2,4,10,15).with(NRF.weekOfYear(),2));
    }

    @Test
    public void
    testAdjustIntoInvalidValue()
    {
        assertThrows(
            DateTimeException.class,
            () -> LocalDate.of(2024,3,10).with(NRF.weekOfMonth(),6));
        assertThrows(
            DateTimeException.class,
            () -> LocalDate.of(2024,3,10).with(NRF.monthOfYear(),13));
        assertThrows(
            DateTimeException.class,
            () -> LocalDate.of(2024,6,1).with(NRF.weekOfYear(),53));
    }

    @Test
    public void
    testUnsupportedTemporal()
    {
        assertFalse(LocalTime.of(10,0).isSupported(NRF.weekOfYear()));
        assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> NRF.weekOfYear().getFrom(LocalTime.of(10,0)));
        assertTrue(LocalDate.of(2024,2,4).isSupported(NRF.weekOfYear()));
    }

    @Test
    public void
    testMetadata()
    {
        assertEquals(ChronoUnit.WEEKS,NRF.weekOfMonth().getBaseUnit());
        assertEquals(NRF.months(),NRF.weekOfMonth().getRangeUnit());
        assertEquals(NRF.months(),NRF.monthOfYear().getBaseUnit());
        assertEquals(ChronoUnit.FOREVER,NRF.year().getRangeUnit());
        assertEquals(ChronoUnit.DAYS,NRF.dayOfYear().getBaseUnit());
        assertTrue(NRF.weekOfYear().isDateBased());
        assertFalse(NRF.weekOfYear().isTimeBased());
        assertEquals("WeekOfMonth[NRF]",NRF.weekOfMonth().toString());
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        assertEquals(NRF.weekOfYear(),NRF.weekOfYear());
        assertEquals(NRF.weekOfYear().hashCode(),NRF.weekOfYear().hashCode());
        assertNotEquals(NRF.weekOfYear(),NRF.weekOfMonth());
        assertNotEquals(NRF.weekOfYear(),T445.weekOfYear());
    }
}

//////////////////////////////////////////////////////////////////////////////
