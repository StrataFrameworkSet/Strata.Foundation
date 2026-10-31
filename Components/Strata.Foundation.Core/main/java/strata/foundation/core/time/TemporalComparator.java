//////////////////////////////////////////////////////////////////////////////
// TemporalComparator.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import strata.foundation.core.utility.Require;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.util.Comparator;

public
class TemporalComparator<T extends Temporal>
    implements Comparator<T>
{
    @Override
    @SuppressWarnings("unchecked")
    public int
    compare(T a,T b)
        throws NullPointerException, IllegalArgumentException
    {
        if (
            a.isSupported(ChronoField.INSTANT_SECONDS) &&
            b.isSupported(ChronoField.INSTANT_SECONDS))
            return
                Instant
                    .from(a)
                    .compareTo(Instant.from(b));

        Require.isTrue(
            a instanceof Comparable && a.getClass().equals(b.getClass()),
            () ->
                new IllegalArgumentException(
                    "cannot compare " +
                    a.getClass().getName() +
                    " with " +
                    b.getClass().getName()));

        return ((Comparable<Object>)a).compareTo(b);
    }
}

//////////////////////////////////////////////////////////////////////////////
