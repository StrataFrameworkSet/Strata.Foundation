//////////////////////////////////////////////////////////////////////////////
// RetailCalendarUnit.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import strata.foundation.core.utility.Validate;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Objects;

public final
class RetailCalendarUnit
    implements TemporalUnit
{
    private final RetailCalendar calendar;
    private final RetailUnitType type;

    RetailCalendarUnit(RetailCalendar calendar,RetailUnitType type)
    {
        this.calendar = calendar;
        this.type     = type;
    }

    @Override
    public Duration
    getDuration()
    {
        return type.getDuration();
    }

    @Override
    public boolean
    isDurationEstimated()
    {
        return true;
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
    isSupportedBy(Temporal temporal)
    {
        return temporal.isSupported(ChronoField.EPOCH_DAY);
    }

    @Override
    public long
    between(Temporal temporal1Inclusive,Temporal temporal2Exclusive)
        throws DateTimeException, UnsupportedTemporalTypeException
    {
        LocalDate start  = toDate(temporal1Inclusive);
        LocalDate end    = toDate(temporal2Exclusive);
        long      count  = getIndex(end) - getIndex(start);
        long      offset = getOffset(end) - getOffset(start);

        if (count > 0 && offset < 0)
            return count - 1;

        if (count < 0 && offset > 0)
            return count + 1;

        return count;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends Temporal> R
    addTo(R temporal,long amount)
        throws DateTimeException
    {
        if (amount == 0)
            return temporal;

        LocalDate date = toDate(temporal);
        LocalDate target =
            switch (type)
            {
                case MONTHS -> calendar.shiftMonths(date,amount);
                case QUARTERS -> calendar.shiftQuarters(date,amount);
                case YEARS -> calendar.shiftYears(date,amount);
            };

        return (R)temporal.plus(ChronoUnit.DAYS.between(date,target),ChronoUnit.DAYS);
    }

    @Override
    public boolean
    equals(Object other)
    {
        return
            other instanceof RetailCalendarUnit o &&
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

    public RetailUnitType getType() { return type; }

    private LocalDate
    toDate(Temporal temporal)
        throws UnsupportedTemporalTypeException, NullPointerException
    {
        Validate.requireNonNull(temporal,"temporal is null");

        if (!isSupportedBy(temporal))
            throw new UnsupportedTemporalTypeException("Unsupported unit: " + this);

        return LocalDate.from(temporal);
    }

    private long
    getIndex(LocalDate date)
    {
        int year = calendar.getYear(date);

        return
            switch (type)
            {
                case MONTHS -> year * 12L + calendar.getMonth(date) - 1;
                case QUARTERS -> year * 4L + calendar.getQuarter(date) - 1;
                case YEARS -> year;
            };
    }

    private long
    getOffset(LocalDate date)
    {
        int year = calendar.getYear(date);

        LocalDate start =
            switch (type)
            {
                case MONTHS -> calendar.getMonthStart(year,calendar.getMonth(date));
                case QUARTERS -> calendar.getQuarterStart(year,calendar.getQuarter(date));
                case YEARS -> calendar.getYearStart(year);
            };

        return ChronoUnit.DAYS.between(start,date);
    }
}

//////////////////////////////////////////////////////////////////////////////
