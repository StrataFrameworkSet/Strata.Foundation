import {Instant} from "@js-joda/core";
import {ExceptionDataBuilder} from "./ExceptionDataBuilder";
import {AbstractServiceRequest} from "./AbstractServiceRequest";
import {AbstractServiceReply} from "./AbstractServiceReply";

export
abstract class AbstractServiceReplyBuilder<R extends AbstractServiceReply>
{
    private replyId: string;
    private originatingRequestId: string;
    private timestamp: Instant;
    private success: boolean;
    private successMessage: string;
    private failureMessage: string;
    private exception: ExceptionDataBuilder;

    protected constructor(originatingRequest: AbstractServiceRequest = null)
    {
        this.replyId = null;
        this.originatingRequestId =
            originatingRequest != null ? originatingRequest.requestId : null;
        this.timestamp = Instant.now();
        this.success = false;
        this.successMessage = null;
        this.failureMessage = null;
        this.exception = null;
    }

    setReplyId(replyId: string): AbstractServiceReplyBuilder<R>
    {
        this.replyId = replyId;
        return this;
    }

    setOriginatingRequestId(requestId: string): AbstractServiceReplyBuilder<R>
    {
        this.originatingRequestId = requestId;
        return this;
    }

    setTimestamp(timestamp:Instant): AbstractServiceReplyBuilder<R>
    {
        this.timestamp = timestamp;
        return this;
    }

    setSuccess(success:boolean): AbstractServiceReplyBuilder<R>
    {
        this.success = success;
        return this;
    }

    setSuccessMessage(successMessage:string): AbstractServiceReplyBuilder<R>
    {
        this.successMessage = successMessage;
        this.failureMessage = null;
        return this;
    }

    setFailureMessage(failureMessage:string): AbstractServiceReplyBuilder<R>
    {
        this.failureMessage = failureMessage;
        this.successMessage = null;
        return this;
    }

    setException(exception:ExceptionDataBuilder): AbstractServiceReplyBuilder<R>
    {
        this.exception = exception;
        return this;
    }

    getReplyId(): string
    {
        return this.replyId;
    }

    getOriginatingRequestId(): string
    {
        return this.originatingRequestId;
    }

    getTimestamp(): Instant
    {
        return this.timestamp;
    }

    getSuccess(): boolean
    {
        return this.success;
    }

    getSuccessMessage(): string
    {
        return this.successMessage;
    }

    getFailureMessage(): string
    {
        return this.failureMessage;
    }

    getException(): ExceptionDataBuilder
    {
        return this.exception;
    }

    abstract build(): R;
}