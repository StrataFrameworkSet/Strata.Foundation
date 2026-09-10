//////////////////////////////////////////////////////////////////////////////
// AbstractServiceRequest.java
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
 * Base class for requests sent to a service operation. Carries the
 * bookkeeping common to every request: a unique {@link #getRequestId()
 * request identifier}, a {@link #getTimestamp() timestamp}, and request
 * {@link #getHeaders() headers} and {@link #getCookies() cookies}.
 * Subclasses add whatever payload is specific to a given service operation,
 * and typically pass {@code this} to an {@link AbstractServiceReply}
 * constructor so the reply can record which request it originated from.
 * </p>
 * <p>
 * Instances are mutable via a fluent setter API (each setter returns {@code
 * this}) and implement {@link Serializable} so requests can cross process or
 * transport boundaries.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * class GetCustomerRequest extends AbstractServiceRequest
 * {
 *     private String customerId;
 *
 *     GetCustomerRequest setCustomerId(String customerId)
 *     {
 *         this.customerId = customerId;
 *         return this;
 *     }
 * }
 *
 * GetCustomerRequest request =
 *     new GetCustomerRequest()
 *         .setCustomerId("12345")
 *         .addHeader("X-Correlation-Id",UUID.randomUUID().toString());
 * </pre>
 */
public abstract
class AbstractServiceRequest
    implements Serializable
{
    private IMultiMap<String,Object> headers;
    private List<Cookie>             cookies;
    private UUID                     requestId;
    private Instant                  timestamp;

    protected
    AbstractServiceRequest()
    {
        headers = new ListValuedMultiMap<>();
        cookies = new ArrayList<>();
        requestId = UUID.randomUUID();
        timestamp = Instant.now();
    }

    @JsonIgnore
    public AbstractServiceRequest
    setHeaders(IMultiMap<String,Object> headers)
    {
        this.headers = new ListValuedMultiMap<>(headers);
        return this;
    }

    @JsonIgnore
    public AbstractServiceRequest
    setCookies(List<Cookie> cookies)
    {
        this.cookies = new ArrayList<>(cookies);
        return this;
    }

    public AbstractServiceRequest
    setRequestId(UUID requestId)
    {
        this.requestId = requestId;
        return this;
    }

    public AbstractServiceRequest
    setTimestamp(Instant timestamp)
    {
        this.timestamp = timestamp;
        return this;
    }

    public AbstractServiceRequest
    addHeader(String name,Object value)
    {
        this.headers.put(name,value);
        return this;
    }

    public AbstractServiceRequest
    clearHeaders()
    {
        this.headers.clear();
        return this;
    }

    public AbstractServiceRequest
    addCookie(Cookie cookie)
    {
        this.cookies.add(cookie);
        return this;
    }

    public AbstractServiceRequest
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
    getRequestId() { return requestId; }

    public Instant
    getTimestamp() { return timestamp; }
}

//////////////////////////////////////////////////////////////////////////////
