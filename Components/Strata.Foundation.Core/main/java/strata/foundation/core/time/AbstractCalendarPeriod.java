//////////////////////////////////////////////////////////////////////////////
// AbstractCalendarPeriod.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import strata.foundation.core.utility.Validate;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
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
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@JsonAutoDetect(
    getterVisibility   = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public abstract
class AbstractCalendarPeriod<P extends AbstractCalendarPeriod<P,C>,C extends ICalendarDefinition>
    implements Temporal,
               TemporalAdjuster,
               Comparable<P>,
               Serializable,
               ICalendarPeriod
{
    private final C calendar;

    protected
    AbstractCalendarPeriod(C calendar)
    {
        this.calendar = calendar;
    }

    @Override
    public LocalDate
    getLastDay()
    {
        return plus(1,getBaseUnit()).getFirstDay().minusDays(1);
    }

    @Override
    public boolean
    contains(LocalDate date)
        throws NullPointerException
    {
        Validate.requireNonNull(date,"date is null");

        return !date.isBefore(getFirstDay()) && !date.isAfter(getLastDay());
    }

    @Override
    public ValueRange
    range(TemporalField field)
        throws UnsupportedTemporalTypeException
    {
        Validate.requireNonNull(field,"field is null");

        if (isSupported(field))
            return field.rangeRefinedBy(getFirstDay());

        if (field instanceof ChronoField)
            throw new UnsupportedTemporalTypeException("Unsupported field: " + field);

        return field.rangeRefinedBy(this);
    }

    @Override
    public long
    getLong(TemporalField field)
        throws UnsupportedTemporalTypeException
    {
        Validate.requireNonNull(field,"field is null");

        if (isSupported(field))
            return getFirstDay().getLong(field);

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
            return (R)getBaseUnit();

        return Temporal.super.query(query);
    }

    @Override
    public long
    until(Temporal endExclusive,TemporalUnit unit)
        throws DateTimeException, UnsupportedTemporalTypeException
    {
        Validate.requireNonNull(unit,"unit is null");

        P end = fromTemporal(endExclusive);

        if (isSupported(unit))
            return getFirstDay().until(end.getFirstDay(),unit);

        if (unit instanceof ChronoUnit)
            throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit);

        return unit.between(this,end);
    }

    @Override
    @SuppressWarnings("unchecked")
    public P
    with(TemporalField field,long newValue)
        throws DateTimeException, UnsupportedTemporalTypeException
    {
        Validate.requireNonNull(field,"field is null");

        if (isSupported(field))
            return fromDate(field.adjustInto(getFirstDay(),newValue));

        if (field instanceof ChronoField)
            throw new UnsupportedTemporalTypeException("Unsupported field: " + field);

        return field.adjustInto((P)this,newValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    public P
    with(TemporalAdjuster adjuster)
        throws DateTimeException
    {
        return (P)adjuster.adjustInto(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public P
    plus(long amountToAdd,TemporalUnit unit)
        throws DateTimeException, UnsupportedTemporalTypeException
    {
        Validate.requireNonNull(unit,"unit is null");

        if (isSupported(unit))
            return fromDate(getFirstDay().plus(amountToAdd,unit));

        if (unit instanceof ChronoUnit)
            throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit);

        return unit.addTo((P)this,amountToAdd);
    }

    @Override
    @SuppressWarnings("unchecked")
    public P
    plus(TemporalAmount amountToAdd)
        throws DateTimeException
    {
        return (P)amountToAdd.addTo(this);
    }

    @Override
    public P
    minus(long amountToSubtract,TemporalUnit unit)
        throws DateTimeException, UnsupportedTemporalTypeException
    {
        if (amountToSubtract == Long.MIN_VALUE)
            return plus(Long.MAX_VALUE,unit).plus(1,unit);

        return plus(-amountToSubtract,unit);
    }

    @Override
    @SuppressWarnings("unchecked")
    public P
    minus(TemporalAmount amountToSubtract)
        throws DateTimeException
    {
        return (P)amountToSubtract.subtractFrom(this);
    }

    @Override
    public int
    compareTo(P other)
        throws IllegalArgumentException
    {
        Validate.require(
            calendar.equals(other.getCalendar()),
            () ->
                new IllegalArgumentException(
                    "cannot compare values from different calendars: " +
                    calendar.getId() + " and " + other.getCalendar().getId()));

        return Long.compare(getIndex(),other.getIndex());
    }

    @Override
    public boolean
    equals(Object other)
    {
        if (this == other)
            return true;

        if (other == null || getClass() != other.getClass())
            return false;

        AbstractCalendarPeriod<?,?> period = (AbstractCalendarPeriod<?,?>)other;

        return calendar.equals(period.calendar) && getIndex() == period.getIndex();
    }

    @Override
    public int
    hashCode()
    {
        return 31 * calendar.hashCode() + Long.hashCode(getIndex());
    }

    @Override
    public String
    toString()
    {
        return format() + "[" + calendar.getId() + "]";
    }

    public C getCalendar() { return calendar; }

    public boolean
    isBefore(P other)
        throws NullPointerException, IllegalArgumentException
    {
        Validate.requireNonNull(other,"other is null");

        return compareTo(other) < 0;
    }

    public boolean
    isAfter(P other)
        throws NullPointerException, IllegalArgumentException
    {
        Validate.requireNonNull(other,"other is null");

        return compareTo(other) > 0;
    }

    public abstract String
    format();

    protected abstract TemporalUnit
    getBaseUnit();

    protected abstract long
    getIndex();

    protected abstract P
    fromDate(LocalDate date);

    protected abstract P
    fromTemporal(TemporalAccessor temporal);

    @JsonProperty("calendar")
    protected String
    getCalendarId()
    {
        return calendar.getId();
    }

    @JsonProperty("value")
    protected String
    getFormattedValue()
    {
        return format();
    }

    protected static Matcher
    matchText(Pattern pattern,CharSequence text)
        throws DateTimeParseException, NullPointerException
    {
        Validate.requireNonNull(text,"text is null");

        Matcher matcher = pattern.matcher(text);

        if (!matcher.matches())
            throw new DateTimeParseException("Text '" + text + "' could not be parsed",text,0);

        return matcher;
    }

    protected static <D extends ICalendarDefinition> D
    resolveCalendar(
        CharSequence                    text,
        String                          id,
        D                               expected,
        D                               defaultCalendar,
        Function<String,Optional<D>>    lookup)
        throws DateTimeParseException
    {
        if (expected != null)
        {
            if (id != null && !id.equals(expected.getId()))
                throw
                    new DateTimeParseException(
                        "Text '" + text + "' does not match calendar " + expected.getId(),
                        text,
                        0);

            return expected;
        }

        if (id == null)
            return defaultCalendar;

        return
            lookup
                .apply(id)
                .orElseThrow(
                    () ->
                        new DateTimeParseException(
                            "Text '" + text + "' refers to unknown calendar " + id,
                            text,
                            0));
    }

    protected static String
    formatYear(int year)
    {
        if (Math.abs(year) < 1000)
            return (year < 0 ? "-" : "") + String.format("%04d",Math.abs(year));

        if (year > 9999)
            return "+" + year;

        return Integer.toString(year);
    }

    protected static LocalDate
    toFirstDate(TemporalAccessor temporal)
        throws DateTimeException
    {
        if (temporal instanceof ICalendarPeriod period)
            return period.getFirstDay();

        if (temporal.isSupported(ChronoField.EPOCH_DAY))
            return LocalDate.from(temporal);

        if (temporal.isSupported(ChronoField.PROLEPTIC_MONTH))
            return YearMonth.from(temporal).atDay(1);

        if (temporal.isSupported(ChronoField.YEAR))
            return Year.from(temporal).atDay(1);

        return LocalDate.from(temporal);
    }
}

//////////////////////////////////////////////////////////////////////////////
