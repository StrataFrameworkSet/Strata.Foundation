//////////////////////////////////////////////////////////////////////////////
// ServiceReply.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.transfer;

import java.time.Instant;
import java.util.UUID;

public abstract
class ServiceReply
{
    private UUID          itsCorrelationId;
    private Instant       itsTimestamp;
    private boolean       itsSuccessIndicator;
    private String        itsSuccessMessage;
    private String        itsFailureMessage;
    private ExceptionData itsException;

    protected
    ServiceReply()
    {
        itsCorrelationId = null;
        itsTimestamp = Instant.now();
        itsSuccessIndicator = false;
        itsSuccessMessage = null;
        itsFailureMessage = null;
        itsException = null;
    }

    public ServiceReply
    setCorrelationId(UUID correlationId)
    {
        itsCorrelationId = correlationId;
        return this;
    }

    public ServiceReply
    setTimestamp(Instant timestamp)
    {
        itsTimestamp = timestamp;
        return this;
    }

    public ServiceReply
    setSuccess(boolean success)
    {
        itsSuccessIndicator = success;
        return this;
    }

    public ServiceReply
    setSuccessMessage(String successMessage)
    {
        itsSuccessMessage = successMessage;
        return this;
    }

    public ServiceReply
    setFailureMessage(String failureMessage)
    {
        itsFailureMessage = failureMessage;
        return this;
    }

    public ServiceReply
    setException(ExceptionData exception)
    {
        itsException = exception;
        return this;
    }

    public UUID
    getCorrelationId() { return itsCorrelationId; }

    public Instant
    getTimestamp()
    {
        return itsTimestamp;
    }

    public boolean
    isSuccess()
    {
        return itsSuccessIndicator;
    }

    public String
    getSuccessMessage()
    {
        return itsSuccessMessage;
    }

    public String
    getFailureMessage()
    {
        return itsFailureMessage;
    }

    public ExceptionData
    getException() { return itsException; }

    public boolean
    hasException() { return itsException != null; }

}

//////////////////////////////////////////////////////////////////////////////