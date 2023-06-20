//////////////////////////////////////////////////////////////////////////////
// ServiceEvent.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.transfer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.Instant;
import java.util.UUID;

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