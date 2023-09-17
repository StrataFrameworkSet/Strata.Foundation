import {Instant} from "@js-joda/core";

export
abstract class AbstractServiceRequest
{
    private requestId: string;
    private timestamp: Instant;

    protected constructor()
    {
        this.requestId = null;
        this.timestamp = Instant.now();
    }

    setRequestId(requestId: string): AbstractServiceRequest
    {
        this.requestId = requestId;
        return this;
    }

    setTimestamp(timestamp:Instant): AbstractServiceRequest
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

}