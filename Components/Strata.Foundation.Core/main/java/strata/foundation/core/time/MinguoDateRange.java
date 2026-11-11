//////////////////////////////////////////////////////////////////////////////
// MinguoDateRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.chrono.MinguoDate;

/**
 * <p>
 * {@link TemporalRange} of {@link java.time.chrono.MinguoDate} values,
 * from a beginning through an end value.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * MinguoDateRange range =
 *     MinguoDateRange.of(
 *         MinguoDate.of(115,1,1),
 *         MinguoDate.of(115,3,31));
 *
 * // Usage
 * MinguoDate beginning = range.getBeginning();
 * MinguoDate end = range.getEnd();
 * Period period = range.getPeriod();
 * </pre>
 */
public
class MinguoDateRange
    extends TemporalRange<MinguoDate>
{
    public
    MinguoDateRange(MinguoDate beginning,MinguoDate end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static MinguoDateRange
    of(MinguoDate beginning,MinguoDate end)
        throws NullPointerException, IllegalArgumentException
    {
        return new MinguoDateRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
