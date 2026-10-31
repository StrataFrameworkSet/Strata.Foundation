//////////////////////////////////////////////////////////////////////////////
// RetailCalendar.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import strata.foundation.core.utility.Require;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.util.Objects;

public final
class RetailCalendar
    implements ICalendarDefinition
{
    static final ValueRange YEAR_RANGE =
        ValueRange.of(Year.MIN_VALUE + 2,Year.MAX_VALUE - 2);

    public static final RetailCalendar NRF =
        of(
            "NRF",
            RetailWeekPattern.FOUR_FIVE_FOUR,
            Month.JANUARY,
            DayOfWeek.SATURDAY,
            RetailYearEndRule.NEAREST_END_OF_MONTH,
            CalendarYearLabel.START_YEAR);

    private final String            id;
    private final RetailWeekPattern pattern;
    private final Month             yearEndMonth;
    private final DayOfWeek         yearEndDay;
    private final RetailYearEndRule yearEndRule;
    private final CalendarYearLabel yearLabel;

    private
    RetailCalendar(
        String            id,
        RetailWeekPattern pattern,
        Month             yearEndMonth,
        DayOfWeek         yearEndDay,
        RetailYearEndRule yearEndRule,
        CalendarYearLabel yearLabel)
    {
        this.id           = id;
        this.pattern      = pattern;
        this.yearEndMonth = yearEndMonth;
        this.yearEndDay   = yearEndDay;
        this.yearEndRule  = yearEndRule;
        this.yearLabel    = yearLabel;
    }

    @Override
    public boolean
    equals(Object other)
    {
        return
            other instanceof RetailCalendar o &&
            id.equals(o.id) &&
            pattern == o.pattern &&
            yearEndMonth == o.yearEndMonth &&
            yearEndDay == o.yearEndDay &&
            yearEndRule == o.yearEndRule &&
            yearLabel == o.yearLabel;
    }

    @Override
    public int
    hashCode()
    {
        return Objects.hash(id,pattern,yearEndMonth,yearEndDay,yearEndRule,yearLabel);
    }

    @Override
    public String
    toString()
    {
        return id;
    }

    @Override
    public String getId() { return id; }

    public RetailWeekPattern getPattern() { return pattern; }

    public Month getYearEndMonth() { return yearEndMonth; }

    public DayOfWeek getYearEndDay() { return yearEndDay; }

    public RetailYearEndRule getYearEndRule() { return yearEndRule; }

    public CalendarYearLabel getYearLabel() { return yearLabel; }

    public DayOfWeek
    getFirstDayOfWeek()
    {
        return yearEndDay.plus(1);
    }

    public LocalDate
    getYearStart(int year)
        throws DateTimeException
    {
        checkYear(year);

        return anchor(getEndAnchorYear(year) - 1).plusDays(1);
    }

    public LocalDate
    getYearEnd(int year)
        throws DateTimeException
    {
        checkYear(year);

        return anchor(getEndAnchorYear(year));
    }

    public int
    lengthOfYearInWeeks(int year)
        throws DateTimeException
    {
        return (int)(ChronoUnit.DAYS.between(getYearStart(year),getYearEnd(year)) + 1) / 7;
    }

    public LocalDate
    getQuarterStart(int year,int quarter)
        throws DateTimeException
    {
        checkQuarter(quarter);

        return getYearStart(year).plusWeeks(13L * (quarter - 1));
    }

    public int
    lengthOfQuarterInWeeks(int year,int quarter)
        throws DateTimeException
    {
        checkQuarter(quarter);

        if (quarter == 4 && lengthOfYearInWeeks(year) == 53)
            return 14;

        return 13;
    }

    public LocalDate
    getMonthStart(int year,int month)
        throws DateTimeException
    {
        checkMonth(month);

        int quarter        = (month - 1) / 3 + 1;
        int monthOfQuarter = (month - 1) % 3 + 1;
        int weeks          = 0;

        for (int m = 1;m < monthOfQuarter;m++)
            weeks += pattern.getWeeksInMonthOfQuarter(m);

        return getQuarterStart(year,quarter).plusWeeks(weeks);
    }

    public int
    lengthOfMonthInWeeks(int year,int month)
        throws DateTimeException
    {
        checkMonth(month);

        int weeks = pattern.getWeeksInMonthOfQuarter((month - 1) % 3 + 1);

        if (month == 12 && lengthOfYearInWeeks(year) == 53)
            return weeks + 1;

        return weeks;
    }

    public LocalDate
    getWeekStart(int year,int week)
        throws DateTimeException
    {
        ValueRange
            .of(1,lengthOfYearInWeeks(year))
            .checkValidIntValue(week,weekOfYear());

        return getYearStart(year).plusWeeks(week - 1);
    }

    public int
    getYear(LocalDate date)
        throws DateTimeException, NullPointerException
    {
        Require.notNull(date,"date is null");

        int anchorYear = date.getYear();

        while (date.isAfter(anchor(anchorYear)))
            anchorYear++;

        while (!date.isAfter(anchor(anchorYear - 1)))
            anchorYear--;

        int year = yearLabel == CalendarYearLabel.START_YEAR ? anchorYear - 1 : anchorYear;

        checkYear(year);

        return year;
    }

    public TemporalField
    year()
    {
        return new RetailCalendarField(this,RetailFieldType.YEAR);
    }

    public TemporalField
    quarterOfYear()
    {
        return new RetailCalendarField(this,RetailFieldType.QUARTER_OF_YEAR);
    }

    public TemporalField
    monthOfQuarter()
    {
        return new RetailCalendarField(this,RetailFieldType.MONTH_OF_QUARTER);
    }

    public TemporalField
    monthOfYear()
    {
        return new RetailCalendarField(this,RetailFieldType.MONTH_OF_YEAR);
    }

    public TemporalField
    weekOfYear()
    {
        return new RetailCalendarField(this,RetailFieldType.WEEK_OF_YEAR);
    }

    public TemporalField
    weekOfQuarter()
    {
        return new RetailCalendarField(this,RetailFieldType.WEEK_OF_QUARTER);
    }

    public TemporalField
    weekOfMonth()
    {
        return new RetailCalendarField(this,RetailFieldType.WEEK_OF_MONTH);
    }

    public TemporalField
    dayOfYear()
    {
        return new RetailCalendarField(this,RetailFieldType.DAY_OF_YEAR);
    }

    public TemporalUnit
    months()
    {
        return new RetailCalendarUnit(this,RetailUnitType.MONTHS);
    }

    public TemporalUnit
    quarters()
    {
        return new RetailCalendarUnit(this,RetailUnitType.QUARTERS);
    }

    public TemporalUnit
    years()
    {
        return new RetailCalendarUnit(this,RetailUnitType.YEARS);
    }

    public static RetailCalendar
    of(
        String            id,
        RetailWeekPattern pattern,
        Month             yearEndMonth,
        DayOfWeek         yearEndDay,
        RetailYearEndRule yearEndRule,
        CalendarYearLabel yearLabel)
        throws NullPointerException, IllegalArgumentException
    {
        Require.notNull(id,"id is null");
        Require.notNull(pattern,"pattern is null");
        Require.notNull(yearEndMonth,"yearEndMonth is null");
        Require.notNull(yearEndDay,"yearEndDay is null");
        Require.notNull(yearEndRule,"yearEndRule is null");
        Require.notNull(yearLabel,"yearLabel is null");
        Require.isTrue(
            !id.isBlank(),
            () -> new IllegalArgumentException("id is blank"));
        Require.isTrue(
            id.indexOf('[') < 0 && id.indexOf(']') < 0,
            () -> new IllegalArgumentException("id must not contain '[' or ']'"));

        return new RetailCalendar(id,pattern,yearEndMonth,yearEndDay,yearEndRule,yearLabel);
    }

    int
    getQuarter(LocalDate date)
        throws DateTimeException
    {
        int year = getYear(date);

        return Math.min(4,(int)(ChronoUnit.DAYS.between(getYearStart(year),date) / 91) + 1);
    }

    int
    getMonth(LocalDate date)
        throws DateTimeException
    {
        int year = getYear(date);

        for (int month = 12;month > 1;month--)
            if (!date.isBefore(getMonthStart(year,month)))
                return month;

        return 1;
    }

    LocalDate
    shiftYears(LocalDate date,long years)
        throws DateTimeException
    {
        int year    = getYear(date);
        int newYear = Math.toIntExact(Math.addExact(year,years));

        return
            shift(
                getYearStart(year),
                date,
                getYearStart(newYear),
                lengthOfYearInWeeks(newYear));
    }

    LocalDate
    shiftQuarters(LocalDate date,long quarters)
        throws DateTimeException
    {
        int  year       = getYear(date);
        int  quarter    = getQuarter(date);
        long index      = Math.addExact(year * 4L + quarter - 1,quarters);
        int  newYear    = Math.toIntExact(Math.floorDiv(index,4));
        int  newQuarter = (int)Math.floorMod(index,4) + 1;

        return
            shift(
                getQuarterStart(year,quarter),
                date,
                getQuarterStart(newYear,newQuarter),
                lengthOfQuarterInWeeks(newYear,newQuarter));
    }

    LocalDate
    shiftMonths(LocalDate date,long months)
        throws DateTimeException
    {
        int  year     = getYear(date);
        int  month    = getMonth(date);
        long index    = Math.addExact(year * 12L + month - 1,months);
        int  newYear  = Math.toIntExact(Math.floorDiv(index,12));
        int  newMonth = (int)Math.floorMod(index,12) + 1;

        return
            shift(
                getMonthStart(year,month),
                date,
                getMonthStart(newYear,newMonth),
                lengthOfMonthInWeeks(newYear,newMonth));
    }

    private LocalDate
    shift(LocalDate fromStart,LocalDate date,LocalDate toStart,int toLengthInWeeks)
    {
        long offset = ChronoUnit.DAYS.between(fromStart,date);
        long weeks  = Math.min(offset / 7,toLengthInWeeks - 1);

        return toStart.plusDays(weeks * 7 + offset % 7);
    }

    private int
    getEndAnchorYear(int year)
    {
        return yearLabel == CalendarYearLabel.START_YEAR ? year + 1 : year;
    }

    private LocalDate
    anchor(int anchorYear)
    {
        LocalDate monthEnd = YearMonth.of(anchorYear,yearEndMonth).atEndOfMonth();
        LocalDate before   = monthEnd.with(TemporalAdjusters.previousOrSame(yearEndDay));

        if (yearEndRule == RetailYearEndRule.LAST_OF_MONTH)
            return before;

        LocalDate after = monthEnd.with(TemporalAdjusters.nextOrSame(yearEndDay));

        if (ChronoUnit.DAYS.between(before,monthEnd) <= ChronoUnit.DAYS.between(monthEnd,after))
            return before;

        return after;
    }

    private void
    checkYear(int year)
        throws DateTimeException
    {
        YEAR_RANGE.checkValidIntValue(year,year());
    }

    private void
    checkQuarter(int quarter)
        throws DateTimeException
    {
        ValueRange.of(1,4).checkValidIntValue(quarter,quarterOfYear());
    }

    private void
    checkMonth(int month)
        throws DateTimeException
    {
        ValueRange.of(1,12).checkValidIntValue(month,monthOfYear());
    }
}

//////////////////////////////////////////////////////////////////////////////
