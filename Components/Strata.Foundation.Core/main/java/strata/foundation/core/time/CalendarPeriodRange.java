//////////////////////////////////////////////////////////////////////////////
// CalendarPeriodRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import strata.foundation.core.utility.Validate;

import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalUnit;
import java.util.Iterator;
import java.util.stream.Stream;

public
class CalendarPeriodRange<P extends Temporal & ICalendarPeriod>
    extends    TemporalRange<P>
    implements ICalendarPeriod,
               Iterable<P>
{
    public
    CalendarPeriodRange(P beginning,P end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    @Override
    public LocalDate
    getFirstDay()
    {
        return getBeginning().getFirstDay();
    }

    @Override
    public LocalDate
    getLastDay()
    {
        return getEnd().getLastDay();
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
    public Iterator<P>
    iterator()
    {
        return stream().iterator();
    }

    public long
    lengthInPeriods()
    {
        return getBeginning().until(getEnd(),getUnit()) + 1;
    }

    @SuppressWarnings("unchecked")
    public Stream<P>
    stream()
    {
        TemporalUnit unit = getUnit();
        LocalDate    last = getEnd().getFirstDay();

        return
            Stream.iterate(
                getBeginning(),
                period -> !period.getFirstDay().isAfter(last),
                period -> (P)period.plus(1,unit));
    }

    public static <P extends Temporal & ICalendarPeriod> CalendarPeriodRange<P>
    ofCalendarPeriods(P beginning,P end)
        throws NullPointerException, IllegalArgumentException
    {
        return new CalendarPeriodRange<>(beginning,end);
    }

    private TemporalUnit
    getUnit()
    {
        return getBeginning().query(TemporalQueries.precision());
    }
}

//////////////////////////////////////////////////////////////////////////////
