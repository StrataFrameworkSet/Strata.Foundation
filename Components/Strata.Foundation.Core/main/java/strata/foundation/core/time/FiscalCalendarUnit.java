//////////////////////////////////////////////////////////////////////////////
// FiscalCalendarUnit.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import strata.foundation.core.utility.Validate;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Objects;

public final
class FiscalCalendarUnit
    implements TemporalUnit
{
    private final FiscalCalendar calendar;
    private final FiscalUnitType type;

    FiscalCalendarUnit(FiscalCalendar calendar,FiscalUnitType type)
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
        int       year1  = calendar.getWeekBasedYear(start);
        int       year2  = calendar.getWeekBasedYear(end);
        long      count  = (long)year2 - year1;
        long      offset =
            ChronoUnit.DAYS.between(calendar.getWeekStart(year2,1),end) -
            ChronoUnit.DAYS.between(calendar.getWeekStart(year1,1),start);

        if (count > 0 && offset < 0)
            return count - 1;

        if (count < 0 && offset > 0)
            return count + 1;

        return count;
    }

    @Override
    public <R extends Temporal> R
    addTo(R temporal,long amount)
        throws DateTimeException
    {
        if (amount == 0)
            return temporal;

        TemporalField field = calendar.weekBasedYear();

        return field.adjustInto(temporal,Math.addExact(field.getFrom(temporal),amount));
    }

    @Override
    public boolean
    equals(Object other)
    {
        return
            other instanceof FiscalCalendarUnit o &&
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

    public FiscalUnitType getType() { return type; }

    private LocalDate
    toDate(Temporal temporal)
        throws UnsupportedTemporalTypeException, NullPointerException
    {
        Validate.requireNonNull(temporal,"temporal is null");

        if (!isSupportedBy(temporal))
            throw new UnsupportedTemporalTypeException("Unsupported unit: " + this);

        return LocalDate.from(temporal);
    }
}

//////////////////////////////////////////////////////////////////////////////
