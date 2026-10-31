//////////////////////////////////////////////////////////////////////////////
// FiscalCalendar.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import strata.foundation.core.utility.Require;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.util.Objects;

public final
class FiscalCalendar
    implements ICalendarDefinition
{
    static final ValueRange YEAR_RANGE =
        ValueRange.of(Year.MIN_VALUE + 2,Year.MAX_VALUE - 2);

    public static final FiscalCalendar CALENDAR_YEAR =
        of("CALENDAR_YEAR",Month.JANUARY,CalendarYearLabel.END_YEAR);

    public static final FiscalCalendar US_FEDERAL =
        of("US_FEDERAL",Month.OCTOBER,CalendarYearLabel.END_YEAR);

    public static final FiscalCalendar US_STATE =
        of("US_STATE",Month.JULY,CalendarYearLabel.END_YEAR);

    public static final FiscalCalendar UK_GOVERNMENT =
        of("UK_GOVERNMENT",Month.APRIL,CalendarYearLabel.END_YEAR);

    public static final FiscalCalendar EU_GOVERNMENT =
        of("EU_GOVERNMENT",Month.JANUARY,CalendarYearLabel.END_YEAR);

    private final String            id;
    private final Month             startMonth;
    private final CalendarYearLabel yearLabel;
    private final DayOfWeek         firstDayOfWeek;
    private final int               minimalDaysInFirstWeek;

    private
    FiscalCalendar(
        String            id,
        Month             startMonth,
        CalendarYearLabel yearLabel,
        DayOfWeek         firstDayOfWeek,
        int               minimalDaysInFirstWeek)
    {
        this.id                     = id;
        this.startMonth             = startMonth;
        this.yearLabel              = yearLabel;
        this.firstDayOfWeek         = firstDayOfWeek;
        this.minimalDaysInFirstWeek = minimalDaysInFirstWeek;
    }

    @Override
    public String getId() { return id; }

    @Override
    public boolean
    equals(Object other)
    {
        return
            other instanceof FiscalCalendar o &&
            id.equals(o.id) &&
            startMonth == o.startMonth &&
            yearLabel == o.yearLabel &&
            firstDayOfWeek == o.firstDayOfWeek &&
            minimalDaysInFirstWeek == o.minimalDaysInFirstWeek;
    }

    @Override
    public int
    hashCode()
    {
        return Objects.hash(id,startMonth,yearLabel,firstDayOfWeek,minimalDaysInFirstWeek);
    }

    @Override
    public String
    toString()
    {
        return id;
    }

    public Month getStartMonth() { return startMonth; }

    public CalendarYearLabel getYearLabel() { return yearLabel; }

    public DayOfWeek getFirstDayOfWeek() { return firstDayOfWeek; }

    public int getMinimalDaysInFirstWeek() { return minimalDaysInFirstWeek; }

    public LocalDate
    getYearStart(int year)
        throws DateTimeException
    {
        checkYear(year);

        return LocalDate.of(getStartGregorianYear(year),startMonth,1);
    }

    public LocalDate
    getYearEnd(int year)
        throws DateTimeException
    {
        return getYearStart(year).plusYears(1).minusDays(1);
    }

    public int
    lengthOfYearInDays(int year)
        throws DateTimeException
    {
        LocalDate start = getYearStart(year);

        return (int)ChronoUnit.DAYS.between(start,start.plusYears(1));
    }

    public LocalDate
    getQuarterStart(int year,int quarter)
        throws DateTimeException
    {
        ValueRange.of(1,4).checkValidIntValue(quarter,quarterOfYear());

        return getYearStart(year).plusMonths(3L * (quarter - 1));
    }

    public LocalDate
    getMonthStart(int year,int month)
        throws DateTimeException
    {
        ValueRange.of(1,12).checkValidIntValue(month,monthOfYear());

        return getYearStart(year).plusMonths(month - 1);
    }

    public int
    lengthOfMonthInDays(int year,int month)
        throws DateTimeException
    {
        return getMonthStart(year,month).lengthOfMonth();
    }

    public int
    getYear(LocalDate date)
        throws DateTimeException, NullPointerException
    {
        Require.notNull(date,"date is null");

        int startYear =
            date.getMonthValue() >= startMonth.getValue()
                ? date.getYear()
                : date.getYear() - 1;
        int year      = getLabelYear(startYear);

        checkYear(year);

        return year;
    }

    public LocalDate
    getWeekStart(int weekBasedYear,int week)
        throws DateTimeException
    {
        ValueRange
            .of(1,lengthOfWeekBasedYearInWeeks(weekBasedYear))
            .checkValidIntValue(week,weekOfWeekBasedYear());

        return getFirstWeekStart(weekBasedYear).plusWeeks(week - 1);
    }

    public int
    lengthOfWeekBasedYearInWeeks(int weekBasedYear)
        throws DateTimeException
    {
        return
            (int)(ChronoUnit.DAYS.between(
                getFirstWeekStart(weekBasedYear),
                getFirstWeekStart(weekBasedYear + 1)) / 7);
    }

    public int
    getWeekBasedYear(LocalDate date)
        throws DateTimeException, NullPointerException
    {
        int year = getYear(date);

        if (date.isBefore(getFirstWeekStart(year)))
            return year - 1;

        if (!date.isBefore(getFirstWeekStart(year + 1)))
            return year + 1;

        return year;
    }

    public TemporalField
    year()
    {
        return new FiscalCalendarField(this,FiscalFieldType.YEAR);
    }

    public TemporalField
    quarterOfYear()
    {
        return new FiscalCalendarField(this,FiscalFieldType.QUARTER_OF_YEAR);
    }

    public TemporalField
    monthOfQuarter()
    {
        return new FiscalCalendarField(this,FiscalFieldType.MONTH_OF_QUARTER);
    }

    public TemporalField
    monthOfYear()
    {
        return new FiscalCalendarField(this,FiscalFieldType.MONTH_OF_YEAR);
    }

    public TemporalField
    dayOfYear()
    {
        return new FiscalCalendarField(this,FiscalFieldType.DAY_OF_YEAR);
    }

    public TemporalField
    weekBasedYear()
    {
        return new FiscalCalendarField(this,FiscalFieldType.WEEK_BASED_YEAR);
    }

    public TemporalField
    weekOfWeekBasedYear()
    {
        return new FiscalCalendarField(this,FiscalFieldType.WEEK_OF_WEEK_BASED_YEAR);
    }

    public TemporalUnit
    weekBasedYears()
    {
        return new FiscalCalendarUnit(this,FiscalUnitType.WEEK_BASED_YEARS);
    }

    public static FiscalCalendar
    of(String id,Month startMonth,CalendarYearLabel yearLabel)
        throws NullPointerException, IllegalArgumentException
    {
        return of(id,startMonth,yearLabel,DayOfWeek.MONDAY,4);
    }

    public static FiscalCalendar
    of(
        String            id,
        Month             startMonth,
        CalendarYearLabel yearLabel,
        DayOfWeek         firstDayOfWeek,
        int               minimalDaysInFirstWeek)
        throws NullPointerException, IllegalArgumentException
    {
        Require.notNull(id,"id is null");
        Require.notNull(startMonth,"startMonth is null");
        Require.notNull(yearLabel,"yearLabel is null");
        Require.notNull(firstDayOfWeek,"firstDayOfWeek is null");
        Require.isTrue(
            !id.isBlank(),
            () -> new IllegalArgumentException("id is blank"));
        Require.isTrue(
            id.indexOf('[') < 0 && id.indexOf(']') < 0,
            () -> new IllegalArgumentException("id must not contain '[' or ']'"));
        Require.isTrue(
            minimalDaysInFirstWeek >= 1 && minimalDaysInFirstWeek <= 7,
            () ->
                new IllegalArgumentException(
                    "minimalDaysInFirstWeek must be between 1 and 7"));

        return
            new FiscalCalendar(
                id,
                startMonth,
                yearLabel,
                firstDayOfWeek,
                minimalDaysInFirstWeek);
    }

    private int
    getStartGregorianYear(int year)
    {
        if (yearLabel == CalendarYearLabel.START_YEAR || startMonth == Month.JANUARY)
            return year;

        return year - 1;
    }

    private int
    getLabelYear(int startGregorianYear)
    {
        if (yearLabel == CalendarYearLabel.START_YEAR || startMonth == Month.JANUARY)
            return startGregorianYear;

        return startGregorianYear + 1;
    }

    private LocalDate
    getFirstWeekStart(int weekBasedYear)
    {
        return
            getYearStart(weekBasedYear)
                .plusDays(minimalDaysInFirstWeek - 1)
                .with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
    }

    private void
    checkYear(int year)
        throws DateTimeException
    {
        YEAR_RANGE.checkValidIntValue(year,year());
    }
}

//////////////////////////////////////////////////////////////////////////////
