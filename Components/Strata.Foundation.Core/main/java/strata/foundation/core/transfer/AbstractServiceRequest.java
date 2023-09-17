//////////////////////////////////////////////////////////////////////////////
// AbstractServiceRequest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.transfer;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public abstract
class AbstractServiceRequest
    implements Serializable
{
    private UUID    itsRequestId;
    private Instant itsTimestamp;

    protected
    AbstractServiceRequest()
    {
        itsRequestId = UUID.randomUUID();
        itsTimestamp = Instant.now();
    }

    public AbstractServiceRequest
    setRequestId(UUID requestId)
    {
        itsRequestId = requestId;
        return this;
    }

    public AbstractServiceRequest
    setTimestamp(Instant timestamp)
    {
        itsTimestamp = timestamp;
        return this;
    }

    public UUID
    getRequestId() { return itsRequestId; }

    public Instant
    getTimestamp()
    {
        return itsTimestamp;
    }
}

//////////////////////////////////////////////////////////////////////////////