import {Instant} from "@js-joda/core";
import {AbstractServiceRequest} from "./AbstractServiceRequest";

export
abstract class AbstractServiceRequestBuilder<R extends AbstractServiceRequest>
{
    private requestId: string;
    private timestamp: Instant;

    protected constructor()
    {
        this.requestId = null;
        this.timestamp = Instant.now();
    }

    setRequestId(requestId: string): AbstractServiceRequestBuilder<R>
    {
        this.requestId = requestId;
        return this;
    }

    setTimestamp(timestamp:Instant): AbstractServiceRequestBuilder<R>
    {
        this.timestamp = timestamp;
        return this;
    }

    getRequestId(): string
    {
        return this.requestId;
    }

    getTimestamp(): Instant
    {
        return this.timestamp;
    }

    abstract build(): R;
}