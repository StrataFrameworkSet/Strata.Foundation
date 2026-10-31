//////////////////////////////////////////////////////////////////////////////
// RetailCalendarTest.java
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("CommitStage")
public
class RetailCalendarTest
{
    private static final RetailCalendar T445 =
        RetailCalendar.of(
            "T445",
            RetailWeekPattern.FOUR_FOUR_FIVE,
            Month.JANUARY,
            DayOfWeek.SATURDAY,
            RetailYearEndRule.NEAREST_END_OF_MONTH,
            CalendarYearLabel.START_YEAR);

    private static final RetailCalendar T544 =
        RetailCalendar.of(
            "T544",
            RetailWeekPattern.FIVE_FOUR_FOUR,
            Month.JANUARY,
            DayOfWeek.SATURDAY,
            RetailYearEndRule.NEAREST_END_OF_MONTH,
            CalendarYearLabel.START_YEAR);

    private static final RetailCalendar LASTDEC_END =
        RetailCalendar.of(
            "LASTDEC_END",
            RetailWeekPattern.FOUR_FIVE_FOUR,
            Month.DECEMBER,
            DayOfWeek.SATURDAY,
            RetailYearEndRule.LAST_OF_MONTH,
            CalendarYearLabel.END_YEAR);

    private static final RetailCalendar LASTDEC_START =
        RetailCalendar.of(
            "LASTDEC_START",
            RetailWeekPattern.FOUR_FIVE_FOUR,
            Month.DECEMBER,
            DayOfWeek.SATURDAY,
            RetailYearEndRule.LAST_OF_MONTH,
            CalendarYearLabel.START_YEAR);

    private static final RetailCalendar LASTJAN =
        RetailCalendar.of(
            "LASTJAN",
            RetailWeekPattern.FOUR_FIVE_FOUR,
            Month.JANUARY,
            DayOfWeek.SATURDAY,
            RetailYearEndRule.LAST_OF_MONTH,
            CalendarYearLabel.START_YEAR);

