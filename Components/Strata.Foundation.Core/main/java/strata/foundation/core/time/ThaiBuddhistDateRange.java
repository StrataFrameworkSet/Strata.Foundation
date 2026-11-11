//////////////////////////////////////////////////////////////////////////////
// ThaiBuddhistDateRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.chrono.ThaiBuddhistDate;

/**
 * <p>
 * {@link TemporalRange} of {@link java.time.chrono.ThaiBuddhistDate} values,
 * from a beginning through an end value.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * ThaiBuddhistDateRange range =
 *     ThaiBuddhistDateRange.of(
 *         ThaiBuddhistDate.of(2569,1,1),
 *         ThaiBuddhistDate.of(2569,3,31));
 *
 * // Usage
 * ThaiBuddhistDate beginning = range.getBeginning();
 * ThaiBuddhistDate end = range.getEnd();
 * Period period = range.getPeriod();
 * </pre>
 */
public
class ThaiBuddhistDateRange
    extends TemporalRange<ThaiBuddhistDate>
{
    public
    ThaiBuddhistDateRange(ThaiBuddhistDate beginning,ThaiBuddhistDate end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static ThaiBuddhistDateRange
    of(ThaiBuddhistDate beginning,ThaiBuddhistDate end)
        throws NullPointerException, IllegalArgumentException
    {
        return new ThaiBuddhistDateRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
