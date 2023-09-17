
export
class ExceptionData
{
    private exceptionType: string;
    private exceptionCode: number;
    private exceptionMessage: string;
    private stackTrace: string;
    private cause: ExceptionData;

    constructor()
    {
        this.exceptionType = null;
        this.exceptionCode = null;
        this.exceptionMessage = null;
        this.stackTrace = null;
        this.cause = null;
    }

    setExceptionType(exceptionType:string): ExceptionData
    {
        this.exceptionType = exceptionType;
        return this;
    }

    setExceptionCode(exceptionCode:number): ExceptionData
    {
        this.exceptionCode = exceptionCode;
        return this;
    }

    setExceptionMessage(exceptionMessage:string): ExceptionData
    {
        this.exceptionMessage = exceptionMessage;
        return this;
    }

    setStackTrace(stackTrace:string): ExceptionData
    {
        this.stackTrace = stackTrace;
        return this;
    }

    setCause(cause:ExceptionData): ExceptionData
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

    getCause(): ExceptionData
    {
        return this.cause;
    }
}