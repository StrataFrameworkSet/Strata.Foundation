import {Instant} from "@js-joda/core";
import {ExceptionData} from "./ExceptionData";
import {AbstractServiceRequest} from "./AbstractServiceRequest";

export
abstract class AbstractServiceReply
{
    private replyId: string;
    private originatingRequestId: string;
    private timestamp: Instant;
    private successIndicator: boolean;
    private successMessage: string;
    private failureMessage: string;
    private exception: ExceptionData;

    protected constructor(originatingRequest: AbstractServiceRequest = null)
    {
        this.replyId = null;
        this.originatingRequestId =
            originatingRequest != null ? originatingRequest.getRequestId() : null;
        this.timestamp = Instant.now();
        this.successIndicator = false;
        this.successMessage = null;
        this.failureMessage = null;
        this.exception = null;
    }

    setReplyId(replyId: string): AbstractServiceReply
    {
        this.replyId = replyId;
        return this;
    }

    setOriginatingRequestId(requestId: string): AbstractServiceReply
    {
        this.originatingRequestId = requestId;
        return this;
    }

    setTimestamp(timestamp:Instant): AbstractServiceReply
    {
        this.timestamp = timestamp;
        return this;
    }

    setSuccess(success:boolean): AbstractServiceReply
    {
        this.successIndicator = success;
        return this;
    }

    setSuccessMessage(successMessage:string): AbstractServiceReply
    {
        this.successMessage = successMessage;
        this.failureMessage = null;
        return this;
    }

    setFailureMessage(failureMessage:string): AbstractServiceReply
    {
        this.failureMessage = failureMessage;
        this.successMessage = null;
        return this;
    }

    setException(exception:ExceptionData): AbstractServiceReply
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
        return this.successIndicator;
    }

    getSuccessMessage(): string
    {
        return this.successMessage;
    }

    getFailureMessage(): string
    {
        return this.failureMessage;
    }

    getException(): ExceptionData
    {
        return this.exception;
    }
}