import {ExceptionData} from "./ExceptionData";

export
class ExceptionDataBuilder
{
    private exceptionType: string;
    private exceptionCode: number;
    private exceptionMessage: string;
    private stackTrace: string;
    private cause: ExceptionDataBuilder;

    constructor()
    {
        this.exceptionType = null;
        this.exceptionCode = null;
        this.exceptionMessage = null;
        this.stackTrace = null;
        this.cause = null;
    }

    setExceptionType(exceptionType:string): ExceptionDataBuilder
    {
        this.exceptionType = exceptionType;
        return this;
    }

    setExceptionCode(exceptionCode:number): ExceptionDataBuilder
    {
        this.exceptionCode = exceptionCode;
        return this;
    }

    setExceptionMessage(exceptionMessage:string): ExceptionDataBuilder
    {
        this.exceptionMessage = exceptionMessage;
        return this;
    }

    setStackTrace(stackTrace:string): ExceptionDataBuilder
    {
        this.stackTrace = stackTrace;
        return this;
    }

    setCause(cause:ExceptionDataBuilder): ExceptionDataBuilder
    {
        this.cause = cause;
        return this;
    }

    getExceptionType(): string
    {
        return this.exceptionType;
    }

    getExceptionCode(): number
    {
        return this.exceptionCode;
    }

    getExceptionMessage(): string
    {
        return this.exceptionMessage;
    }

    getStackTrace(): string
    {
        return this.stackTrace;
    }

    getCause(): ExceptionDataBuilder
    {
        return this.cause;
    }

    build(): ExceptionData
    {
        const output: ExceptionData =
            {
                exceptionType: this.getExceptionType(),
                exceptionCode: this.getExceptionCode(),
                exceptionMessage: this.getExceptionMessage(),
                stackTrace: this.getStackTrace(),
                cause: this.getCause().build()
            };

        return output;
    }

}