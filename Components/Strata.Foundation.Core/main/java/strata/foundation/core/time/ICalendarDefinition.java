//////////////////////////////////////////////////////////////////////////////
// ICalendarDefinition.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.io.Serializable;

/**
 * <p>
 * Serializable definition of a calendar system, such as a fiscal or
 * retail calendar, identified by a unique id. Calendar periods refer to
 * their calendar definition to determine their boundaries.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Selection
 * ICalendarDefinition calendar = FiscalCalendar.US_FEDERAL;
 *
 * // Usage
 * String id = calendar.getId();
 * </pre>
 */
public
interface ICalendarDefinition
    extends Serializable
{
    String
    getId();
}

//////////////////////////////////////////////////////////////////////////////
