//////////////////////////////////////////////////////////////////////////////
// YearWeek.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import strata.foundation.core.utility.Require;

import java.io.Serializable;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;

/**
 * <p>
 * Immutable ISO-8601 week of a week-based year, such as {@code 2026-W01}.
 * Weeks start on Monday and week 1 is the first week containing at least
 * four days of the year, so a week-based year has 52 or 53 weeks.
 * Implements {@link java.time.temporal.Temporal},
 * {@link java.time.temporal.TemporalAdjuster}, and {@link ICalendarPeriod}.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * YearWeek week = YearWeek.of(2026,1);
 * YearWeek current = YearWeek.now();
 * YearWeek parsed = YearWeek.parse("2026-W01");
 * YearWeek fromDate = YearWeek.from(LocalDate.of(2026,1,1));
 *
 * // Usage
 * LocalDate first = week.getFirstDay();
 * LocalDate friday = week.atDay(DayOfWeek.FRIDAY);
 * boolean longYear = week.is53WeekYear();
 * YearWeek next = week.plus(1,ChronoUnit.WEEKS);
 * </pre>
 */
public final
class YearWeek
    implements Temporal,
               TemporalAdjuster,
               Comparable<YearWeek>,
               Serializable,
               ICalendarPeriod
{
    private static final DateTimeFormatter FORMATTER =
        new DateTimeFormatterBuilder()
            .appendValue(IsoFields.WEEK_BASED_YEAR,4,10,SignStyle.EXCEEDS_PAD)
            .appendLiteral("-W")
            .appendValue(IsoFields.WEEK_OF_WEEK_BASED_YEAR,2)
            .toFormatter();

    private final int year;
    private final int week;

    private
    YearWeek(int year,int week)
    {
        this.year = year;
        this.week = week;
    }

    @Override
    public LocalDate
    getFirstDay()
    {
        return
            LocalDate
                .of(year,1,4)
                .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR,week)
                .with(DayOfWeek.MONDAY);
    }

    @Override
    public LocalDate
    getLastDay()
    {
        return getFirstDay().plusDays(6);
    }

    @Override
    public boolean
    contains(LocalDate date)
        throws NullPointerException
    {
        Require.notNull(date,"date is null");

        return equals(from(date));
    }

    @Override
    public boolean
    isSupported(TemporalField field)
    {
        if (field == IsoFields.WEEK_BASED_YEAR || field == IsoFields.WEEK_OF_WEEK_BASED_YEAR)
            return true;

        if (field instanceof ChronoField)
            return false;

        return field != null && field.isSupportedBy(this);
    }

    @Override
    public boolean
    isSupported(TemporalUnit unit)
    {
        if (unit == IsoFields.WEEK_BASED_YEARS)
            return true;

        if (unit instanceof ChronoUnit)
            return
                unit == ChronoUnit.WEEKS ||
                unit == ChronoUnit.DECADES ||
                unit == ChronoUnit.CENTURIES ||
                unit == ChronoUnit.MILLENNIA;

        return unit != null && unit.isSupportedBy(this);
    }

    @Override
    public ValueRange
    range(TemporalField field)
        throws UnsupportedTemporalTypeException
    {
        if (field == IsoFields.WEEK_BASED_YEAR)
            return IsoFields.WEEK_BASED_YEAR.range();

        if (field == IsoFields.WEEK_OF_WEEK_BASED_YEAR)
            return ValueRange.of(1,lengthOfYear());

        if (field instanceof ChronoField)
            throw new UnsupportedTemporalTypeException("Unsupported field: " + field);

        return Temporal.super.range(field);
    }

    @Override
    public long
    getLong(TemporalField field)
        throws UnsupportedTemporalTypeException
    {
        if (field == IsoFields.WEEK_BASED_YEAR)
            return year;

        if (field == IsoFields.WEEK_OF_WEEK_BASED_YEAR)
            return week;

        if (field instanceof ChronoField)
            throw new UnsupportedTemporalTypeException("Unsupported field: " + field);

        return field.getFrom(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> R
    query(TemporalQuery<R> query)
    {
        if (query == TemporalQueries.chronology())
            return (R)IsoChronology.INSTANCE;

        if (query == TemporalQueries.precision())
            return (R)ChronoUnit.WEEKS;

        return Temporal.super.query(query);
    }

    @Override
    public long
    until(Temporal endExclusive,TemporalUnit unit)
        throws DateTimeException, UnsupportedTemporalTypeException, NullPointerException
    {
        Require.notNull(unit,"unit is null");

        YearWeek end = from(endExclusive);

        if (unit == ChronoUnit.WEEKS)
            return ChronoUnit.WEEKS.between(getFirstDay(),end.getFirstDay());

        if (unit == IsoFields.WEEK_BASED_YEARS)
            return yearsUntil(end);

        if (unit == ChronoUnit.DECADES)
            return yearsUntil(end) / 10;

        if (unit == ChronoUnit.CENTURIES)
            return yearsUntil(end) / 100;

        if (unit == ChronoUnit.MILLENNIA)
            return yearsUntil(end) / 1000;

        if (unit instanceof ChronoUnit)
            throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit);

        return unit.between(this,end);
    }

    @Override
    public YearWeek
    with(TemporalField field,long newValue)
        throws DateTimeException, UnsupportedTemporalTypeException
    {
        if (field == IsoFields.WEEK_BASED_YEAR)
            return withYear(IsoFields.WEEK_BASED_YEAR.range().checkValidIntValue(newValue,field));

        if (field == IsoFields.WEEK_OF_WEEK_BASED_YEAR)
            return withWeek(range(field).checkValidIntValue(newValue,field));

        if (field instanceof ChronoField)
            throw new UnsupportedTemporalTypeException("Unsupported field: " + field);

        return field.adjustInto(this,newValue);
    }

    @Override
    public YearWeek
    with(TemporalAdjuster adjuster)
        throws DateTimeException
    {
        return (YearWeek)adjuster.adjustInto(this);
    }

    @Override
    public YearWeek
    plus(long amountToAdd,TemporalUnit unit)
        throws DateTimeException, UnsupportedTemporalTypeException
    {
        if (unit == ChronoUnit.WEEKS)
            return plusWeeks(amountToAdd);

        if (unit == IsoFields.WEEK_BASED_YEARS)
            return plusYears(amountToAdd);

        if (unit == ChronoUnit.DECADES)
            return plusYears(Math.multiplyExact(amountToAdd,10));

        if (unit == ChronoUnit.CENTURIES)
            return plusYears(Math.multiplyExact(amountToAdd,100));

        if (unit == ChronoUnit.MILLENNIA)
            return plusYears(Math.multiplyExact(amountToAdd,1000));

        if (unit instanceof ChronoUnit)
            throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit);

        return unit.addTo(this,amountToAdd);
    }

    @Override
    public YearWeek
    plus(TemporalAmount amountToAdd)
        throws DateTimeException
    {
        return (YearWeek)amountToAdd.addTo(this);
    }

    @Override
    public YearWeek
    minus(long amountToSubtract,TemporalUnit unit)
        throws DateTimeException, UnsupportedTemporalTypeException
    {
        if (amountToSubtract == Long.MIN_VALUE)
            return plus(Long.MAX_VALUE,unit).plus(1,unit);

        return plus(-amountToSubtract,unit);
    }

    @Override
    public YearWeek
    minus(TemporalAmount amountToSubtract)
        throws DateTimeException
    {
        return (YearWeek)amountToSubtract.subtractFrom(this);
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
                .with(IsoFields.WEEK_BASED_YEAR,year)
                .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR,week);
    }

    @Override
    public int
    compareTo(YearWeek other)
    {
        int result = Integer.compare(year,other.year);

        if (result != 0)
            return result;

        return Integer.compare(week,other.week);
    }

    @Override
    public boolean
    equals(Object other)
    {
        return other instanceof YearWeek o && year == o.year && week == o.week;
    }

    @Override
    public int
    hashCode()
    {
        return 31 * year + week;
    }

    @Override
    @JsonProperty("value")
    public String
    toString()
    {
        return format();
    }

    public int getYear() { return year; }

    public int getWeek() { return week; }

    public boolean
    is53WeekYear()
    {
        return lengthOfYear() == 53;
    }

    public int
    lengthOfYear()
    {
        return weeksInYear(year);
    }

    public boolean
    isBefore(YearWeek other)
        throws NullPointerException
    {
        Require.notNull(other,"other is null");

        return compareTo(other) < 0;
    }

    public boolean
    isAfter(YearWeek other)
        throws NullPointerException
    {
        Require.notNull(other,"other is null");

        return compareTo(other) > 0;
    }

    public LocalDate
    atDay(DayOfWeek day)
        throws NullPointerException
    {
        Require.notNull(day,"day is null");

        return getFirstDay().plusDays(day.getValue() - 1);
    }

    public String
    format()
    {
        return FORMATTER.format(this);
    }

    public YearWeek
    plusWeeks(long weeks)
        throws DateTimeException
    {
        if (weeks == 0)
            return this;

        return from(getFirstDay().plusWeeks(weeks));
    }

    public YearWeek
    minusWeeks(long weeks)
        throws DateTimeException
    {
        if (weeks == Long.MIN_VALUE)
            return plusWeeks(Long.MAX_VALUE).plusWeeks(1);

        return plusWeeks(-weeks);
    }

    public YearWeek
    plusYears(long years)
        throws DateTimeException
    {
        if (years == 0)
            return this;

        return
            withYear(
                IsoFields.WEEK_BASED_YEAR
                    .range()
                    .checkValidIntValue(Math.addExact(year,years),IsoFields.WEEK_BASED_YEAR));
    }

    public YearWeek
    minusYears(long years)
        throws DateTimeException
    {
        if (years == Long.MIN_VALUE)
            return plusYears(Long.MAX_VALUE).plusYears(1);

        return plusYears(-years);
    }

    public YearWeek
    withYear(int year)
        throws DateTimeException
    {
        IsoFields.WEEK_BASED_YEAR.range().checkValidIntValue(year,IsoFields.WEEK_BASED_YEAR);

        return of(year,Math.min(week,weeksInYear(year)));
    }

    public YearWeek
    withWeek(int week)
        throws DateTimeException
    {
        return of(year,week);
    }

    public static YearWeek
    of(int year,int week)
        throws DateTimeException
    {
        IsoFields.WEEK_BASED_YEAR.range().checkValidIntValue(year,IsoFields.WEEK_BASED_YEAR);
        ValueRange
            .of(1,weeksInYear(year))
            .checkValidIntValue(week,IsoFields.WEEK_OF_WEEK_BASED_YEAR);

        return new YearWeek(year,week);
    }

    public static YearWeek
    from(TemporalAccessor temporal)
        throws DateTimeException, NullPointerException
    {
        Require.notNull(temporal,"temporal is null");

        if (temporal instanceof YearWeek yearWeek)
            return yearWeek;

        try
        {
            LocalDate date = LocalDate.from(temporal);

            return
                of(
                    date.get(IsoFields.WEEK_BASED_YEAR),
                    date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
        }
        catch (DateTimeException e)
        {
            throw
                new DateTimeException(
                    "Unable to obtain YearWeek from TemporalAccessor: " +
                    temporal + " of type " + temporal.getClass().getName(),
                    e);
        }
    }

    public static YearWeek
    now()
    {
        return now(Clock.systemDefaultZone());
    }

    public static YearWeek
    now(ZoneId zone)
        throws NullPointerException
    {
        Require.notNull(zone,"zone is null");

        return now(Clock.system(zone));
    }

    public static YearWeek
    now(Clock clock)
        throws NullPointerException
    {
        Require.notNull(clock,"clock is null");

        return from(LocalDate.now(clock));
    }

    public static YearWeek
    parse(CharSequence text)
        throws NullPointerException
    {
        Require.notNull(text,"text is null");

        return
            FORMATTER.parse(
                text,
                parsed ->
                    of(
                        Math.toIntExact(parsed.getLong(IsoFields.WEEK_BASED_YEAR)),
                        Math.toIntExact(parsed.getLong(IsoFields.WEEK_OF_WEEK_BASED_YEAR))));
    }

    @JsonCreator
    private static YearWeek
    fromValue(@JsonProperty("value") String value)
    {
        return parse(value);
    }

    private long
    yearsUntil(YearWeek end)
    {
        long years = (long)end.year - year;

        if (years > 0 && end.week < week)
            return years - 1;

        if (years < 0 && end.week > week)
            return years + 1;

        return years;
    }

    private static int
    weeksInYear(int year)
    {
        return
            (int)IsoFields.WEEK_OF_WEEK_BASED_YEAR
                .rangeRefinedBy(LocalDate.of(year,6,1))
                .getMaximum();
    }
}

//////////////////////////////////////////////////////////////////////////////
