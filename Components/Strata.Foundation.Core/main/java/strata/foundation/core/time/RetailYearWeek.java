//////////////////////////////////////////////////////////////////////////////
// RetailYearWeek.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import strata.foundation.core.utility.Require;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Immutable week of a {@link RetailYear}, numbered 1 to 52 or 53.
 * Formatted and parsed as the year and week with an optional calendar id,
 * such as {@code 2026-W01[NRF]}.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * RetailYearWeek week = RetailYearWeek.of(RetailCalendar.NRF,2026,1);
 * RetailYearWeek parsed = RetailYearWeek.parse("2026-W01[NRF]",RetailCalendar.NRF);
 *
 * // Usage
 * RetailYearMonth month = week.getRetailYearMonth();
 * LocalDate first = week.getFirstDay();
 * RetailYearWeek next = week.plus(1,ChronoUnit.WEEKS);
 * </pre>
 */
public final
class RetailYearWeek
    extends AbstractCalendarPeriod<RetailYearWeek,RetailCalendar>
{
    private static final Pattern PATTERN =
        Pattern.compile("^(?<year>[+-]?\\d{4,10})-W(?<week>\\d{2})(?:\\[(?<id>[^\\[\\]]+)\\])?$");

    private final int year;
    private final int week;

    private
    RetailYearWeek(RetailCalendar calendar,int year,int week)
    {
        super(calendar);
        this.year = year;
        this.week = week;
    }

    @Override
    public LocalDate
    getFirstDay()
    {
        return getCalendar().getWeekStart(year,week);
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
            calendar.monthOfQuarter().equals(field) ||
            calendar.weekOfYear().equals(field) ||
            calendar.weekOfQuarter().equals(field) ||
            calendar.weekOfMonth().equals(field))
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
            unit == ChronoUnit.WEEKS ||
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
                .with(getCalendar().weekOfYear(),week);
    }

    @Override
    public String
    format()
    {
        return formatYear(year) + "-W" + String.format("%02d",week);
    }

    public int getYear() { return year; }

    public int getWeek() { return week; }

    public RetailYear
    getRetailYear()
    {
        return RetailYear.of(getCalendar(),year);
    }

    public RetailYearQuarter
    getRetailYearQuarter()
    {
        return RetailYearQuarter.from(getCalendar(),getFirstDay());
    }

    public RetailYearMonth
    getRetailYearMonth()
    {
        return RetailYearMonth.from(getCalendar(),getFirstDay());
    }

    public LocalDate
    atDay(DayOfWeek day)
        throws NullPointerException
    {
        Require.notNull(day,"day is null");

        return getFirstDay().with(TemporalAdjusters.nextOrSame(day));
    }

    public RetailYearWeek
    plusWeeks(long weeks)
        throws DateTimeException
    {
        return plus(weeks,ChronoUnit.WEEKS);
    }

    public RetailYearWeek
    minusWeeks(long weeks)
        throws DateTimeException
    {
        return minus(weeks,ChronoUnit.WEEKS);
    }

    public RetailYearWeek
    plusYears(long years)
        throws DateTimeException
    {
        return plus(years,getCalendar().years());
    }

    public RetailYearWeek
    minusYears(long years)
        throws DateTimeException
    {
        return minus(years,getCalendar().years());
    }

    public RetailYearWeek
    withYear(int year)
        throws DateTimeException
    {
        return with(getCalendar().year(),year);
    }

    public RetailYearWeek
    withWeek(int week)
        throws DateTimeException
    {
        return with(getCalendar().weekOfYear(),week);
    }

    public static RetailYearWeek
    of(int year,int week)
        throws DateTimeException
    {
        return of(RetailCalendarRegistry.getDefault(),year,week);
    }

    public static RetailYearWeek
    of(RetailCalendar calendar,int year,int week)
        throws DateTimeException, NullPointerException
    {
        Require.notNull(calendar,"calendar is null");

        calendar.getWeekStart(year,week);

        return new RetailYearWeek(calendar,year,week);
    }

    public static RetailYearWeek
    from(TemporalAccessor temporal)
        throws DateTimeException, NullPointerException
    {
        return from(RetailCalendarRegistry.getDefault(),temporal);
    }

    public static RetailYearWeek
    from(RetailCalendar calendar,TemporalAccessor temporal)
        throws DateTimeException, NullPointerException
    {
        Require.notNull(calendar,"calendar is null");
        Require.notNull(temporal,"temporal is null");

        if (temporal instanceof RetailYearWeek other && other.getCalendar().equals(calendar))
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
                    date.get(calendar.weekOfYear()));
        }
        catch (DateTimeException e)
        {
            throw
                new DateTimeException(
                    "Unable to obtain RetailYearWeek from TemporalAccessor: " +
                    temporal + " of type " + temporal.getClass().getName(),
                    e);
        }
    }

    public static RetailYearWeek
    now()
    {
        return now(Clock.systemDefaultZone());
    }

    public static RetailYearWeek
    now(ZoneId zone)
        throws NullPointerException
    {
        Require.notNull(zone,"zone is null");

        return now(Clock.system(zone));
    }

    public static RetailYearWeek
    now(Clock clock)
        throws NullPointerException
    {
        return now(RetailCalendarRegistry.getDefault(),clock);
    }

    public static RetailYearWeek
    now(RetailCalendar calendar,Clock clock)
        throws NullPointerException
    {
        Require.notNull(clock,"clock is null");

        return from(calendar,LocalDate.now(clock));
    }

    public static RetailYearWeek
    parse(CharSequence text)
        throws DateTimeParseException, NullPointerException
    {
        return parse(text,null,RetailCalendarRegistry.getDefault());
    }

    public static RetailYearWeek
    parse(CharSequence text,RetailCalendar calendar)
        throws DateTimeParseException, NullPointerException
    {
        Require.notNull(calendar,"calendar is null");

        return parse(text,calendar,calendar);
    }

    @Override
    protected TemporalUnit
    getBaseUnit()
    {
        return ChronoUnit.WEEKS;
    }

    @Override
    protected long
    getIndex()
    {
        return getFirstDay().toEpochDay();
    }

    @Override
    protected RetailYearWeek
    fromDate(LocalDate date)
    {
        return from(getCalendar(),date);
    }

    @Override
    protected RetailYearWeek
    fromTemporal(TemporalAccessor temporal)
    {
        return from(getCalendar(),temporal);
    }

    private static RetailYearWeek
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
                    Integer.parseInt(matcher.group("week")));
        }
        catch (DateTimeException | NumberFormatException e)
        {
            throw new DateTimeParseException(e.getMessage(),text,0,e);
        }
    }

    @JsonCreator
    private static RetailYearWeek
    fromJson(
        @JsonProperty("calendar") String calendar,
        @JsonProperty("value")    String value)
    {
        Require.notNull(calendar,"calendar is null");

        return parse(value,RetailCalendarRegistry.getOrThrow(calendar));
    }
}

//////////////////////////////////////////////////////////////////////////////
