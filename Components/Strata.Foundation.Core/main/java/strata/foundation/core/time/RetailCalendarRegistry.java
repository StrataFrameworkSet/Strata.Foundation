//////////////////////////////////////////////////////////////////////////////
// RetailCalendarRegistry.java
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
 * Static, thread-safe registry of {@link RetailCalendar} instances keyed
 * by calendar id, with a default calendar used when a retail period is
 * created without an explicit calendar. {@code NRF} is the initial
 * default.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Registration
 * RetailCalendarRegistry.register(customCalendar);
 * RetailCalendarRegistry.setDefault(customCalendar);
 *
 * // Lookup
 * Optional&lt;RetailCalendar&gt; calendar = RetailCalendarRegistry.get("NRF");
 * RetailCalendar required = RetailCalendarRegistry.getOrThrow("NRF");
 * RetailCalendar defaultCalendar = RetailCalendarRegistry.getDefault();
 * </pre>
 */
public final
class RetailCalendarRegistry
{
    private static final Map<String,RetailCalendar>      calendars       = new ConcurrentHashMap<>();
    private static final AtomicReference<RetailCalendar> defaultCalendar = new AtomicReference<>();

    static
    {
        setDefault(RetailCalendar.NRF);
    }

    private
    RetailCalendarRegistry() {}

    public static Optional<RetailCalendar>
    get(String id)
        throws NullPointerException
    {
        Require.notNull(id,"id is null");

        return Optional.ofNullable(calendars.get(id));
    }

    public static RetailCalendar
    getOrThrow(String id)
        throws DateTimeException, NullPointerException
    {
        return
            get(id)
                .orElseThrow(() -> new DateTimeException("Unknown retail calendar: " + id));
    }

    public static RetailCalendar
    getDefault()
    {
        return defaultCalendar.get();
    }

    public static void
    register(RetailCalendar calendar)
        throws NullPointerException, IllegalArgumentException
    {
        Require.notNull(calendar,"calendar is null");

        RetailCalendar existing = calendars.putIfAbsent(calendar.getId(),calendar);

        Require.isTrue(
            existing == null || existing.equals(calendar),
            () ->
                new IllegalArgumentException(
                    "a different retail calendar is already registered with id: " +
                    calendar.getId()));
    }

    public static void
    setDefault(RetailCalendar calendar)
        throws NullPointerException, IllegalArgumentException
    {
        register(calendar);
        defaultCalendar.set(calendar);
    }
}

//////////////////////////////////////////////////////////////////////////////
