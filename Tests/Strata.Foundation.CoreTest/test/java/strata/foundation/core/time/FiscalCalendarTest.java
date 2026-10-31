//////////////////////////////////////////////////////////////////////////////
// FiscalCalendarTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.IsoFields;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("CommitStage")
public
class FiscalCalendarTest
{
    private static final FiscalCalendar US    = FiscalCalendar.US_FEDERAL;
    private static final FiscalCalendar CAL   = FiscalCalendar.CALENDAR_YEAR;
    private static final FiscalCalendar UK    = FiscalCalendar.UK_GOVERNMENT;
    private static final FiscalCalendar STATE = FiscalCalendar.US_STATE;
    private static final FiscalCalendar EU    = FiscalCalendar.EU_GOVERNMENT;

    private static final FiscalCalendar APR_START =
        FiscalCalendar.of("T_APR_START",Month.APRIL,CalendarYearLabel.START_YEAR);

    private static final FiscalCalendar SUN1 =
        FiscalCalendar.of("T_SUN1",Month.JANUARY,CalendarYearLabel.END_YEAR,DayOfWeek.SUNDAY,1);

    public static Stream<Arguments>
    yearSources()
    {
        return
            Stream.of(
                Arguments.of(US,2025,LocalDate.of(2024,10,1),LocalDate.of(2025,9,30),365),
                Arguments.of(US,2024,LocalDate.of(2023,10,1),LocalDate.of(2024,9,30),366),
                Arguments.of(CAL,2025,LocalDate.of(2025,1,1),LocalDate.of(2025,12,31),365),
                Arguments.of(UK,2025,LocalDate.of(2024,4,1),LocalDate.of(2025,3,31),365),
                Arguments.of(STATE,2025,LocalDate.of(2024,7,1),LocalDate.of(2025,6,30),365),
                Arguments.of(APR_START,2024,LocalDate.of(2024,4,1),LocalDate.of(2025,3,31),365));
    }

    @ParameterizedTest
    @MethodSource("yearSources")
    public void
    testYears(
        FiscalCalendar calendar,
        int            year,
        LocalDate      start,
        LocalDate      end,
        int            days)
    {
        assertEquals(start,calendar.getYearStart(year));
        assertEquals(end,calendar.getYearEnd(year));
        assertEquals(days,calendar.lengthOfYearInDays(year));
        assertEquals(year,calendar.getYear(start));
        assertEquals(year,calendar.getYear(end));
    }

    @Test
    public void
    testGetYear()
    {
        assertEquals(2024,US.getYear(LocalDate.of(2024,9,30)));
        assertEquals(2025,US.getYear(LocalDate.of(2024,10,1)));
        assertEquals(2025,US.getYear(LocalDate.of(2025,1,15)));
        assertEquals(2024,APR_START.getYear(LocalDate.of(2025,3,31)));
    }

    @Test
    public void
    testGetYearNull()
    {
        NullPointerException e =
            assertThrows(NullPointerException.class,() -> US.getYear(null));

        assertEquals("date is null",e.getMessage());
    }

    @Test
    public void
    testQuarters()
    {
        assertEquals(LocalDate.of(2024,10,1),US.getQuarterStart(2025,1));
        assertEquals(LocalDate.of(2025,1,1),US.getQuarterStart(2025,2));
        assertEquals(LocalDate.of(2025,4,1),US.getQuarterStart(2025,3));
        assertEquals(LocalDate.of(2025,7,1),US.getQuarterStart(2025,4));
        assertThrows(DateTimeException.class,() -> US.getQuarterStart(2025,5));
    }

    @Test
    public void
    testMonths()
    {
        assertEquals(LocalDate.of(2024,10,1),US.getMonthStart(2025,1));
        assertEquals(LocalDate.of(2025,1,1),US.getMonthStart(2025,4));
        assertEquals(LocalDate.of(2025,9,1),US.getMonthStart(2025,12));
        assertEquals(28,US.lengthOfMonthInDays(2025,5));
        assertEquals(29,US.lengthOfMonthInDays(2024,5));
        assertEquals(31,US.lengthOfMonthInDays(2025,1));
        assertThrows(DateTimeException.class,() -> US.getMonthStart(2025,13));
    }

