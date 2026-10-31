//////////////////////////////////////////////////////////////////////////////
// CalendarPeriodRangeTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("CommitStage")
public
class CalendarPeriodRangeTest
{
    private static final RetailCalendar NRF = RetailCalendar.NRF;

    private static final FiscalCalendar US = FiscalCalendar.US_FEDERAL;

    private static final RetailCalendar T445 =
        RetailCalendar.of(
            "CPR_T445",
            RetailWeekPattern.FOUR_FOUR_FIVE,
            Month.JANUARY,
            DayOfWeek.SATURDAY,
            RetailYearEndRule.NEAREST_END_OF_MONTH,
            CalendarYearLabel.START_YEAR);

    private static RetailYearMonthRange
    monthRange()
    {
        return RetailYearMonthRange.of(RetailYearMonth.of(NRF,2024,1),RetailYearMonth.of(NRF,2024,3));
    }

    public static Stream<Arguments>
    periodSources()
    {
        return
            Stream.of(
                Arguments.of(
                    RetailYearMonthRange.of(RetailYearMonth.of(NRF,2024,1),RetailYearMonth.of(NRF,2024,3)),
                    Period.of(0,2,3),
                    Duration.ofDays(63)),
                Arguments.of(
                    FiscalYearMonthRange.of(FiscalYearMonth.of(US,2025,1),FiscalYearMonth.of(US,2025,3)),
                    Period.ofMonths(2),
                    Duration.ofDays(61)),
                Arguments.of(
                    YearWeekRange.of(YearWeek.of(2024,1),YearWeek.of(2024,3)),
                    Period.ofDays(14),
                    Duration.ofDays(14)),
                Arguments.of(
                    FiscalYearRange.of(FiscalYear.of(US,2025),FiscalYear.of(US,2027)),
                    Period.ofYears(2),
                    Duration.ofDays(730)),
                Arguments.of(
                    RetailYearRange.of(RetailYear.of(NRF,2023),RetailYear.of(NRF,2024)),
                    Period.of(1,0,6),
                    Duration.ofDays(371)),
                Arguments.of(
                    RetailYearMonthRange.of(RetailYearMonth.of(NRF,2024,3),RetailYearMonth.of(NRF,2024,3)),
                    Period.ZERO,
                    Duration.ZERO));
    }

    public static Stream<Arguments>
    containsSources()
    {
        return
            Stream.of(
                Arguments.of(LocalDate.of(2024,2,4),true),
                Arguments.of(LocalDate.of(2024,5,4),true),
                Arguments.of(LocalDate.of(2024,5,5),false),
                Arguments.of(LocalDate.of(2024,2,3),false));
    }

    public static Stream<Arguments>
    lengthSources()
    {
        return
            Stream.of(
                Arguments.of(monthRange(),3L),
                Arguments.of(
                    RetailYearMonthRange.of(RetailYearMonth.of(NRF,2024,3),RetailYearMonth.of(NRF,2024,3)),
                    1L),
                Arguments.of(YearWeekRange.of(YearWeek.of(2020,52),YearWeek.of(2021,1)),3L),
                Arguments.of(
                    RetailYearWeekRange.of(RetailYearWeek.of(NRF,2023,52),RetailYearWeek.of(NRF,2024,1)),
                    3L),
                Arguments.of(
                    FiscalYearQuarterRange.of(FiscalYearQuarter.of(US,2025,1),FiscalYearQuarter.of(US,2025,4)),
                    4L));
    }

    @ParameterizedTest
    @MethodSource("periodSources")
    public void
    testGetPeriodAndDuration(TemporalRange<?> range,Period period,Duration duration)
    {
        assertEquals(period,range.getPeriod());
        assertEquals(duration,range.getDuration());
    }

    @Test
    public void
    testTemporalRangeGetPeriod()
    {
        TemporalRange<RetailYearMonth> range =
            new TemporalRange<>(RetailYearMonth.of(NRF,2024,1),RetailYearMonth.of(NRF,2024,3));

        assertEquals(Period.of(0,2,3),range.getPeriod());
    }

    @Test
    public void
    testFirstAndLastDay()
    {
        RetailYearMonthRange range = monthRange();

        assertEquals(LocalDate.of(2024,2,4),range.getFirstDay());
        assertEquals(LocalDate.of(2024,5,4),range.getLastDay());
    }

