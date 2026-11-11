//////////////////////////////////////////////////////////////////////////////
// TemporalComparator.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import strata.foundation.core.utility.Require;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.util.Comparator;

/**
 * <p>
 * {@link java.util.Comparator} for temporal values. Values that both
 * support {@link java.time.temporal.ChronoField#INSTANT_SECONDS} are
 * compared as instants; otherwise values must be of the same
 * {@link Comparable} type and are compared by their natural order.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * TemporalComparator&lt;LocalDate&gt; comparator = new TemporalComparator&lt;&gt;();
 *
 * // Usage
 * int result = comparator.compare(LocalDate.of(2026,1,1),LocalDate.of(2026,2,1));
 * </pre>
 *
 * @param <T> temporal type
 */
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
