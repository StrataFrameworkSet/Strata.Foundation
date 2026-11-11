//////////////////////////////////////////////////////////////////////////////
// HijrahDateRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.chrono.HijrahDate;

/**
 * <p>
 * {@link TemporalRange} of {@link java.time.chrono.HijrahDate} values,
 * from a beginning through an end value.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * HijrahDateRange range =
 *     HijrahDateRange.of(
 *         HijrahDate.of(1447,1,1),
 *         HijrahDate.of(1447,6,1));
 *
 * // Usage
 * HijrahDate beginning = range.getBeginning();
 * HijrahDate end = range.getEnd();
 * Period period = range.getPeriod();
 * </pre>
 */
public
class HijrahDateRange
    extends TemporalRange<HijrahDate>
{
    public
    HijrahDateRange(HijrahDate beginning,HijrahDate end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static HijrahDateRange
    of(HijrahDate beginning,HijrahDate end)
        throws NullPointerException, IllegalArgumentException
    {
        return new HijrahDateRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