    private RetailCalendar
    roundTrip(RetailCalendar calendar) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(calendar);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (RetailCalendar)in.readObject();
        }
    }

    public static Stream<Arguments>
    nrfYearSources()
    {
        return
            Stream.of(
                Arguments.of(2022,LocalDate.of(2022,1,30),LocalDate.of(2023,1,28),52),
                Arguments.of(2023,LocalDate.of(2023,1,29),LocalDate.of(2024,2,3),53),
                Arguments.of(2024,LocalDate.of(2024,2,4),LocalDate.of(2025,2,1),52),
                Arguments.of(2025,LocalDate.of(2025,2,2),LocalDate.of(2026,1,31),52),
                Arguments.of(2026,LocalDate.of(2026,2,1),LocalDate.of(2027,1,30),52));
    }

    public static Stream<Arguments>
    nrfMonthSources()
    {
        return
            Stream.of(
                Arguments.of(1,LocalDate.of(2024,2,4),4),
                Arguments.of(2,LocalDate.of(2024,3,3),5),
                Arguments.of(3,LocalDate.of(2024,4,7),4),
                Arguments.of(4,LocalDate.of(2024,5,5),4),
                Arguments.of(5,LocalDate.of(2024,6,2),5),
                Arguments.of(6,LocalDate.of(2024,7,7),4),
                Arguments.of(7,LocalDate.of(2024,8,4),4),
                Arguments.of(8,LocalDate.of(2024,9,1),5),
                Arguments.of(9,LocalDate.of(2024,10,6),4),
                Arguments.of(10,LocalDate.of(2024,11,3),4),
                Arguments.of(11,LocalDate.of(2024,12,1),5),
                Arguments.of(12,LocalDate.of(2025,1,5),4));
    }

    public static Stream<Arguments>
    quarterStartSources()
    {
        return
            Stream.of(
                Arguments.of(1,LocalDate.of(2024,2,4)),
                Arguments.of(2,LocalDate.of(2024,5,5)),
                Arguments.of(3,LocalDate.of(2024,8,4)),
                Arguments.of(4,LocalDate.of(2024,11,3)));
    }

    public static Stream<Arguments>
    differingCalendarSources()
    {
        return
            Stream.of(
                Arguments.of(
                    "NRF2",
                    RetailWeekPattern.FOUR_FIVE_FOUR,
                    Month.JANUARY,
                    DayOfWeek.SATURDAY,
                    RetailYearEndRule.NEAREST_END_OF_MONTH,
                    CalendarYearLabel.START_YEAR),
                Arguments.of(
                    "NRF",
                    RetailWeekPattern.FOUR_FOUR_FIVE,
                    Month.JANUARY,
                    DayOfWeek.SATURDAY,
                    RetailYearEndRule.NEAREST_END_OF_MONTH,
                    CalendarYearLabel.START_YEAR),
                Arguments.of(
                    "NRF",
                    RetailWeekPattern.FOUR_FIVE_FOUR,
                    Month.FEBRUARY,
                    DayOfWeek.SATURDAY,
                    RetailYearEndRule.NEAREST_END_OF_MONTH,
                    CalendarYearLabel.START_YEAR),
                Arguments.of(
                    "NRF",
                    RetailWeekPattern.FOUR_FIVE_FOUR,
                    Month.JANUARY,
                    DayOfWeek.SUNDAY,
                    RetailYearEndRule.NEAREST_END_OF_MONTH,
                    CalendarYearLabel.START_YEAR),
                Arguments.of(
                    "NRF",
                    RetailWeekPattern.FOUR_FIVE_FOUR,
                    Month.JANUARY,
                    DayOfWeek.SATURDAY,
                    RetailYearEndRule.LAST_OF_MONTH,
                    CalendarYearLabel.START_YEAR),
                Arguments.of(
                    "NRF",
                    RetailWeekPattern.FOUR_FIVE_FOUR,
                    Month.JANUARY,
                    DayOfWeek.SATURDAY,
                    RetailYearEndRule.NEAREST_END_OF_MONTH,
                    CalendarYearLabel.END_YEAR));
    }

    @ParameterizedTest
    @MethodSource("nrfYearSources")
    public void
    testNrfYears(int year,LocalDate start,LocalDate end,int weeks)
    {
        assertEquals(start,RetailCalendar.NRF.getYearStart(year));
        assertEquals(end,RetailCalendar.NRF.getYearEnd(year));
        assertEquals(weeks,RetailCalendar.NRF.lengthOfYearInWeeks(year));
        assertEquals(year,RetailCalendar.NRF.getYear(start));
        assertEquals(year,RetailCalendar.NRF.getYear(end));
    }

    @Test
    public void
    testGetYearEdges()
    {
        assertEquals(2023,RetailCalendar.NRF.getYear(LocalDate.of(2024,2,3)));
        assertEquals(2024,RetailCalendar.NRF.getYear(LocalDate.of(2024,2,4)));
    }

    @Test
    public void
    testGetYearNull()
    {
        NullPointerException e =
            assertThrows(NullPointerException.class,() -> RetailCalendar.NRF.getYear(null));

        assertEquals("date is null",e.getMessage());
    }

    @ParameterizedTest
    @MethodSource("nrfMonthSources")
    public void
    testNrfMonths(int month,LocalDate start,int weeks)
    {
        assertEquals(start,RetailCalendar.NRF.getMonthStart(2024,month));
        assertEquals(weeks,RetailCalendar.NRF.lengthOfMonthInWeeks(2024,month));
    }

    @Test
    public void
    testNrf53WeekYear()
    {
        assertEquals(LocalDate.of(2023,12,31),RetailCalendar.NRF.getMonthStart(2023,12));
        assertEquals(5,RetailCalendar.NRF.lengthOfMonthInWeeks(2023,12));
        assertEquals(14,RetailCalendar.NRF.lengthOfQuarterInWeeks(2023,4));
        assertEquals(13,RetailCalendar.NRF.lengthOfQuarterInWeeks(2023,1));
        assertEquals(13,RetailCalendar.NRF.lengthOfQuarterInWeeks(2024,4));
    }

    @ParameterizedTest
    @MethodSource("quarterStartSources")
    public void
    testQuarterStart(int quarter,LocalDate start)
    {
        assertEquals(start,RetailCalendar.NRF.getQuarterStart(2024,quarter));
    }

    @Test
    public void
    testWeekStart()
    {
        assertEquals(LocalDate.of(2024,1,28),RetailCalendar.NRF.getWeekStart(2023,53));
        assertEquals(LocalDate.of(2024,2,4),RetailCalendar.NRF.getWeekStart(2024,1));
    }

    @Test
    public void
    testWeekStartInvalid()
    {
        assertThrows(DateTimeException.class,() -> RetailCalendar.NRF.getWeekStart(2024,53));
        assertThrows(DateTimeException.class,() -> RetailCalendar.NRF.getWeekStart(2024,0));
    }

    @Test
    public void
    testInvalidQuarterAndMonth()
    {
        assertThrows(DateTimeException.class,() -> RetailCalendar.NRF.getQuarterStart(2024,5));
        assertThrows(DateTimeException.class,() -> RetailCalendar.NRF.getQuarterStart(2024,0));
        assertThrows(DateTimeException.class,() -> RetailCalendar.NRF.getMonthStart(2024,13));
        assertThrows(
            DateTimeException.class,
            () -> RetailCalendar.NRF.lengthOfMonthInWeeks(2024,0));
    }

    @Test
    public void
    testPatterns()
    {
        assertEquals(6,T445.lengthOfMonthInWeeks(2023,12));
        assertEquals(5,T445.lengthOfMonthInWeeks(2024,12));
        assertEquals(5,T445.lengthOfMonthInWeeks(2024,3));
        assertEquals(5,T544.lengthOfMonthInWeeks(2024,1));
        assertEquals(LocalDate.of(2024,3,10),T544.getMonthStart(2024,2));
    }

    @Test
    public void
    testRulesAndLabels()
    {
        assertEquals(LocalDate.of(2023,12,31),LASTDEC_END.getYearStart(2024));
        assertEquals(LocalDate.of(2024,12,28),LASTDEC_END.getYearEnd(2024));
        assertEquals(52,LASTDEC_END.lengthOfYearInWeeks(2024));
        assertEquals(2024,LASTDEC_END.getYear(LocalDate.of(2024,6,1)));
        assertEquals(LocalDate.of(2023,12,31),LASTDEC_START.getYearStart(2023));
        assertEquals(LocalDate.of(2024,12,28),LASTDEC_START.getYearEnd(2023));
        assertEquals(2023,LASTDEC_START.getYear(LocalDate.of(2024,6,1)));
        assertEquals(LocalDate.of(2024,1,28),LASTJAN.getYearStart(2024));
        assertEquals(LocalDate.of(2025,1,25),LASTJAN.getYearEnd(2024));
    }

    @Test
    public void
    testGetFirstDayOfWeek()
    {
        assertEquals(DayOfWeek.SUNDAY,RetailCalendar.NRF.getFirstDayOfWeek());
    }

    @Test
    public void
    testNrfGetters()
    {
        assertEquals("NRF",RetailCalendar.NRF.getId());
        assertEquals(RetailWeekPattern.FOUR_FIVE_FOUR,RetailCalendar.NRF.getPattern());
        assertEquals(Month.JANUARY,RetailCalendar.NRF.getYearEndMonth());
        assertEquals(DayOfWeek.SATURDAY,RetailCalendar.NRF.getYearEndDay());
        assertEquals(RetailYearEndRule.NEAREST_END_OF_MONTH,RetailCalendar.NRF.getYearEndRule());
        assertEquals(CalendarYearLabel.START_YEAR,RetailCalendar.NRF.getYearLabel());
        assertEquals("NRF",RetailCalendar.NRF.toString());
    }

    @Test
    public void
    testOfNullId()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> RetailCalendar.of(
                    null,
                    RetailWeekPattern.FOUR_FIVE_FOUR,
                    Month.JANUARY,
                    DayOfWeek.SATURDAY,
                    RetailYearEndRule.NEAREST_END_OF_MONTH,
                    CalendarYearLabel.START_YEAR));

        assertEquals("id is null",e.getMessage());
    }

    @Test
    public void
    testOfNullPattern()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> RetailCalendar.of(
                    "X",
                    null,
                    Month.JANUARY,
                    DayOfWeek.SATURDAY,
                    RetailYearEndRule.NEAREST_END_OF_MONTH,
                    CalendarYearLabel.START_YEAR));

        assertEquals("pattern is null",e.getMessage());
    }

    @Test
    public void
    testOfNullYearEndMonth()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> RetailCalendar.of(
                    "X",
                    RetailWeekPattern.FOUR_FIVE_FOUR,
                    null,
                    DayOfWeek.SATURDAY,
                    RetailYearEndRule.NEAREST_END_OF_MONTH,
                    CalendarYearLabel.START_YEAR));

        assertEquals("yearEndMonth is null",e.getMessage());
    }

    @Test
    public void
    testOfNullYearEndDay()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> RetailCalendar.of(
                    "X",
                    RetailWeekPattern.FOUR_FIVE_FOUR,
                    Month.JANUARY,
                    null,
                    RetailYearEndRule.NEAREST_END_OF_MONTH,
                    CalendarYearLabel.START_YEAR));

        assertEquals("yearEndDay is null",e.getMessage());
    }

    @Test
    public void
    testOfNullYearEndRule()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> RetailCalendar.of(
                    "X",
                    RetailWeekPattern.FOUR_FIVE_FOUR,
                    Month.JANUARY,
                    DayOfWeek.SATURDAY,
                    null,
                    CalendarYearLabel.START_YEAR));

        assertEquals("yearEndRule is null",e.getMessage());
    }

    @Test
    public void
    testOfNullYearLabel()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> RetailCalendar.of(
                    "X",
                    RetailWeekPattern.FOUR_FIVE_FOUR,
                    Month.JANUARY,
                    DayOfWeek.SATURDAY,
                    RetailYearEndRule.NEAREST_END_OF_MONTH,
                    null));

        assertEquals("yearLabel is null",e.getMessage());
    }

    @Test
    public void
    testOfBlankId()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> RetailCalendar.of(
                    " ",
                    RetailWeekPattern.FOUR_FIVE_FOUR,
                    Month.JANUARY,
                    DayOfWeek.SATURDAY,
                    RetailYearEndRule.NEAREST_END_OF_MONTH,
                    CalendarYearLabel.START_YEAR));

        assertEquals("id is blank",e.getMessage());
    }

    @Test
    public void
    testOfInvalidId()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> RetailCalendar.of(
                    "A[B",
                    RetailWeekPattern.FOUR_FIVE_FOUR,
                    Month.JANUARY,
                    DayOfWeek.SATURDAY,
                    RetailYearEndRule.NEAREST_END_OF_MONTH,
                    CalendarYearLabel.START_YEAR));

        assertEquals("id must not contain '[' or ']'",e.getMessage());
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        RetailCalendar same =
            RetailCalendar.of(
                "NRF",
                RetailWeekPattern.FOUR_FIVE_FOUR,
                Month.JANUARY,
                DayOfWeek.SATURDAY,
                RetailYearEndRule.NEAREST_END_OF_MONTH,
                CalendarYearLabel.START_YEAR);

        assertEquals(RetailCalendar.NRF,same);
        assertEquals(RetailCalendar.NRF.hashCode(),same.hashCode());
        assertNotEquals(null,RetailCalendar.NRF);
        assertNotEquals("NRF",RetailCalendar.NRF);
    }

    @ParameterizedTest
    @MethodSource("differingCalendarSources")
    public void
    testNotEqualWhenArgumentDiffers(
        String            id,
        RetailWeekPattern pattern,
        Month             month,
        DayOfWeek         day,
        RetailYearEndRule rule,
        CalendarYearLabel label)
    {
        assertNotEquals(RetailCalendar.NRF,RetailCalendar.of(id,pattern,month,day,rule,label));
    }

    @Test
    public void
    testSerialization() throws IOException, ClassNotFoundException
    {
        assertEquals(RetailCalendar.NRF,roundTrip(RetailCalendar.NRF));
        assertEquals(LASTDEC_END,roundTrip(LASTDEC_END));
    }

    @Test
    public void
    testFieldAndUnitAccessors()
    {
        assertEquals("WeekOfYear[NRF]",RetailCalendar.NRF.weekOfYear().toString());
        assertEquals("Months[NRF]",RetailCalendar.NRF.months().toString());
        assertEquals(RetailCalendar.NRF.weekOfYear(),RetailCalendar.NRF.weekOfYear());
        assertNotEquals(RetailCalendar.NRF.weekOfYear(),T445.weekOfYear());
    }
}

//////////////////////////////////////////////////////////////////////////////
