//////////////////////////////////////////////////////////////////////////////
// FiscalYear.java
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
 * Immutable year of a {@link FiscalCalendar}, labeled according to the
 * calendar's {@link CalendarYearLabel}. Formatted and parsed as the year
 * number with an optional calendar id, such as {@code 2026[US_FEDERAL]}.
 * When no calendar is given, the default calendar from
 * {@link FiscalCalendarRegistry} is used.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * FiscalYear year = FiscalYear.of(FiscalCalendar.US_FEDERAL,2026);
 * FiscalYear current = FiscalYear.now();
 * FiscalYear parsed = FiscalYear.parse("2026[US_FEDERAL]");
 * FiscalYear fromDate = FiscalYear.from(FiscalCalendar.US_FEDERAL,LocalDate.of(2025,10,15));
 *
 * // Usage
 * LocalDate first = year.getFirstDay();
 * FiscalYear next = year.plus(1,ChronoUnit.YEARS);
 * String text = year.format();
 * </pre>
 */
public final
class FiscalYear
    extends AbstractCalendarPeriod<FiscalYear,FiscalCalendar>
{
    private static final Pattern PATTERN =
        Pattern.compile("^(?<year>[+-]?\\d{4,10})(?:\\[(?<id>[^\\[\\]]+)\\])?$");

    private final int year;

    private
    FiscalYear(FiscalCalendar calendar,int year)
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
        if (getCalendar().year().equals(field))
            return true;

        if (field instanceof ChronoField)
            return false;

        return field != null && field.isSupportedBy(this);
    }

    @Override
    public boolean
    isSupported(TemporalUnit unit)
    {
        if (unit == ChronoUnit.YEARS)
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
    lengthInDays()
    {
        return getCalendar().lengthOfYearInDays(year);
    }

    public boolean
    isLeap()
    {
        return lengthInDays() == 366;
    }

    public FiscalYearQuarter
    atQuarter(int quarter)
        throws DateTimeException
    {
        return FiscalYearQuarter.of(getCalendar(),year,quarter);
    }

    public FiscalYearMonth
    atMonth(int month)
        throws DateTimeException
    {
        return FiscalYearMonth.of(getCalendar(),year,month);
    }

    public LocalDate
    atDay(int dayOfYear)
        throws DateTimeException
    {
        Require.isTrue(
            dayOfYear >= 1 && dayOfYear <= lengthInDays(),
            () -> new DateTimeException("Invalid day of year: " + dayOfYear));

        return getFirstDay().plusDays(dayOfYear - 1);
    }

    public FiscalYear
    plusYears(long years)
        throws DateTimeException
    {
        return plus(years,ChronoUnit.YEARS);
    }

    public FiscalYear
    minusYears(long years)
        throws DateTimeException
    {
        return minus(years,ChronoUnit.YEARS);
    }

    public static FiscalYear
    of(int year)
        throws DateTimeException
    {
        return of(FiscalCalendarRegistry.getDefault(),year);
    }

    public static FiscalYear
    of(FiscalCalendar calendar,int year)
        throws DateTimeException, NullPointerException
    {
        Require.notNull(calendar,"calendar is null");

        calendar.getYearStart(year);

        return new FiscalYear(calendar,year);
    }

    public static FiscalYear
    from(TemporalAccessor temporal)
        throws DateTimeException, NullPointerException
    {
        return from(FiscalCalendarRegistry.getDefault(),temporal);
    }

    public static FiscalYear
    from(FiscalCalendar calendar,TemporalAccessor temporal)
        throws DateTimeException, NullPointerException
    {
        Require.notNull(calendar,"calendar is null");
        Require.notNull(temporal,"temporal is null");

        if (temporal instanceof FiscalYear other && other.getCalendar().equals(calendar))
            return other;

        try
        {
            LocalDate date = toFirstDate(temporal);

            return of(calendar,calendar.getYear(date));
        }
        catch (DateTimeException e)
        {
            throw
                new DateTimeException(
                    "Unable to obtain FiscalYear from TemporalAccessor: " +
                    temporal + " of type " + temporal.getClass().getName(),
                    e);
        }
    }

    public static FiscalYear
    now()
    {
        return now(Clock.systemDefaultZone());
    }

    public static FiscalYear
    now(ZoneId zone)
        throws NullPointerException
    {
        Require.notNull(zone,"zone is null");

        return now(Clock.system(zone));
    }

    public static FiscalYear
    now(Clock clock)
        throws NullPointerException
    {
        return now(FiscalCalendarRegistry.getDefault(),clock);
    }

    public static FiscalYear
    now(FiscalCalendar calendar,Clock clock)
        throws NullPointerException
    {
        Require.notNull(clock,"clock is null");

        return from(calendar,LocalDate.now(clock));
    }

    public static FiscalYear
    parse(CharSequence text)
        throws DateTimeParseException, NullPointerException
    {
        return parse(text,null,FiscalCalendarRegistry.getDefault());
    }

    public static FiscalYear
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
        return ChronoUnit.YEARS;
    }

    @Override
    protected long
    getIndex()
    {
        return year;
    }

    @Override
    protected FiscalYear
    fromDate(LocalDate date)
    {
        return from(getCalendar(),date);
    }

    @Override
    protected FiscalYear
    fromTemporal(TemporalAccessor temporal)
    {
        return from(getCalendar(),temporal);
    }

    private static FiscalYear
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
                    Integer.parseInt(matcher.group("year")));
        }
        catch (DateTimeException | NumberFormatException e)
        {
            throw new DateTimeParseException(e.getMessage(),text,0,e);
        }
    }

    @JsonCreator
    private static FiscalYear
    fromJson(
        @JsonProperty("calendar") String calendar,
        @JsonProperty("value")    String value)
    {
        Require.notNull(calendar,"calendar is null");

        return parse(value,FiscalCalendarRegistry.getOrThrow(calendar));
    }
}

//////////////////////////////////////////////////////////////////////////////
