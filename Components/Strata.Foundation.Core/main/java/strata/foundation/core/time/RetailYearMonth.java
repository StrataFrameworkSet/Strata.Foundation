//////////////////////////////////////////////////////////////////////////////
// RetailYearMonth.java
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
class RetailYearMonth
    extends AbstractCalendarPeriod<RetailYearMonth,RetailCalendar>
{
    private static final Pattern PATTERN =
        Pattern.compile("^(?<year>[+-]?\\d{4,10})-M(?<month>\\d{2})(?:\\[(?<id>[^\\[\\]]+)\\])?$");

    private final int year;
    private final int month;

    private
    RetailYearMonth(RetailCalendar calendar,int year,int month)
    {
        super(calendar);
        this.year  = year;
        this.month = month;
    }

    @Override
    public LocalDate
    getFirstDay()
    {
        return getCalendar().getMonthStart(year,month);
    }

    @Override
    public boolean
    isSupported(TemporalField field)
    {
        RetailCalendar calendar = getCalendar();

        if (
            calendar.year().equals(field) ||
            calendar.quarterOfYear().equals(field) ||
            calendar.monthOfYear().equals(field) ||
            calendar.monthOfQuarter().equals(field))
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
            calendar.months().equals(unit) ||
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
                .with(getCalendar().monthOfYear(),month);
    }

    @Override
    public String
    format()
    {
        return formatYear(year) + "-M" + String.format("%02d",month);
    }

    public int getYear() { return year; }

    public int getMonth() { return month; }

    public int
    lengthInWeeks()
    {
        return getCalendar().lengthOfMonthInWeeks(year,month);
    }

    public RetailYear
    getRetailYear()
    {
        return RetailYear.of(getCalendar(),year);
    }

    public RetailYearQuarter
    getRetailYearQuarter()
    {
        return RetailYearQuarter.of(getCalendar(),year,(month - 1) / 3 + 1);
    }

    public RetailYearWeek
    atWeek(int weekOfMonth)
        throws DateTimeException
    {
        ValueRange
            .of(1,lengthInWeeks())
            .checkValidIntValue(weekOfMonth,getCalendar().weekOfMonth());

        return RetailYearWeek.from(getCalendar(),getFirstDay().plusWeeks(weekOfMonth - 1));
    }

    public LocalDate
    atDay(int dayOfMonth)
        throws DateTimeException
    {
        Validate.require(
            dayOfMonth >= 1 && dayOfMonth <= lengthInWeeks() * 7,
            () -> new DateTimeException("Invalid day of month: " + dayOfMonth));

        return getFirstDay().plusDays(dayOfMonth - 1);
    }

    public RetailYearMonth
    plusMonths(long months)
        throws DateTimeException
    {
        return plus(months,getCalendar().months());
    }

    public RetailYearMonth
    minusMonths(long months)
        throws DateTimeException
    {
        return minus(months,getCalendar().months());
    }

    public RetailYearMonth
    plusYears(long years)
        throws DateTimeException
    {
        return plus(years,getCalendar().years());
    }

    public RetailYearMonth
    minusYears(long years)
        throws DateTimeException
    {
        return minus(years,getCalendar().years());
    }

    public RetailYearMonth
    withYear(int year)
        throws DateTimeException
    {
        return with(getCalendar().year(),year);
    }

    public RetailYearMonth
    withMonth(int month)
        throws DateTimeException
    {
        return with(getCalendar().monthOfYear(),month);
    }

    public static RetailYearMonth
    of(int year,int month)
        throws DateTimeException
    {
        return of(RetailCalendarRegistry.getDefault(),year,month);
    }

    public static RetailYearMonth
    of(RetailCalendar calendar,int year,int month)
        throws DateTimeException, NullPointerException
    {
        Validate.requireNonNull(calendar,"calendar is null");

        calendar.getMonthStart(year,month);

        return new RetailYearMonth(calendar,year,month);
    }

    public static RetailYearMonth
    from(TemporalAccessor temporal)
        throws DateTimeException, NullPointerException
    {
        return from(RetailCalendarRegistry.getDefault(),temporal);
    }

    public static RetailYearMonth
    from(RetailCalendar calendar,TemporalAccessor temporal)
        throws DateTimeException, NullPointerException
    {
        Validate.requireNonNull(calendar,"calendar is null");
        Validate.requireNonNull(temporal,"temporal is null");

        if (temporal instanceof RetailYearMonth other && other.getCalendar().equals(calendar))
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
                    date.get(calendar.monthOfYear()));
        }
        catch (DateTimeException e)
        {
            throw
                new DateTimeException(
                    "Unable to obtain RetailYearMonth from TemporalAccessor: " +
                    temporal + " of type " + temporal.getClass().getName(),
                    e);
        }
    }

    public static RetailYearMonth
    now()
    {
        return now(Clock.systemDefaultZone());
    }

    public static RetailYearMonth
    now(ZoneId zone)
        throws NullPointerException
    {
        Validate.requireNonNull(zone,"zone is null");

        return now(Clock.system(zone));
    }

    public static RetailYearMonth
    now(Clock clock)
        throws NullPointerException
    {
        return now(RetailCalendarRegistry.getDefault(),clock);
    }

    public static RetailYearMonth
    now(RetailCalendar calendar,Clock clock)
        throws NullPointerException
    {
        Validate.requireNonNull(clock,"clock is null");

        return from(calendar,LocalDate.now(clock));
    }

    public static RetailYearMonth
    parse(CharSequence text)
        throws DateTimeParseException, NullPointerException
    {
        return parse(text,null,RetailCalendarRegistry.getDefault());
    }

    public static RetailYearMonth
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
        return getCalendar().months();
    }

    @Override
    protected long
    getIndex()
    {
        return year * 12L + month - 1;
    }

    @Override
    protected RetailYearMonth
    fromDate(LocalDate date)
    {
        return from(getCalendar(),date);
    }

    @Override
    protected RetailYearMonth
    fromTemporal(TemporalAccessor temporal)
    {
        return from(getCalendar(),temporal);
    }

    private static RetailYearMonth
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
                    Integer.parseInt(matcher.group("month")));
        }
        catch (DateTimeException | NumberFormatException e)
        {
            throw new DateTimeParseException(e.getMessage(),text,0,e);
        }
    }

    @JsonCreator
    private static RetailYearMonth
    fromJson(
        @JsonProperty("calendar") String calendar,
        @JsonProperty("value")    String value)
    {
        Validate.requireNonNull(calendar,"calendar is null");

        return parse(value,RetailCalendarRegistry.getOrThrow(calendar));
    }
}

//////////////////////////////////////////////////////////////////////////////
