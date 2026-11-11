//////////////////////////////////////////////////////////////////////////////
// FiscalCalendarRegistry.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import strata.foundation.core.utility.Require;

import java.time.DateTimeException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * Static, thread-safe registry of {@link FiscalCalendar} instances keyed
 * by calendar id, with a default calendar used when a fiscal period is
 * created without an explicit calendar. The standard calendars are
 * registered automatically and {@code CALENDAR_YEAR} is the initial
 * default.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Registration
 * FiscalCalendarRegistry.register(customCalendar);
 * FiscalCalendarRegistry.setDefault(FiscalCalendar.US_FEDERAL);
 *
 * // Lookup
 * Optional&lt;FiscalCalendar&gt; calendar = FiscalCalendarRegistry.get("US_FEDERAL");
 * FiscalCalendar required = FiscalCalendarRegistry.getOrThrow("US_FEDERAL");
 * FiscalCalendar defaultCalendar = FiscalCalendarRegistry.getDefault();
 * </pre>
 */
public final
class FiscalCalendarRegistry
{
    private static final Map<String,FiscalCalendar>      calendars       = new ConcurrentHashMap<>();
    private static final AtomicReference<FiscalCalendar> defaultCalendar = new AtomicReference<>();

    static
    {
        setDefault(FiscalCalendar.CALENDAR_YEAR);
        register(FiscalCalendar.US_FEDERAL);
        register(FiscalCalendar.US_STATE);
        register(FiscalCalendar.UK_GOVERNMENT);
        register(FiscalCalendar.EU_GOVERNMENT);
    }

    private
    FiscalCalendarRegistry() {}

    public static Optional<FiscalCalendar>
    get(String id)
        throws NullPointerException
    {
        Require.notNull(id,"id is null");

        return Optional.ofNullable(calendars.get(id));
    }

    public static FiscalCalendar
    getOrThrow(String id)
        throws DateTimeException, NullPointerException
    {
        return
            get(id)
                .orElseThrow(() -> new DateTimeException("Unknown fiscal calendar: " + id));
    }

    public static FiscalCalendar
    getDefault()
    {
        return defaultCalendar.get();
    }

    public static void
    register(FiscalCalendar calendar)
        throws NullPointerException, IllegalArgumentException
    {
        Require.notNull(calendar,"calendar is null");

        FiscalCalendar existing = calendars.putIfAbsent(calendar.getId(),calendar);

        Require.isTrue(
            existing == null || existing.equals(calendar),
            () ->
                new IllegalArgumentException(
                    "a different fiscal calendar is already registered with id: " +
                    calendar.getId()));
    }

    public static void
    setDefault(FiscalCalendar calendar)
        throws NullPointerException, IllegalArgumentException
    {
        register(calendar);
        defaultCalendar.set(calendar);
    }
}

//////////////////////////////////////////////////////////////////////////////
