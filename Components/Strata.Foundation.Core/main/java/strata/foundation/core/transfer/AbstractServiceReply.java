//////////////////////////////////////////////////////////////////////////////
// AbstractServiceReply.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.transfer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import strata.foundation.core.collection.IMultiMap;
import strata.foundation.core.collection.ImmutableMultiMap;
import strata.foundation.core.collection.ListValuedMultiMap;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * Base class for replies returned in response to a service request. Carries
 * the bookkeeping common to every reply: a unique {@link #getReplyId() reply
 * identifier}, the {@link #getOriginatingRequestId() identifier of the
 * request} it answers, a {@link #getTimestamp() timestamp}, response
 * {@link #getHeaders() headers} and {@link #getCookies() cookies}, and an
 * indication of whether the request {@link #isSuccess() succeeded}, along
 * with a success or failure message and, when applicable, {@link
 * ExceptionData} describing a failure. Subclasses add whatever payload is
 * specific to a given service operation.
 * </p>
 * <p>
 * Instances are mutable via a fluent setter API (each setter returns {@code
 * this}) and implement {@link Serializable} so replies can cross process or
 * transport boundaries.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * class GetCustomerReply extends AbstractServiceReply
 * {
 *     private Customer customer;
 *
 *     GetCustomerReply(AbstractServiceRequest request)
 *     {
 *         super(request);
 *     }
 *
 *     GetCustomerReply setCustomer(Customer customer)
 *     {
 *         this.customer = customer;
 *         return this;
 *     }
 * }
 *
 * GetCustomerReply reply =
 *     new GetCustomerReply(request)
 *         .setCustomer(customer)
 *         .setSuccess(true)
 *         .setSuccessMessage("Customer found");
 * </pre>
 * </p>
 */
public abstract
class AbstractServiceReply
    implements Serializable
{
    private IMultiMap<String,Object> headers;
    private List<Cookie>             cookies;
    private UUID                     replyId;
    private UUID                     originatingRequestId;
    private Instant                  timestamp;
    private boolean                  successIndicator;
    private String                   successMessage;
    private String                   failureMessage;
    private ExceptionData            exception;

    protected
    AbstractServiceReply()
    {
        headers = new ListValuedMultiMap<>();
        cookies = new ArrayList<>();
        replyId = UUID.randomUUID();
        originatingRequestId = null;
        timestamp = Instant.now();
        successIndicator = false;
        successMessage = null;
        failureMessage = null;
        exception = null;
    }

    protected
    AbstractServiceReply(AbstractServiceRequest originatingRequest)
    {
        headers = new ListValuedMultiMap<>();
        cookies = new ArrayList<>();
        replyId = UUID.randomUUID();
        originatingRequestId =
            originatingRequest != null
                ? originatingRequest.getRequestId() : null;
        timestamp = Instant.now();
        successIndicator = false;
        successMessage = null;
        failureMessage = null;
        exception = null;
    }

    @JsonIgnore
    public AbstractServiceReply
    setHeaders(IMultiMap<String,Object> headers)
    {
        this.headers = new ListValuedMultiMap<>(headers);
        return this;
    }

    @JsonIgnore
    public AbstractServiceReply
    setCookies(List<Cookie> cookies)
    {
        this.cookies = new ArrayList<>(cookies);
        return this;
    }

    public AbstractServiceReply
    setReplyId(UUID replyId)
    {
        this.replyId = replyId;
        return this;
    }

    public AbstractServiceReply
    setOriginatingRequestId(UUID requestId)
    {
        originatingRequestId = requestId;
        return this;
    }

    public AbstractServiceReply
    setTimestamp(Instant timestamp)
    {
        this.timestamp = timestamp;
        return this;
    }

    public AbstractServiceReply
    setSuccess(boolean success)
    {
        successIndicator = success;
        return this;
    }

    public AbstractServiceReply
    setSuccessMessage(String successMessage)
    {
        this.successMessage = successMessage;
        failureMessage = null;
        return this;
    }

    public AbstractServiceReply
    setFailureMessage(String failureMessage)
    {
        this.failureMessage = failureMessage;
        successMessage = null;
        return this;
    }

    public AbstractServiceReply
    setException(ExceptionData exception)
    {
        this.exception = exception;
        return this;
    }

    public AbstractServiceReply
    addHeader(String name,Object value)
    {
        this.headers.put(name,value);
        return this;
    }

    public AbstractServiceReply
    clearHeaders()
    {
        this.headers.clear();
        return this;
    }

    public AbstractServiceReply
    addCookie(Cookie cookie)
    {
        this.cookies.add(cookie);
        return this;
    }

    public AbstractServiceReply
    clearCookies()
    {
        this.cookies.clear();
        return this;
    }

    @JsonIgnore
    public IMultiMap<String,Object>
    getHeaders() { return ImmutableMultiMap.of(headers); }

    @JsonIgnore
    public List<Cookie>
    getCookies() { return Collections.unmodifiableList(cookies); }

    public UUID
    getReplyId() { return replyId; }

    public UUID
    getOriginatingRequestId() { return originatingRequestId; }

    public Instant
    getTimestamp()
    {
        return timestamp;
    }

    public boolean
    isSuccess()
    {
        return successIndicator;
    }

    public String
    getSuccessMessage()
    {
        return successMessage;
    }

    public String
    getFailureMessage()
    {
        return failureMessage;
    }

    public ExceptionData
    getException() { return exception; }

    public boolean
    hasException() { return exception != null; }

}

//////////////////////////////////////////////////////////////////////////////