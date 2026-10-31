//////////////////////////////////////////////////////////////////////////////
// FiscalCalendarRegistry.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import strata.foundation.core.utility.Validate;

import java.time.DateTimeException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

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
        Validate.requireNonNull(id,"id is null");

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
        Validate.requireNonNull(calendar,"calendar is null");

        FiscalCalendar existing = calendars.putIfAbsent(calendar.getId(),calendar);

        Validate.require(
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
