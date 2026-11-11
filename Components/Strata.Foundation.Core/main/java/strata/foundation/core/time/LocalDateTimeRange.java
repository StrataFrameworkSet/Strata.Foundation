//////////////////////////////////////////////////////////////////////////////
// LocalDateTimeRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.LocalDateTime;

/**
 * <p>
 * {@link TemporalRange} of {@link java.time.LocalDateTime} values,
 * from a beginning through an end value.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * LocalDateTimeRange range =
 *     LocalDateTimeRange.of(
 *         LocalDateTime.of(2026,1,1,9,0),
 *         LocalDateTime.of(2026,1,1,17,30));
 *
 * // Usage
 * LocalDateTime beginning = range.getBeginning();
 * LocalDateTime end = range.getEnd();
 * Duration duration = range.getDuration();
 * </pre>
 */
public
class LocalDateTimeRange
    extends TemporalRange<LocalDateTime>
{
    public
    LocalDateTimeRange(LocalDateTime beginning,LocalDateTime end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static LocalDateTimeRange
    of(LocalDateTime beginning,LocalDateTime end)
        throws NullPointerException, IllegalArgumentException
    {
        return new LocalDateTimeRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
