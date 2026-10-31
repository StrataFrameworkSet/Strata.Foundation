//////////////////////////////////////////////////////////////////////////////
// RetailCalendarField.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import strata.foundation.core.utility.Require;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.Objects;

public final
class RetailCalendarField
    implements TemporalField
{
    private final RetailCalendar  calendar;
    private final RetailFieldType type;

    RetailCalendarField(RetailCalendar calendar,RetailFieldType type)
    {
        this.calendar = calendar;
        this.type     = type;
    }

    @Override
    public TemporalUnit
    getBaseUnit()
    {
        return
            switch (type)
            {
                case YEAR -> calendar.years();
                case QUARTER_OF_YEAR -> calendar.quarters();
                case MONTH_OF_QUARTER, MONTH_OF_YEAR -> calendar.months();
                case WEEK_OF_YEAR, WEEK_OF_QUARTER, WEEK_OF_MONTH -> ChronoUnit.WEEKS;
                case DAY_OF_YEAR -> ChronoUnit.DAYS;
            };
    }

    @Override
    public TemporalUnit
    getRangeUnit()
    {
        return
            switch (type)
            {
                case YEAR -> ChronoUnit.FOREVER;
                case QUARTER_OF_YEAR, MONTH_OF_YEAR, WEEK_OF_YEAR, DAY_OF_YEAR -> calendar.years();
                case MONTH_OF_QUARTER, WEEK_OF_QUARTER -> calendar.quarters();
                case WEEK_OF_MONTH -> calendar.months();
            };
    }

    @Override
    public ValueRange
    range()
    {
        return
            switch (type)
            {
                case YEAR -> RetailCalendar.YEAR_RANGE;
                case QUARTER_OF_YEAR -> ValueRange.of(1,4);
                case MONTH_OF_QUARTER -> ValueRange.of(1,3);
                case MONTH_OF_YEAR -> ValueRange.of(1,12);
                case WEEK_OF_YEAR -> ValueRange.of(1,52,53);
                case WEEK_OF_QUARTER -> ValueRange.of(1,13,14);
                case WEEK_OF_MONTH ->
                    ValueRange.of(
                        1,
                        calendar.getPattern().getMinimumWeeksInMonth(),
                        calendar.getPattern().getMaximumWeeksInMonth());
                case DAY_OF_YEAR -> ValueRange.of(1,364,371);
            };
    }

    @Override
    public boolean
    isDateBased()
    {
        return true;
    }

    @Override
    public boolean
    isTimeBased()
    {
        return false;
    }

    @Override
    public boolean
    isSupportedBy(TemporalAccessor temporal)
    {
        return temporal.isSupported(ChronoField.EPOCH_DAY);
    }

    @Override
    public ValueRange
    rangeRefinedBy(TemporalAccessor temporal)
        throws UnsupportedTemporalTypeException
    {
        LocalDate date = toDate(temporal);
        int       year = calendar.getYear(date);

        return
            switch (type)
            {
                case WEEK_OF_YEAR -> ValueRange.of(1,calendar.lengthOfYearInWeeks(year));
                case DAY_OF_YEAR -> ValueRange.of(1,calendar.lengthOfYearInWeeks(year) * 7L);
                case WEEK_OF_QUARTER ->
                    ValueRange.of(
                        1,
                        calendar.lengthOfQuarterInWeeks(year,calendar.getQuarter(date)));
                case WEEK_OF_MONTH ->
                    ValueRange.of(
                        1,
                        calendar.lengthOfMonthInWeeks(year,calendar.getMonth(date)));
                default -> range();
            };
    }

    @Override
    public long
    getFrom(TemporalAccessor temporal)
        throws UnsupportedTemporalTypeException
    {
        return getValue(toDate(temporal));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends Temporal> R
    adjustInto(R temporal,long newValue)
        throws DateTimeException
    {
        LocalDate date    = toDate(temporal);
        long      current = getValue(date);
        int       value   = rangeRefinedBy(temporal).checkValidIntValue(newValue,this);

        if (value == current)
            return temporal;

        LocalDate target =
            switch (type)
            {
                case YEAR -> calendar.shiftYears(date,value - current);
                case QUARTER_OF_YEAR -> calendar.shiftQuarters(date,value - current);
                case MONTH_OF_QUARTER, MONTH_OF_YEAR -> calendar.shiftMonths(date,value - current);
                case WEEK_OF_YEAR, WEEK_OF_QUARTER, WEEK_OF_MONTH -> date.plusWeeks(value - current);
                case DAY_OF_YEAR -> date.plusDays(value - current);
            };

        return (R)temporal.plus(ChronoUnit.DAYS.between(date,target),ChronoUnit.DAYS);
    }

    @Override
    public boolean
    equals(Object other)
    {
        return
            other instanceof RetailCalendarField o &&
            calendar.equals(o.calendar) &&
            type == o.type;
    }

    @Override
    public int
    hashCode()
    {
        return Objects.hash(calendar,type);
    }

    @Override
    public String
    toString()
    {
        return type.getDisplayName() + "[" + calendar.getId() + "]";
    }

    public RetailCalendar getCalendar() { return calendar; }

    public RetailFieldType getType() { return type; }

    private LocalDate
    toDate(TemporalAccessor temporal)
        throws UnsupportedTemporalTypeException, NullPointerException
    {
        Require.notNull(temporal,"temporal is null");

        if (!isSupportedBy(temporal))
            throw new UnsupportedTemporalTypeException("Unsupported field: " + this);

        return LocalDate.from(temporal);
    }

    private long
    getValue(LocalDate date)
    {
        int year = calendar.getYear(date);

        return
            switch (type)
            {
                case YEAR -> year;
                case QUARTER_OF_YEAR -> calendar.getQuarter(date);
                case MONTH_OF_QUARTER -> (calendar.getMonth(date) - 1) % 3 + 1;
                case MONTH_OF_YEAR -> calendar.getMonth(date);
                case WEEK_OF_YEAR -> weeksSince(calendar.getYearStart(year),date) + 1;
                case WEEK_OF_QUARTER ->
                    weeksSince(calendar.getQuarterStart(year,calendar.getQuarter(date)),date) + 1;
                case WEEK_OF_MONTH ->
                    weeksSince(calendar.getMonthStart(year,calendar.getMonth(date)),date) + 1;
                case DAY_OF_YEAR -> ChronoUnit.DAYS.between(calendar.getYearStart(year),date) + 1;
            };
    }

    private long
    weeksSince(LocalDate start,LocalDate date)
    {
        return ChronoUnit.DAYS.between(start,date) / 7;
    }
}

//////////////////////////////////////////////////////////////////////////////