    @ParameterizedTest
    @MethodSource("containsSources")
    public void
    testContains(LocalDate date,boolean expected)
    {
        assertEquals(expected,monthRange().contains(date));
    }

    @Test
    public void
    testContainsNull()
    {
        NullPointerException e =
            assertThrows(NullPointerException.class,() -> monthRange().contains(null));

        assertEquals("date is null",e.getMessage());
    }

    @ParameterizedTest
    @MethodSource("lengthSources")
    public void
    testLengthInPeriods(CalendarPeriodRange<?> range,long expected)
    {
        assertEquals(expected,range.lengthInPeriods());
    }

    @Test
    public void
    testYearWeekLastDay()
    {
        YearWeekRange range = YearWeekRange.of(YearWeek.of(2020,52),YearWeek.of(2021,1));

        assertEquals(LocalDate.of(2021,1,10),range.getLastDay());
    }

    @Test
    public void
    testFiscalQuarterDays()
    {
        FiscalYearQuarterRange range =
            FiscalYearQuarterRange.of(FiscalYearQuarter.of(US,2025,1),FiscalYearQuarter.of(US,2025,4));

        assertEquals(LocalDate.of(2024,10,1),range.getFirstDay());
        assertEquals(LocalDate.of(2025,9,30),range.getLastDay());
    }

    @Test
    public void
    testStream()
    {
        List<RetailYearMonth> expected =
            List.of(
                RetailYearMonth.of(NRF,2024,1),
                RetailYearMonth.of(NRF,2024,2),
                RetailYearMonth.of(NRF,2024,3));

        assertEquals(expected,monthRange().stream().toList());
    }

    @Test
    public void
    testIterator()
    {
        List<RetailYearMonth> actual = new ArrayList<>();

        for (RetailYearMonth month : monthRange())
            actual.add(month);

        assertEquals(
            List.of(
                RetailYearMonth.of(NRF,2024,1),
                RetailYearMonth.of(NRF,2024,2),
                RetailYearMonth.of(NRF,2024,3)),
            actual);
    }

    @Test
    public void
    testYearWeekStream()
    {
        YearWeekRange range = YearWeekRange.of(YearWeek.of(2020,52),YearWeek.of(2021,1));

        assertEquals(
            List.of(YearWeek.of(2020,52),YearWeek.of(2020,53),YearWeek.of(2021,1)),
            range.stream().toList());
    }

    @Test
    public void
    testSinglePeriodStream()
    {
        RetailYearMonthRange range =
            RetailYearMonthRange.of(RetailYearMonth.of(NRF,2024,3),RetailYearMonth.of(NRF,2024,3));

        assertEquals(List.of(RetailYearMonth.of(NRF,2024,3)),range.stream().toList());
    }

    @Test
    public void
    testOfCalendarPeriods()
    {
        CalendarPeriodRange<RetailYearMonth> generic =
            CalendarPeriodRange.ofCalendarPeriods(RetailYearMonth.of(NRF,2024,1),RetailYearMonth.of(NRF,2024,3));

        assertEquals(3,generic.lengthInPeriods());
    }

    @Test
    public void
    testOfCalendarPeriodsNotEqualToSubclass()
    {
        CalendarPeriodRange<RetailYearMonth> generic =
            CalendarPeriodRange.ofCalendarPeriods(RetailYearMonth.of(NRF,2024,1),RetailYearMonth.of(NRF,2024,3));
        RetailYearMonthRange                 typed   = monthRange();

        assertNotEquals(generic,typed);
        assertNotEquals(typed,generic);
        assertFalse(generic.equals(typed));
        assertTrue(typed.equals(monthRange()));
    }

    @Test
    public void
    testMixedCalendars()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> RetailYearMonthRange.of(RetailYearMonth.of(NRF,2024,1),RetailYearMonth.of(T445,2024,3)));
    }

    @Test
    public void
    testReversed()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> RetailYearMonthRange.of(RetailYearMonth.of(NRF,2024,3),RetailYearMonth.of(NRF,2024,1)));

        assertEquals("beginning must be before end",e.getMessage());
    }
}

//////////////////////////////////////////////////////////////////////////////
