//////////////////////////////////////////////////////////////////////////////
// TemporalRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import strata.foundation.core.value.Range;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.UnsupportedTemporalTypeException;

public
class TemporalRange<T extends Temporal>
    extends Range<T>
{
    public
    TemporalRange(T beginning,T end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end,new TemporalComparator<>());
    }

    public Period
    getPeriod()
        throws UnsupportedTemporalTypeException
    {
        T beginning = getBeginning();
        T end = getEnd();

        if (beginning instanceof ICalendarPeriod first && end instanceof ICalendarPeriod last)
            return Period.between(first.getFirstDay(),last.getFirstDay());

        if (beginning.isSupported(ChronoField.EPOCH_DAY))
            return
                Period.between(
                    LocalDate.from(beginning),
                    LocalDate.from(end));

        if (beginning.isSupported(ChronoField.INSTANT_SECONDS))
            return
                Period.between(
                    LocalDate.ofInstant(Instant.from(beginning),ZoneOffset.UTC),
                    LocalDate.ofInstant(Instant.from(end),ZoneOffset.UTC));

        if (beginning.isSupported(ChronoField.PROLEPTIC_MONTH))
        {
            long months =
                end.getLong(ChronoField.PROLEPTIC_MONTH) -
                beginning.getLong(ChronoField.PROLEPTIC_MONTH);

            return Period.of(Math.toIntExact(months / 12),(int)(months % 12),0);
        }

        if (beginning.isSupported(ChronoField.YEAR))
            return
                Period.ofYears(
                    Math.toIntExact(
                        end.getLong(ChronoField.YEAR) -
                        beginning.getLong(ChronoField.YEAR)));

        throw
            new UnsupportedTemporalTypeException(
                "getPeriod is not supported for " + beginning.getClass().getName());
    }

    public Duration
    getDuration()
        throws UnsupportedTemporalTypeException
    {
        T beginning = getBeginning();
        T end = getEnd();

        if (beginning instanceof ICalendarPeriod first && end instanceof ICalendarPeriod last)
            return
                Duration.ofDays(
                    ChronoUnit.DAYS.between(first.getFirstDay(),last.getFirstDay()));

        if (beginning.isSupported(ChronoUnit.SECONDS))
            return Duration.between(beginning,end);

        if (beginning.isSupported(ChronoUnit.DAYS))
            return Duration.ofDays(ChronoUnit.DAYS.between(beginning,end));

        if (beginning.isSupported(ChronoField.YEAR))
            return
                Duration.ofDays(
                    ChronoUnit.DAYS.between(toFirstDay(beginning),toFirstDay(end)));

        throw
            new UnsupportedTemporalTypeException(
                "getDuration is not supported for " + beginning.getClass().getName());
    }

    public static <T extends Temporal> TemporalRange<T>
    ofTemporal(T beginning,T end)
        throws NullPointerException,IllegalArgumentException
    {
        return new TemporalRange<>(beginning,end);
    }

    private LocalDate
    toFirstDay(T temporal)
    {
        int year = Math.toIntExact(temporal.getLong(ChronoField.YEAR));

        if (temporal.isSupported(ChronoField.MONTH_OF_YEAR))
            return
                LocalDate.of(
                    year,
                    (int)temporal.getLong(ChronoField.MONTH_OF_YEAR),
                    1);

        return LocalDate.of(year,1,1);
    }
}

//////////////////////////////////////////////////////////////////////////////
