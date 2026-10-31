//////////////////////////////////////////////////////////////////////////////
// FiscalYearMonth.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import strata.foundation.core.utility.Require;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final
class FiscalYearMonth
    extends AbstractCalendarPeriod<FiscalYearMonth,FiscalCalendar>
{
    private static final Pattern PATTERN =
        Pattern.compile("^(?<year>[+-]?\\d{4,10})-M(?<month>\\d{2})(?:\\[(?<id>[^\\[\\]]+)\\])?$");

    private final int year;
    private final int month;

    private
    FiscalYearMonth(FiscalCalendar calendar,int year,int month)
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
        FiscalCalendar calendar = getCalendar();

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
        if (
            unit == ChronoUnit.MONTHS ||
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
    lengthInDays()
    {
        return getCalendar().lengthOfMonthInDays(year,month);
    }

    public FiscalYear
    getFiscalYear()
    {
        return FiscalYear.of(getCalendar(),year);
    }

    public FiscalYearQuarter
    getFiscalYearQuarter()
    {
        return FiscalYearQuarter.of(getCalendar(),year,(month - 1) / 3 + 1);
    }

    public LocalDate
    atDay(int dayOfMonth)
        throws DateTimeException
    {
        Require.isTrue(
            dayOfMonth >= 1 && dayOfMonth <= lengthInDays(),
            () -> new DateTimeException("Invalid day of month: " + dayOfMonth));

        return getFirstDay().plusDays(dayOfMonth - 1);
    }

    public YearMonth
    toYearMonth()
    {
        return YearMonth.from(getFirstDay());
    }

    public FiscalYearMonth
    plusMonths(long months)
        throws DateTimeException
    {
        return plus(months,ChronoUnit.MONTHS);
    }

    public FiscalYearMonth
    minusMonths(long months)
        throws DateTimeException
    {
        return minus(months,ChronoUnit.MONTHS);
    }

    public FiscalYearMonth
    plusYears(long years)
        throws DateTimeException
    {
        return plus(years,ChronoUnit.YEARS);
    }

    public FiscalYearMonth
    minusYears(long years)
        throws DateTimeException
    {
        return minus(years,ChronoUnit.YEARS);
    }

    public FiscalYearMonth
    withYear(int year)
        throws DateTimeException
    {
        return with(getCalendar().year(),year);
    }

    public FiscalYearMonth
    withMonth(int month)
        throws DateTimeException
    {
        return with(getCalendar().monthOfYear(),month);
    }

    public static FiscalYearMonth
    of(int year,int month)
        throws DateTimeException
    {
        return of(FiscalCalendarRegistry.getDefault(),year,month);
    }

    public static FiscalYearMonth
    of(FiscalCalendar calendar,int year,int month)
        throws DateTimeException, NullPointerException
    {
        Require.notNull(calendar,"calendar is null");

        calendar.getMonthStart(year,month);

        return new FiscalYearMonth(calendar,year,month);
    }

    public static FiscalYearMonth
    from(TemporalAccessor temporal)
        throws DateTimeException, NullPointerException
    {
        return from(FiscalCalendarRegistry.getDefault(),temporal);
    }

    public static FiscalYearMonth
    from(FiscalCalendar calendar,TemporalAccessor temporal)
        throws DateTimeException, NullPointerException
    {
        Require.notNull(calendar,"calendar is null");
        Require.notNull(temporal,"temporal is null");

        if (temporal instanceof FiscalYearMonth other && other.getCalendar().equals(calendar))
            return other;

        try
        {
            LocalDate date = toFirstDate(temporal);

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
                    "Unable to obtain FiscalYearMonth from TemporalAccessor: " +
                    temporal + " of type " + temporal.getClass().getName(),
                    e);
        }
    }

    public static FiscalYearMonth
    now()
    {
        return now(Clock.systemDefaultZone());
    }

    public static FiscalYearMonth
    now(ZoneId zone)
        throws NullPointerException
    {
        Require.notNull(zone,"zone is null");

        return now(Clock.system(zone));
    }

    public static FiscalYearMonth
    now(Clock clock)
        throws NullPointerException
    {
        return now(FiscalCalendarRegistry.getDefault(),clock);
    }

    public static FiscalYearMonth
    now(FiscalCalendar calendar,Clock clock)
        throws NullPointerException
    {
        Require.notNull(clock,"clock is null");

        return from(calendar,LocalDate.now(clock));
    }

    public static FiscalYearMonth
    parse(CharSequence text)
        throws DateTimeParseException, NullPointerException
    {
        return parse(text,null,FiscalCalendarRegistry.getDefault());
    }

    public static FiscalYearMonth
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
        return ChronoUnit.MONTHS;
    }

    @Override
    protected long
    getIndex()
    {
        return year * 12L + month - 1;
    }

    @Override
    protected FiscalYearMonth
    fromDate(LocalDate date)
    {
        return from(getCalendar(),date);
    }

    @Override
    protected FiscalYearMonth
    fromTemporal(TemporalAccessor temporal)
    {
        return from(getCalendar(),temporal);
    }

    private static FiscalYearMonth
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
                    Integer.parseInt(matcher.group("month")));
        }
        catch (DateTimeException | NumberFormatException e)
        {
            throw new DateTimeParseException(e.getMessage(),text,0,e);
        }
    }

    @JsonCreator
    private static FiscalYearMonth
    fromJson(
        @JsonProperty("calendar") String calendar,
        @JsonProperty("value")    String value)
    {
        Require.notNull(calendar,"calendar is null");

        return parse(value,FiscalCalendarRegistry.getOrThrow(calendar));
    }
}

//////////////////////////////////////////////////////////////////////////////
