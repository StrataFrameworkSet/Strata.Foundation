//////////////////////////////////////////////////////////////////////////////
// ZonedDateTimeRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.ZonedDateTime;

/**
 * <p>
 * {@link TemporalRange} of {@link java.time.ZonedDateTime} values,
 * from a beginning through an end value.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * ZonedDateTimeRange range =
 *     ZonedDateTimeRange.of(
 *         ZonedDateTime.of(2026,1,1,9,0,0,0,ZoneId.of("America/Los_Angeles")),
 *         ZonedDateTime.of(2026,1,1,17,30,0,0,ZoneId.of("America/Los_Angeles")));
 *
 * // Usage
 * ZonedDateTime beginning = range.getBeginning();
 * ZonedDateTime end = range.getEnd();
 * Duration duration = range.getDuration();
 * </pre>
 */
public
class ZonedDateTimeRange
    extends TemporalRange<ZonedDateTime>
{
    public
    ZonedDateTimeRange(ZonedDateTime beginning,ZonedDateTime end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static ZonedDateTimeRange
    of(ZonedDateTime beginning,ZonedDateTime end)
        throws NullPointerException, IllegalArgumentException
    {
        return new ZonedDateTimeRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
