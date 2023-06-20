//////////////////////////////////////////////////////////////////////////////
// ServiceRequest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.transfer;

import java.time.Instant;
import java.util.UUID;

public abstract
class ServiceRequest
{
    private UUID itsCorrelationId;
    private Instant itsTimestamp;

    protected
    ServiceRequest()
    {
        itsCorrelationId = null;
        itsTimestamp = Instant.now();
    }

    public ServiceRequest
    setCorrelationId(UUID correlationId)
    {
        itsCorrelationId = correlationId;
        return this;
    }

    public ServiceRequest
    setTimestamp(Instant timestamp)
    {
        itsTimestamp = timestamp;
        return this;
    }

    public UUID
    getCorrelationId() { return itsCorrelationId; }

    public Instant
    getTimestamp()
    {
        return itsTimestamp;
    }
}

//////////////////////////////////////////////////////////////////////////////