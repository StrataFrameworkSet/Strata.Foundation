//////////////////////////////////////////////////////////////////////////////
// ServiceEvent.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.transfer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.Instant;
import java.util.UUID;

/**
 * <p>
 * Base class for events published within or between services. Carries the
 * bookkeeping common to every event: a unique {@link #getEventId() event
 * identifier}, an optional {@link #getCorrelationId() correlation
 * identifier} linking it to a related request or workflow, a human-readable
 * {@link #getEventName() event name}, a {@link #getTimestamp() timestamp},
 * and the {@link #getSource() source object} that raised it. The {@link
 * com.fasterxml.jackson.annotation.JsonTypeInfo} annotation causes concrete
 * subclasses to be serialized with their class name so events can be
 * deserialized back to their original type.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * class CustomerUpdatedEvent extends ServiceEvent&lt;Customer&gt;
 * {
 *     CustomerUpdatedEvent(Customer customer)
 *     {
 *         super("CustomerUpdated",customer);
 *     }
 * }
 *
 * ServiceEvent&lt;Customer&gt; event =
 *     new CustomerUpdatedEvent(customer)
 *         .setCorrelationId(request.getRequestId());
 * </pre>
 *
 * @param <S> the type of the object that is the source of the event.
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS,property = "@class")
public abstract
class ServiceEvent<S>
{
    private UUID itsEventId;
    private UUID itsCorrelationId;
    private String itsEventName;
    private Instant itsTimestamp;
    private S       itsSource;

    protected
    ServiceEvent(String eventName)
    {
        this(eventName,null);
    }

    protected
    ServiceEvent(String eventName,S source)
    {
        itsEventId = UUID.randomUUID();
        itsCorrelationId = null;
        itsEventName = eventName;
        itsTimestamp = Instant.now();
        itsSource = source;
    }

    public ServiceEvent<S>
    setCorrelationId(UUID correlationId)
    {
        itsCorrelationId = correlationId;
        return this;
    }

    public ServiceEvent<S>
    setTimestamp(Instant timestamp)
    {
        itsTimestamp = timestamp;
        return this;
    }

    public ServiceEvent<S>
    setSource(S source)
    {
        itsSource = source;
        return this;
    }

    public UUID
    getEventId()
    {
        return itsEventId;
    }

    public UUID
    getCorrelationId() { return itsCorrelationId; }

    public String
    getEventName()
    {
        return itsEventName;
    }

    public Instant
    getTimestamp()
    {
        return itsTimestamp;
    }

    public S
    getSource()
    {
        return itsSource;
    }
}

//////////////////////////////////////////////////////////////////////////////
