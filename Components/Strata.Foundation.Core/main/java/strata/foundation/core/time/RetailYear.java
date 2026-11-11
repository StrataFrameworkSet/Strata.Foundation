//////////////////////////////////////////////////////////////////////////////
// RetailYear.java
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
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Immutable year of a {@link RetailCalendar}, containing 52 or 53 whole
 * weeks and labeled according to the calendar's {@link CalendarYearLabel}.
 * Formatted and parsed as the year number with an optional calendar id,
 * such as {@code 2026[NRF]}. When no calendar is given, the default
 * calendar from {@link RetailCalendarRegistry} is used.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * RetailYear year = RetailYear.of(RetailCalendar.NRF,2026);
 * RetailYear parsed = RetailYear.parse("2026[NRF]",RetailCalendar.NRF);
 *
 * // Usage
 * LocalDate first = year.getFirstDay();
 * LocalDate last = year.getLastDay();
 * RetailYear next = year.plus(1,RetailCalendar.NRF.years());
 * </pre>
 */
public final
class RetailYear
    extends AbstractCalendarPeriod<RetailYear,RetailCalendar>
{
    private static final Pattern PATTERN =
        Pattern.compile("^(?<year>[+-]?\\d{4,10})(?:\\[(?<id>[^\\[\\]]+)\\])?$");

    private final int year;

    private
    RetailYear(RetailCalendar calendar,int year)
    {
        super(calendar);
        this.year = year;
    }

    @Override
    public LocalDate
    getFirstDay()
    {
        return getCalendar().getYearStart(year);
    }

    @Override
    public boolean
    isSupported(TemporalField field)
    {
        RetailCalendar calendar = getCalendar();

        if (calendar.year().equals(field))
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

        if (calendar.years().equals(unit))
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

        return temporal.with(getCalendar().year(),year);
    }

    @Override
    public String
    format()
    {
        return formatYear(year);
    }

    public int getYear() { return year; }

    public int
    lengthInWeeks()
    {
        return getCalendar().lengthOfYearInWeeks(year);
    }

    public boolean
    is53WeekYear()
    {
        return lengthInWeeks() == 53;
    }

    public RetailYearQuarter
    atQuarter(int quarter)
        throws DateTimeException
    {
        return RetailYearQuarter.of(getCalendar(),year,quarter);
    }

    public RetailYearMonth
    atMonth(int month)
        throws DateTimeException
    {
        return RetailYearMonth.of(getCalendar(),year,month);
    }

    public RetailYearWeek
    atWeek(int week)
        throws DateTimeException
    {
        return RetailYearWeek.of(getCalendar(),year,week);
    }

    public LocalDate
    atDay(int dayOfYear)
        throws DateTimeException
    {
        Require.isTrue(
            dayOfYear >= 1 && dayOfYear <= lengthInWeeks() * 7,
            () -> new DateTimeException("Invalid day of year: " + dayOfYear));

        return getFirstDay().plusDays(dayOfYear - 1);
    }

    public RetailYear
    plusYears(long years)
        throws DateTimeException
    {
        return plus(years,getCalendar().years());
    }

    public RetailYear
    minusYears(long years)
        throws DateTimeException
    {
        return minus(years,getCalendar().years());
    }

    public static RetailYear
    of(int year)
        throws DateTimeException
    {
        return of(RetailCalendarRegistry.getDefault(),year);
    }

    public static RetailYear
    of(RetailCalendar calendar,int year)
        throws DateTimeException, NullPointerException
    {
        Require.notNull(calendar,"calendar is null");

        calendar.getYearStart(year);

        return new RetailYear(calendar,year);
    }

    public static RetailYear
    from(TemporalAccessor temporal)
        throws DateTimeException, NullPointerException
    {
        return from(RetailCalendarRegistry.getDefault(),temporal);
    }

    public static RetailYear
    from(RetailCalendar calendar,TemporalAccessor temporal)
        throws DateTimeException, NullPointerException
    {
        Require.notNull(calendar,"calendar is null");
        Require.notNull(temporal,"temporal is null");

        if (temporal instanceof RetailYear other && other.getCalendar().equals(calendar))
            return other;

        try
        {
            LocalDate date =
                temporal instanceof ICalendarPeriod period
                    ? period.getFirstDay()
                    : LocalDate.from(temporal);

            return of(calendar,calendar.getYear(date));
        }
        catch (DateTimeException e)
        {
            throw
                new DateTimeException(
                    "Unable to obtain RetailYear from TemporalAccessor: " +
                    temporal + " of type " + temporal.getClass().getName(),
                    e);
        }
    }

    public static RetailYear
    now()
    {
        return now(Clock.systemDefaultZone());
    }

    public static RetailYear
    now(ZoneId zone)
        throws NullPointerException
    {
        Require.notNull(zone,"zone is null");

        return now(Clock.system(zone));
    }

    public static RetailYear
    now(Clock clock)
        throws NullPointerException
    {
        return now(RetailCalendarRegistry.getDefault(),clock);
    }

    public static RetailYear
    now(RetailCalendar calendar,Clock clock)
        throws NullPointerException
    {
        Require.notNull(clock,"clock is null");

        return from(calendar,LocalDate.now(clock));
    }

    public static RetailYear
    parse(CharSequence text)
        throws DateTimeParseException, NullPointerException
    {
        return parse(text,null,RetailCalendarRegistry.getDefault());
    }

    public static RetailYear
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
        return getCalendar().years();
    }

    @Override
    protected long
    getIndex()
    {
        return year;
    }

    @Override
    protected RetailYear
    fromDate(LocalDate date)
    {
        return from(getCalendar(),date);
    }

    @Override
    protected RetailYear
    fromTemporal(TemporalAccessor temporal)
    {
        return from(getCalendar(),temporal);
    }

    private static RetailYear
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
            return of(calendar,Integer.parseInt(matcher.group("year")));
        }
        catch (DateTimeException | NumberFormatException e)
        {
            throw new DateTimeParseException(e.getMessage(),text,0,e);
        }
    }

    @JsonCreator
    private static RetailYear
    fromJson(
        @JsonProperty("calendar") String calendar,
        @JsonProperty("value")    String value)
    {
        Require.notNull(calendar,"calendar is null");

        return parse(value,RetailCalendarRegistry.getOrThrow(calendar));
    }
}

//////////////////////////////////////////////////////////////////////////////
