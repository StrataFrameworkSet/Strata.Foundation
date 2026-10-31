//////////////////////////////////////////////////////////////////////////////
// FiscalYearQuarter.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import strata.foundation.core.utility.Require;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final
class FiscalYearQuarter
    extends AbstractCalendarPeriod<FiscalYearQuarter,FiscalCalendar>
{
    private static final Pattern PATTERN =
        Pattern.compile("^(?<year>[+-]?\\d{4,10})-Q(?<quarter>\\d)(?:\\[(?<id>[^\\[\\]]+)\\])?$");

    private final int year;
    private final int quarter;

    private
    FiscalYearQuarter(FiscalCalendar calendar,int year,int quarter)
    {
        super(calendar);
        this.year    = year;
        this.quarter = quarter;
    }

    @Override
    public LocalDate
    getFirstDay()
    {
        return getCalendar().getQuarterStart(year,quarter);
    }

    @Override
    public boolean
    isSupported(TemporalField field)
    {
        FiscalCalendar calendar = getCalendar();

        if (
            calendar.year().equals(field) ||
            calendar.quarterOfYear().equals(field))
            return true;

        if (field instanceof ChronoField)
            return false;

        return field != null && field.isSupportedBy(this);
    }

    @Override
    public boolean
    isSupported(TemporalUnit unit)
    {
        if (
            unit == IsoFields.QUARTER_YEARS ||
            unit == ChronoUnit.YEARS)
            return true;

        if (unit instanceof ChronoUnit)
            return false;

        return unit != null && unit.isSupportedBy(this);
    }

    @Override
    public Temporal
    adjustInto(Temporal temporal)
        throws DateTimeException
    {
        if (!IsoChronology.INSTANCE.equals(Chronology.from(temporal)))
            throw new DateTimeException("Adjustment only supported on ISO date-time");

        return
            temporal
                .with(getCalendar().year(),year)
                .with(getCalendar().quarterOfYear(),quarter);
    }

    @Override
    public String
    format()
    {
        return formatYear(year) + "-Q" + quarter;
    }

    public int getYear() { return year; }

    public int getQuarter() { return quarter; }

    public int
    lengthInDays()
    {
        return (int)ChronoUnit.DAYS.between(getFirstDay(),getFirstDay().plusMonths(3));
    }

    public FiscalYear
    getFiscalYear()
    {
        return FiscalYear.of(getCalendar(),year);
    }

    public FiscalYearMonth
    atMonth(int monthOfQuarter)
        throws DateTimeException
    {
        ValueRange.of(1,3).checkValidIntValue(monthOfQuarter,getCalendar().monthOfQuarter());

        return FiscalYearMonth.of(getCalendar(),year,(quarter - 1) * 3 + monthOfQuarter);
    }

    public LocalDate
    atDay(int dayOfQuarter)
        throws DateTimeException
    {
        Require.isTrue(
            dayOfQuarter >= 1 && dayOfQuarter <= lengthInDays(),
            () -> new DateTimeException("Invalid day of quarter: " + dayOfQuarter));

        return getFirstDay().plusDays(dayOfQuarter - 1);
    }

    public FiscalYearQuarter
    plusQuarters(long quarters)
        throws DateTimeException
    {
        return plus(quarters,IsoFields.QUARTER_YEARS);
    }

    public FiscalYearQuarter
    minusQuarters(long quarters)
        throws DateTimeException
    {
        return minus(quarters,IsoFields.QUARTER_YEARS);
    }

    public FiscalYearQuarter
    plusYears(long years)
        throws DateTimeException
    {
        return plus(years,ChronoUnit.YEARS);
    }

    public FiscalYearQuarter
    minusYears(long years)
        throws DateTimeException
    {
        return minus(years,ChronoUnit.YEARS);
    }

    public FiscalYearQuarter
    withYear(int year)
        throws DateTimeException
    {
        return with(getCalendar().year(),year);
    }

    public FiscalYearQuarter
    withQuarter(int quarter)
        throws DateTimeException
    {
        return with(getCalendar().quarterOfYear(),quarter);
    }

    public static FiscalYearQuarter
    of(int year,int quarter)
        throws DateTimeException
    {
        return of(FiscalCalendarRegistry.getDefault(),year,quarter);
    }

    public static FiscalYearQuarter
    of(FiscalCalendar calendar,int year,int quarter)
        throws DateTimeException, NullPointerException
    {
        Require.notNull(calendar,"calendar is null");

        calendar.getQuarterStart(year,quarter);

        return new FiscalYearQuarter(calendar,year,quarter);
    }

    public static FiscalYearQuarter
    from(TemporalAccessor temporal)
        throws DateTimeException, NullPointerException
    {
        return from(FiscalCalendarRegistry.getDefault(),temporal);
    }

    public static FiscalYearQuarter
    from(FiscalCalendar calendar,TemporalAccessor temporal)
        throws DateTimeException, NullPointerException
    {
        Require.notNull(calendar,"calendar is null");
        Require.notNull(temporal,"temporal is null");

        if (temporal instanceof FiscalYearQuarter other && other.getCalendar().equals(calendar))
            return other;

        try
        {
            LocalDate date = toFirstDate(temporal);

            return
                of(
                    calendar,
                    calendar.getYear(date),
                    date.get(calendar.quarterOfYear()));
        }
        catch (DateTimeException e)
        {
            throw
                new DateTimeException(
                    "Unable to obtain FiscalYearQuarter from TemporalAccessor: " +
                    temporal + " of type " + temporal.getClass().getName(),
                    e);
        }
    }

    public static FiscalYearQuarter
    now()
    {
        return now(Clock.systemDefaultZone());
    }

    public static FiscalYearQuarter
    now(ZoneId zone)
        throws NullPointerException
    {
        Require.notNull(zone,"zone is null");

        return now(Clock.system(zone));
    }

    public static FiscalYearQuarter
    now(Clock clock)
        throws NullPointerException
    {
        return now(FiscalCalendarRegistry.getDefault(),clock);
    }

    public static FiscalYearQuarter
    now(FiscalCalendar calendar,Clock clock)
        throws NullPointerException
    {
        Require.notNull(clock,"clock is null");

        return from(calendar,LocalDate.now(clock));
    }

    public static FiscalYearQuarter
    parse(CharSequence text)
        throws DateTimeParseException, NullPointerException
    {
        return parse(text,null,FiscalCalendarRegistry.getDefault());
    }

    public static FiscalYearQuarter
    parse(CharSequence text,FiscalCalendar calendar)
        throws DateTimeParseException, NullPointerException
    {
        Require.notNull(calendar,"calendar is null");

        return parse(text,calendar,calendar);
    }

    @Override
    protected TemporalUnit
    getBaseUnit()
    {
        return IsoFields.QUARTER_YEARS;
    }

    @Override
    protected long
    getIndex()
    {
        return year * 4L + quarter - 1;
    }

    @Override
    protected FiscalYearQuarter
    fromDate(LocalDate date)
    {
        return from(getCalendar(),date);
    }

    @Override
    protected FiscalYearQuarter
    fromTemporal(TemporalAccessor temporal)
    {
        return from(getCalendar(),temporal);
    }

    private static FiscalYearQuarter
    parse(CharSequence text,FiscalCalendar expected,FiscalCalendar defaultCalendar)
        throws DateTimeParseException, NullPointerException
    {
        Matcher matcher = matchText(PATTERN,text);
        FiscalCalendar calendar =
            resolveCalendar(
                text,
                matcher.group("id"),
                expected,
                defaultCalendar,
                FiscalCalendarRegistry::get);

        try
        {
            return
                of(
                    calendar,
                    Integer.parseInt(matcher.group("year")),
                    Integer.parseInt(matcher.group("quarter")));
        }
        catch (DateTimeException | NumberFormatException e)
        {
            throw new DateTimeParseException(e.getMessage(),text,0,e);
        }
    }

    @JsonCreator
    private static FiscalYearQuarter
    fromJson(
        @JsonProperty("calendar") String calendar,
        @JsonProperty("value")    String value)
    {
        Require.notNull(calendar,"calendar is null");

        return parse(value,FiscalCalendarRegistry.getOrThrow(calendar));
    }
}

//////////////////////////////////////////////////////////////////////////////
