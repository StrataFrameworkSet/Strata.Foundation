//////////////////////////////////////////////////////////////////////////////
// InstantRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.Instant;

/**
 * <p>
 * {@link TemporalRange} of {@link java.time.Instant} values,
 * from a beginning through an end value.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * InstantRange range =
 *     InstantRange.of(
 *         Instant.parse("2026-01-01T00:00:00Z"),
 *         Instant.parse("2026-01-02T12:00:00Z"));
 *
 * // Usage
 * Instant beginning = range.getBeginning();
 * Instant end = range.getEnd();
 * Duration duration = range.getDuration();
 * </pre>
 */
public
class InstantRange
    extends TemporalRange<Instant>
{
    public
    InstantRange(Instant beginning,Instant end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static InstantRange
    of(Instant beginning,Instant end)
        throws NullPointerException, IllegalArgumentException
    {
        return new InstantRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
