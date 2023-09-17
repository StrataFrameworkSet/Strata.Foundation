//////////////////////////////////////////////////////////////////////////////
// AbstractServiceReply.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.transfer;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public abstract
class AbstractServiceReply
    implements Serializable
{
    private UUID          itsReplyId;
    private UUID          itsOriginatingRequestId;
    private Instant       itsTimestamp;
    private boolean       itsSuccessIndicator;
    private String        itsSuccessMessage;
    private String        itsFailureMessage;
    private ExceptionData itsException;

    protected
    AbstractServiceReply()
    {
        itsReplyId = UUID.randomUUID();
        itsOriginatingRequestId = null;
        itsTimestamp = Instant.now();
        itsSuccessIndicator = false;
        itsSuccessMessage = null;
        itsFailureMessage = null;
        itsException = null;
    }

    protected
    AbstractServiceReply(AbstractServiceRequest originatingRequest)
    {
        itsReplyId = UUID.randomUUID();
        itsOriginatingRequestId =
            originatingRequest != null
                ? originatingRequest.getRequestId() : null;
        itsTimestamp = Instant.now();
        itsSuccessIndicator = false;
        itsSuccessMessage = null;
        itsFailureMessage = null;
        itsException = null;
    }

    public AbstractServiceReply
    setReplyId(UUID replyId)
    {
        itsReplyId = replyId;
        return this;
    }

    public AbstractServiceReply
    setOriginatingRequestId(UUID requestId)
    {
        itsOriginatingRequestId = requestId;
        return this;
    }

    public AbstractServiceReply
    setTimestamp(Instant timestamp)
    {
        itsTimestamp = timestamp;
        return this;
    }

    public AbstractServiceReply
    setSuccess(boolean success)
    {
        itsSuccessIndicator = success;
        return this;
    }

    public AbstractServiceReply
    setSuccessMessage(String successMessage)
    {
        itsSuccessMessage = successMessage;
        itsFailureMessage = null;
        return this;
    }

    public AbstractServiceReply
    setFailureMessage(String failureMessage)
    {
        itsFailureMessage = failureMessage;
        itsSuccessMessage = null;
        return this;
    }

    public AbstractServiceReply
    setException(ExceptionData exception)
    {
        itsException = exception;
        return this;
    }

    public UUID
    getReplyId() { return itsReplyId; }

    public UUID
    getOriginatingRequestId() { return itsOriginatingRequestId; }

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