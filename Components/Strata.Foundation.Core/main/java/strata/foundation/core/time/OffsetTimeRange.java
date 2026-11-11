//////////////////////////////////////////////////////////////////////////////
// OffsetTimeRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.OffsetTime;

/**
 * <p>
 * {@link TemporalRange} of {@link java.time.OffsetTime} values,
 * from a beginning through an end value.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * OffsetTimeRange range =
 *     OffsetTimeRange.of(
 *         OffsetTime.of(9,0,0,0,ZoneOffset.UTC),
 *         OffsetTime.of(17,30,0,0,ZoneOffset.UTC));
 *
 * // Usage
 * OffsetTime beginning = range.getBeginning();
 * OffsetTime end = range.getEnd();
 * Duration duration = range.getDuration();
 * </pre>
 */
public
class OffsetTimeRange
    extends TemporalRange<OffsetTime>
{
    public
    OffsetTimeRange(OffsetTime beginning,OffsetTime end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static OffsetTimeRange
    of(OffsetTime beginning,OffsetTime end)
        throws NullPointerException, IllegalArgumentException
    {
        return new OffsetTimeRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
