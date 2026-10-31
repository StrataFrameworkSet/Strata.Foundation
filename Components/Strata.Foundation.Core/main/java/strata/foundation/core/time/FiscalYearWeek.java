//////////////////////////////////////////////////////////////////////////////
// FiscalYearWeek.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import strata.foundation.core.utility.Validate;

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

public final
class FiscalYearWeek
    extends AbstractCalendarPeriod<FiscalYearWeek,FiscalCalendar>
{
    private static final Pattern PATTERN =
        Pattern.compile("^(?<year>[+-]?\\d{4,10})-W(?<week>\\d{2})(?:\\[(?<id>[^\\[\\]]+)\\])?$");

    private final int year;
    private final int week;

    private
    FiscalYearWeek(FiscalCalendar calendar,int year,int week)
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
        FiscalCalendar calendar = getCalendar();

        if (
            calendar.weekBasedYear().equals(field) ||
            calendar.weekOfWeekBasedYear().equals(field))
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
            unit == ChronoUnit.WEEKS ||
            getCalendar().weekBasedYears().equals(unit))
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
                .with(getCalendar().weekBasedYear(),year)
                .with(getCalendar().weekOfWeekBasedYear(),week);
    }

    @Override
    public String
    format()
    {
        return formatYear(year) + "-W" + String.format("%02d",week);
    }

    public int getYear() { return year; }

    public int getWeek() { return week; }

    public int
    lengthOfYear()
    {
        return getCalendar().lengthOfWeekBasedYearInWeeks(year);
    }

    public boolean
    is53WeekYear()
    {
        return lengthOfYear() == 53;
    }

    public LocalDate
    atDay(DayOfWeek day)
        throws NullPointerException
    {
        Validate.requireNonNull(day,"day is null");

        return getFirstDay().with(TemporalAdjusters.nextOrSame(day));
    }

    public FiscalYearWeek
    plusWeeks(long weeks)
        throws DateTimeException
    {
        return plus(weeks,ChronoUnit.WEEKS);
    }

    public FiscalYearWeek
    minusWeeks(long weeks)
        throws DateTimeException
    {
        return minus(weeks,ChronoUnit.WEEKS);
    }

    public FiscalYearWeek
    plusYears(long years)
        throws DateTimeException
    {
        return plus(years,getCalendar().weekBasedYears());
    }

    public FiscalYearWeek
    minusYears(long years)
        throws DateTimeException
    {
        return minus(years,getCalendar().weekBasedYears());
    }

    public FiscalYearWeek
    withYear(int year)
        throws DateTimeException
    {
        return with(getCalendar().weekBasedYear(),year);
    }

    public FiscalYearWeek
    withWeek(int week)
        throws DateTimeException
    {
        return with(getCalendar().weekOfWeekBasedYear(),week);
    }

    public static FiscalYearWeek
    of(int year,int week)
        throws DateTimeException
    {
        return of(FiscalCalendarRegistry.getDefault(),year,week);
    }

    public static FiscalYearWeek
    of(FiscalCalendar calendar,int year,int week)
        throws DateTimeException, NullPointerException
    {
        Validate.requireNonNull(calendar,"calendar is null");

        calendar.getWeekStart(year,week);

        return new FiscalYearWeek(calendar,year,week);
    }

    public static FiscalYearWeek
    from(TemporalAccessor temporal)
        throws DateTimeException, NullPointerException
    {
        return from(FiscalCalendarRegistry.getDefault(),temporal);
    }

    public static FiscalYearWeek
    from(FiscalCalendar calendar,TemporalAccessor temporal)
        throws DateTimeException, NullPointerException
    {
        Validate.requireNonNull(calendar,"calendar is null");
        Validate.requireNonNull(temporal,"temporal is null");

        if (temporal instanceof FiscalYearWeek other && other.getCalendar().equals(calendar))
            return other;

        try
        {
            LocalDate date = toFirstDate(temporal);

            return
                of(
                    calendar,
                    calendar.getWeekBasedYear(date),
                    date.get(calendar.weekOfWeekBasedYear()));
        }
        catch (DateTimeException e)
        {
            throw
                new DateTimeException(
                    "Unable to obtain FiscalYearWeek from TemporalAccessor: " +
                    temporal + " of type " + temporal.getClass().getName(),
                    e);
        }
    }

    public static FiscalYearWeek
    now()
    {
        return now(Clock.systemDefaultZone());
    }

    public static FiscalYearWeek
    now(ZoneId zone)
        throws NullPointerException
    {
        Validate.requireNonNull(zone,"zone is null");

        return now(Clock.system(zone));
    }

    public static FiscalYearWeek
    now(Clock clock)
        throws NullPointerException
    {
        return now(FiscalCalendarRegistry.getDefault(),clock);
    }

    public static FiscalYearWeek
    now(FiscalCalendar calendar,Clock clock)
        throws NullPointerException
    {
        Validate.requireNonNull(clock,"clock is null");

        return from(calendar,LocalDate.now(clock));
    }

    public static FiscalYearWeek
    parse(CharSequence text)
        throws DateTimeParseException, NullPointerException
    {
        return parse(text,null,FiscalCalendarRegistry.getDefault());
    }

    public static FiscalYearWeek
    parse(CharSequence text,FiscalCalendar calendar)
        throws DateTimeParseException, NullPointerException
    {
        Validate.requireNonNull(calendar,"calendar is null");

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
    protected FiscalYearWeek
    fromDate(LocalDate date)
    {
        return from(getCalendar(),date);
    }

    @Override
    protected FiscalYearWeek
    fromTemporal(TemporalAccessor temporal)
    {
        return from(getCalendar(),temporal);
    }

    private static FiscalYearWeek
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
                    Integer.parseInt(matcher.group("week")));
        }
        catch (DateTimeException | NumberFormatException e)
        {
            throw new DateTimeParseException(e.getMessage(),text,0,e);
        }
    }

    @JsonCreator
    private static FiscalYearWeek
    fromJson(
        @JsonProperty("calendar") String calendar,
        @JsonProperty("value")    String value)
    {
        Validate.requireNonNull(calendar,"calendar is null");

        return parse(value,FiscalCalendarRegistry.getOrThrow(calendar));
    }
}

//////////////////////////////////////////////////////////////////////////////