    @Test
    public void
    testWeeks()
    {
        assertEquals(LocalDate.of(2024,9,30),US.getWeekStart(2025,1));
        assertEquals(52,US.lengthOfWeekBasedYearInWeeks(2025));
        assertEquals(LocalDate.of(2025,9,29),US.getWeekStart(2026,1));
        assertEquals(53,US.lengthOfWeekBasedYearInWeeks(2027));
        assertEquals(LocalDate.of(2027,9,27),US.getWeekStart(2027,53));
        assertThrows(DateTimeException.class,() -> US.getWeekStart(2025,53));
        assertEquals(2025,US.getWeekBasedYear(LocalDate.of(2024,9,30)));
        assertEquals(2024,US.getWeekBasedYear(LocalDate.of(2024,9,29)));
    }

    public static Stream<Arguments>
    isoWeekYearSources()
    {
        return
            Stream.of(
                Arguments.of(2020,53),
                Arguments.of(2023,52),
                Arguments.of(2024,52),
                Arguments.of(2026,53));
    }

    @ParameterizedTest
    @MethodSource("isoWeekYearSources")
    public void
    testIsoWeeksInYear(int year,int weeks)
    {
        assertEquals(weeks,CAL.lengthOfWeekBasedYearInWeeks(year));
    }

    public static Stream<Arguments>
    isoWeekBasedYearSources()
    {
        return
            Stream.of(
                Arguments.of(LocalDate.of(2021,1,1),2020),
                Arguments.of(LocalDate.of(2024,12,30),2025),
                Arguments.of(LocalDate.of(2023,1,1),2022));
    }

    @ParameterizedTest
    @MethodSource("isoWeekBasedYearSources")
    public void
    testIsoWeekBasedYear(LocalDate date,int weekBasedYear)
    {
        assertEquals(weekBasedYear,CAL.getWeekBasedYear(date));
        assertEquals(date.get(IsoFields.WEEK_BASED_YEAR),CAL.getWeekBasedYear(date));
    }

    @Test
    public void
    testIsoWeekStart()
    {
        assertEquals(LocalDate.of(2020,12,28),CAL.getWeekStart(2020,53));
    }

    @Test
    public void
    testSundayFirstWeek()
    {
        assertEquals(LocalDate.of(2023,12,31),SUN1.getWeekStart(2024,1));
        assertEquals(LocalDate.of(2024,12,29),SUN1.getWeekStart(2025,1));
        assertEquals(52,SUN1.lengthOfWeekBasedYearInWeeks(2024));
    }

    @Test
    public void
    testGetters()
    {
        assertEquals("US_FEDERAL",US.getId());
        assertEquals(Month.OCTOBER,US.getStartMonth());
        assertEquals(CalendarYearLabel.END_YEAR,US.getYearLabel());
        assertEquals(DayOfWeek.MONDAY,US.getFirstDayOfWeek());
        assertEquals(4,US.getMinimalDaysInFirstWeek());
        assertEquals("US_FEDERAL",US.toString());
    }

    @Test
    public void
    testConstants()
    {
        assertEquals("CALENDAR_YEAR",FiscalCalendar.CALENDAR_YEAR.getId());
        assertEquals("US_FEDERAL",FiscalCalendar.US_FEDERAL.getId());
        assertEquals("US_STATE",FiscalCalendar.US_STATE.getId());
        assertEquals("UK_GOVERNMENT",FiscalCalendar.UK_GOVERNMENT.getId());
        assertEquals("EU_GOVERNMENT",FiscalCalendar.EU_GOVERNMENT.getId());
        assertEquals(Month.JANUARY,EU.getStartMonth());
    }

