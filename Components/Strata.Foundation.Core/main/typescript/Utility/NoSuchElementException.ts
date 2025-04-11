export
class NoSuchElementException
    extends Error
{
    private readonly itsCause: any;

    constructor(cause: any)
    {
        super(cause instanceof Error ? cause.message : cause.toString());
        this.itsCause = cause;
    }

    getCause(): any
    {
        return this.itsCause;
    }
}