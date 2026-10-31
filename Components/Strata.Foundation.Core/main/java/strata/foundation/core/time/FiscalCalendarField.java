//////////////////////////////////////////////////////////////////////////////
// FiscalCalendarField.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import strata.foundation.core.utility.Require;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.Objects;

public final
class FiscalCalendarField
    implements TemporalField
{
    private final FiscalCalendar  calendar;
    private final FiscalFieldType type;

    FiscalCalendarField(FiscalCalendar calendar,FiscalFieldType type)
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
                case YEAR -> ChronoUnit.YEARS;
                case QUARTER_OF_YEAR -> IsoFields.QUARTER_YEARS;
                case MONTH_OF_QUARTER, MONTH_OF_YEAR -> ChronoUnit.MONTHS;
                case DAY_OF_YEAR -> ChronoUnit.DAYS;
                case WEEK_BASED_YEAR -> calendar.weekBasedYears();
                case WEEK_OF_WEEK_BASED_YEAR -> ChronoUnit.WEEKS;
            };
    }

    @Override
    public TemporalUnit
    getRangeUnit()
    {
        return
            switch (type)
            {
                case YEAR, WEEK_BASED_YEAR -> ChronoUnit.FOREVER;
                case QUARTER_OF_YEAR, MONTH_OF_YEAR, DAY_OF_YEAR -> ChronoUnit.YEARS;
                case MONTH_OF_QUARTER -> IsoFields.QUARTER_YEARS;
                case WEEK_OF_WEEK_BASED_YEAR -> calendar.weekBasedYears();
            };
    }

    @Override
    public ValueRange
    range()
    {
        return
            switch (type)
            {
                case YEAR, WEEK_BASED_YEAR -> FiscalCalendar.YEAR_RANGE;
                case QUARTER_OF_YEAR -> ValueRange.of(1,4);
                case MONTH_OF_QUARTER -> ValueRange.of(1,3);
                case MONTH_OF_YEAR -> ValueRange.of(1,12);
                case DAY_OF_YEAR -> ValueRange.of(1,365,366);
                case WEEK_OF_WEEK_BASED_YEAR -> ValueRange.of(1,52,53);
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

        return
            switch (type)
            {
                case DAY_OF_YEAR ->
                    ValueRange.of(1,calendar.lengthOfYearInDays(calendar.getYear(date)));
                case WEEK_OF_WEEK_BASED_YEAR ->
                    ValueRange.of(
                        1,
                        calendar.lengthOfWeekBasedYearInWeeks(calendar.getWeekBasedYear(date)));
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

        long delta = value - current;
        LocalDate target =
            switch (type)
            {
                case YEAR -> date.plusYears(delta);
                case QUARTER_OF_YEAR -> date.plusMonths(3 * delta);
                case MONTH_OF_QUARTER, MONTH_OF_YEAR -> date.plusMonths(delta);
                case DAY_OF_YEAR -> date.plusDays(delta);
                case WEEK_OF_WEEK_BASED_YEAR -> date.plusWeeks(delta);
                case WEEK_BASED_YEAR -> moveToWeekBasedYear(date,value);
            };

        return (R)temporal.plus(ChronoUnit.DAYS.between(date,target),ChronoUnit.DAYS);
    }

    @Override
    public boolean
    equals(Object other)
    {
        return
            other instanceof FiscalCalendarField o &&
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

    public FiscalCalendar getCalendar() { return calendar; }

    public FiscalFieldType getType() { return type; }

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
        int monthOfYear =
            Math.floorMod(date.getMonthValue() - calendar.getStartMonth().getValue(),12) + 1;

        return
            switch (type)
            {
                case YEAR -> calendar.getYear(date);
                case QUARTER_OF_YEAR -> (monthOfYear - 1) / 3 + 1;
                case MONTH_OF_QUARTER -> (monthOfYear - 1) % 3 + 1;
                case MONTH_OF_YEAR -> monthOfYear;
                case DAY_OF_YEAR ->
                    ChronoUnit.DAYS.between(
                        calendar.getYearStart(calendar.getYear(date)),
                        date) + 1;
                case WEEK_BASED_YEAR -> calendar.getWeekBasedYear(date);
                case WEEK_OF_WEEK_BASED_YEAR ->
                    ChronoUnit.DAYS.between(
                        calendar.getWeekStart(calendar.getWeekBasedYear(date),1),
                        date) / 7 + 1;
            };
    }

    private LocalDate
    moveToWeekBasedYear(LocalDate date,int weekBasedYear)
    {
        int  current = calendar.getWeekBasedYear(date);
        long offset  = ChronoUnit.DAYS.between(calendar.getWeekStart(current,1),date);
        int  week    =
            (int)Math.min(offset / 7 + 1,calendar.lengthOfWeekBasedYearInWeeks(weekBasedYear));

        return calendar.getWeekStart(weekBasedYear,week).plusDays(offset % 7);
    }
}

//////////////////////////////////////////////////////////////////////////////