    @Test
    public void
    testOfNullArguments()
    {
        NullPointerException id =
            assertThrows(
                NullPointerException.class,
                () -> FiscalCalendar.of(null,Month.OCTOBER,CalendarYearLabel.END_YEAR));
        NullPointerException startMonth =
            assertThrows(
                NullPointerException.class,
                () -> FiscalCalendar.of("X",null,CalendarYearLabel.END_YEAR));
        NullPointerException yearLabel =
            assertThrows(
                NullPointerException.class,
                () -> FiscalCalendar.of("X",Month.OCTOBER,null));
        NullPointerException firstDayOfWeek =
            assertThrows(
                NullPointerException.class,
                () -> FiscalCalendar.of("X",Month.OCTOBER,CalendarYearLabel.END_YEAR,null,4));

        assertEquals("id is null",id.getMessage());
        assertEquals("startMonth is null",startMonth.getMessage());
        assertEquals("yearLabel is null",yearLabel.getMessage());
        assertEquals("firstDayOfWeek is null",firstDayOfWeek.getMessage());
    }

    @Test
    public void
    testOfInvalidArguments()
    {
        IllegalArgumentException blank =
            assertThrows(
                IllegalArgumentException.class,
                () -> FiscalCalendar.of(" ",Month.OCTOBER,CalendarYearLabel.END_YEAR));
        IllegalArgumentException bracket =
            assertThrows(
                IllegalArgumentException.class,
                () -> FiscalCalendar.of("A]B",Month.OCTOBER,CalendarYearLabel.END_YEAR));
        IllegalArgumentException low =
            assertThrows(
                IllegalArgumentException.class,
                () -> FiscalCalendar.of("X",Month.OCTOBER,CalendarYearLabel.END_YEAR,DayOfWeek.MONDAY,0));
        IllegalArgumentException high =
            assertThrows(
                IllegalArgumentException.class,
                () -> FiscalCalendar.of("X",Month.OCTOBER,CalendarYearLabel.END_YEAR,DayOfWeek.MONDAY,8));

        assertEquals("id is blank",blank.getMessage());
        assertEquals("id must not contain '[' or ']'",bracket.getMessage());
        assertEquals("minimalDaysInFirstWeek must be between 1 and 7",low.getMessage());
        assertEquals("minimalDaysInFirstWeek must be between 1 and 7",high.getMessage());
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        FiscalCalendar same = FiscalCalendar.of("US_FEDERAL",Month.OCTOBER,CalendarYearLabel.END_YEAR);

        assertEquals(US,same);
        assertEquals(US.hashCode(),same.hashCode());
        assertNotEquals(
            US,FiscalCalendar.of("OTHER",Month.OCTOBER,CalendarYearLabel.END_YEAR));
        assertNotEquals(
            US,FiscalCalendar.of("US_FEDERAL",Month.JULY,CalendarYearLabel.END_YEAR));
        assertNotEquals(
            US,FiscalCalendar.of("US_FEDERAL",Month.OCTOBER,CalendarYearLabel.START_YEAR));
        assertNotEquals(
            US,
            FiscalCalendar.of(
                "US_FEDERAL",Month.OCTOBER,CalendarYearLabel.END_YEAR,DayOfWeek.SUNDAY,4));
        assertNotEquals(
            US,
            FiscalCalendar.of(
                "US_FEDERAL",Month.OCTOBER,CalendarYearLabel.END_YEAR,DayOfWeek.MONDAY,1));
        assertNotEquals(CAL,EU);
    }

    private FiscalCalendar
    roundTrip(FiscalCalendar calendar) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(calendar);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (FiscalCalendar)in.readObject();
        }
    }

    @Test
    public void
    testSerialization() throws IOException, ClassNotFoundException
    {
        assertEquals(US,roundTrip(US));
    }
}

//////////////////////////////////////////////////////////////////////////////
