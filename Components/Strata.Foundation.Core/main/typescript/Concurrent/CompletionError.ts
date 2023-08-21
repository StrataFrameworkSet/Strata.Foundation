export
class CompletionError
    extends Error
{
    private readonly cause: any;

    constructor(cause: any)
    {
        super(cause instanceof Error ? cause.message : cause.toString());
        this.cause = cause;
    }

    getCause(): any
    {
        return this.cause;
    }
}