//////////////////////////////////////////////////////////////////////////////
// OffsetDateTimeRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.OffsetDateTime;

/**
 * <p>
 * {@link TemporalRange} of {@link java.time.OffsetDateTime} values,
 * from a beginning through an end value.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * OffsetDateTimeRange range =
 *     OffsetDateTimeRange.of(
 *         OffsetDateTime.of(2026,1,1,9,0,0,0,ZoneOffset.UTC),
 *         OffsetDateTime.of(2026,1,1,17,30,0,0,ZoneOffset.UTC));
 *
 * // Usage
 * OffsetDateTime beginning = range.getBeginning();
 * OffsetDateTime end = range.getEnd();
 * Duration duration = range.getDuration();
 * </pre>
 */
public
class OffsetDateTimeRange
    extends TemporalRange<OffsetDateTime>
{
    public
    OffsetDateTimeRange(OffsetDateTime beginning,OffsetDateTime end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static OffsetDateTimeRange
    of(OffsetDateTime beginning,OffsetDateTime end)
        throws NullPointerException, IllegalArgumentException
    {
        return new OffsetDateTimeRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
