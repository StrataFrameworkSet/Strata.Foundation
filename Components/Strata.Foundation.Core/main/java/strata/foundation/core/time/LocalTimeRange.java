//////////////////////////////////////////////////////////////////////////////
// LocalTimeRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.LocalTime;

/**
 * <p>
 * {@link TemporalRange} of {@link java.time.LocalTime} values,
 * from a beginning through an end value.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * LocalTimeRange range =
 *     LocalTimeRange.of(
 *         LocalTime.of(9,0),
 *         LocalTime.of(17,30));
 *
 * // Usage
 * LocalTime beginning = range.getBeginning();
 * LocalTime end = range.getEnd();
 * Duration duration = range.getDuration();
 * </pre>
 */
public
class LocalTimeRange
    extends TemporalRange<LocalTime>
{
    public
    LocalTimeRange(LocalTime beginning,LocalTime end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static LocalTimeRange
    of(LocalTime beginning,LocalTime end)
        throws NullPointerException, IllegalArgumentException
    {
        return new LocalTimeRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
