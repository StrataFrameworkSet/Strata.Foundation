//////////////////////////////////////////////////////////////////////////////
// RetailYearQuarter.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import strata.foundation.core.utility.Validate;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final
class RetailYearQuarter
    extends AbstractCalendarPeriod<RetailYearQuarter,RetailCalendar>
{
    private static final Pattern PATTERN =
        Pattern.compile("^(?<year>[+-]?\\d{4,10})-Q(?<quarter>\\d)(?:\\[(?<id>[^\\[\\]]+)\\])?$");

    private final int year;
    private final int quarter;

    private
    RetailYearQuarter(RetailCalendar calendar,int year,int quarter)
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
        RetailCalendar calendar = getCalendar();

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
        RetailCalendar calendar = getCalendar();

        if (
            calendar.quarters().equals(unit) ||
            calendar.years().equals(unit))
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
    lengthInWeeks()
    {
        return getCalendar().lengthOfQuarterInWeeks(year,quarter);
    }

    public RetailYear
    getRetailYear()
    {
        return RetailYear.of(getCalendar(),year);
    }

    public RetailYearMonth
    atMonth(int monthOfQuarter)
        throws DateTimeException
    {
        ValueRange
            .of(1,3)
            .checkValidIntValue(monthOfQuarter,getCalendar().monthOfQuarter());

        return RetailYearMonth.of(getCalendar(),year,(quarter - 1) * 3 + monthOfQuarter);
    }

    public RetailYearWeek
    atWeek(int weekOfQuarter)
        throws DateTimeException
    {
        ValueRange
            .of(1,lengthInWeeks())
            .checkValidIntValue(weekOfQuarter,getCalendar().weekOfQuarter());

        return RetailYearWeek.from(getCalendar(),getFirstDay().plusWeeks(weekOfQuarter - 1));
    }

    public LocalDate
    atDay(int dayOfQuarter)
        throws DateTimeException
    {
        Validate.require(
            dayOfQuarter >= 1 && dayOfQuarter <= lengthInWeeks() * 7,
            () -> new DateTimeException("Invalid day of quarter: " + dayOfQuarter));

        return getFirstDay().plusDays(dayOfQuarter - 1);
    }

    public RetailYearQuarter
    plusQuarters(long quarters)
        throws DateTimeException
    {
        return plus(quarters,getCalendar().quarters());
    }

    public RetailYearQuarter
    minusQuarters(long quarters)
        throws DateTimeException
    {
        return minus(quarters,getCalendar().quarters());
    }

    public RetailYearQuarter
    plusYears(long years)
        throws DateTimeException
    {
        return plus(years,getCalendar().years());
    }

    public RetailYearQuarter
    minusYears(long years)
        throws DateTimeException
    {
        return minus(years,getCalendar().years());
    }

    public RetailYearQuarter
    withYear(int year)
        throws DateTimeException
    {
        return with(getCalendar().year(),year);
    }

    public RetailYearQuarter
    withQuarter(int quarter)
        throws DateTimeException
    {
        return with(getCalendar().quarterOfYear(),quarter);
    }

    public static RetailYearQuarter
    of(int year,int quarter)
        throws DateTimeException
    {
        return of(RetailCalendarRegistry.getDefault(),year,quarter);
    }

    public static RetailYearQuarter
    of(RetailCalendar calendar,int year,int quarter)
        throws DateTimeException, NullPointerException
    {
        Validate.requireNonNull(calendar,"calendar is null");

        calendar.getQuarterStart(year,quarter);

        return new RetailYearQuarter(calendar,year,quarter);
    }

    public static RetailYearQuarter
    from(TemporalAccessor temporal)
        throws DateTimeException, NullPointerException
    {
        return from(RetailCalendarRegistry.getDefault(),temporal);
    }

    public static RetailYearQuarter
    from(RetailCalendar calendar,TemporalAccessor temporal)
        throws DateTimeException, NullPointerException
    {
        Validate.requireNonNull(calendar,"calendar is null");
        Validate.requireNonNull(temporal,"temporal is null");

        if (temporal instanceof RetailYearQuarter other && other.getCalendar().equals(calendar))
            return other;

        try
        {
            LocalDate date =
                temporal instanceof ICalendarPeriod period
                    ? period.getFirstDay()
                    : LocalDate.from(temporal);

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
                    "Unable to obtain RetailYearQuarter from TemporalAccessor: " +
                    temporal + " of type " + temporal.getClass().getName(),
                    e);
        }
    }

    public static RetailYearQuarter
    now()
    {
        return now(Clock.systemDefaultZone());
    }

    public static RetailYearQuarter
    now(ZoneId zone)
        throws NullPointerException
    {
        Validate.requireNonNull(zone,"zone is null");

        return now(Clock.system(zone));
    }

    public static RetailYearQuarter
    now(Clock clock)
        throws NullPointerException
    {
        return now(RetailCalendarRegistry.getDefault(),clock);
    }

    public static RetailYearQuarter
    now(RetailCalendar calendar,Clock clock)
        throws NullPointerException
    {
        Validate.requireNonNull(clock,"clock is null");

        return from(calendar,LocalDate.now(clock));
    }

    public static RetailYearQuarter
    parse(CharSequence text)
        throws DateTimeParseException, NullPointerException
    {
        return parse(text,null,RetailCalendarRegistry.getDefault());
    }

    public static RetailYearQuarter
    parse(CharSequence text,RetailCalendar calendar)
        throws DateTimeParseException, NullPointerException
    {
        Validate.requireNonNull(calendar,"calendar is null");

        return parse(text,calendar,calendar);
    }

    @Override
    protected TemporalUnit
    getBaseUnit()
    {
        return getCalendar().quarters();
    }

    @Override
    protected long
    getIndex()
    {
        return year * 4L + quarter - 1;
    }

    @Override
    protected RetailYearQuarter
    fromDate(LocalDate date)
    {
        return from(getCalendar(),date);
    }

    @Override
    protected RetailYearQuarter
    fromTemporal(TemporalAccessor temporal)
    {
        return from(getCalendar(),temporal);
    }

    private static RetailYearQuarter
    parse(CharSequence text,RetailCalendar expected,RetailCalendar defaultCalendar)
        throws DateTimeParseException, NullPointerException
    {
        Matcher matcher = matchText(PATTERN,text);
        RetailCalendar calendar =
            resolveCalendar(
                text,
                matcher.group("id"),
                expected,
                defaultCalendar,
                RetailCalendarRegistry::get);

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
    private static RetailYearQuarter
    fromJson(
        @JsonProperty("calendar") String calendar,
        @JsonProperty("value")    String value)
    {
        Validate.requireNonNull(calendar,"calendar is null");

        return parse(value,RetailCalendarRegistry.getOrThrow(calendar));
    }
}

//////////////////////////////////////////////////////////////////////////////
