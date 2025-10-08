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