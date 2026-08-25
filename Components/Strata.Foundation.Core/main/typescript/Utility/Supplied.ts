//////////////////////////////////////////////////////////////////////////////
// Supplied.ts
//////////////////////////////////////////////////////////////////////////////

import {Optional}              from "./Optional";
import {NullPointerException}  from "./NullPointerException";

export
class Supplied<O>
{
    private readonly method:    string;
    private readonly output:    O | null;
    private readonly throwable: Error | null;

    private
    constructor(method: string,output: O | null,throwable: Error | null)
    {
        this.method    = method;
        this.output    = output;
        this.throwable = throwable;
    }

    getMethod(): string { return this.method; }

    getOutput(): Optional<O> { return Optional.ofNullable<O>(this.output); }

    getException(): Optional<Error> { return Optional.ofNullable<Error>(this.throwable); }

    isSuccess(): boolean { return this.output != null; }

    isFailure(): boolean { return this.throwable != null; }

    static of<O>(method: string,output: O): Supplied<O>;
    static of<O>(method: string,throwable: Error): Supplied<O>;
    static of<O>(method: string,second: O | Error): Supplied<O>
    {
        if (method == null)
            throw new NullPointerException("method is null");

        if (second instanceof Error)
            return new Supplied<O>(method,null,second);

        if (second == null)
            return new Supplied<O>(method,null,new NullPointerException("output is null"));

        return new Supplied<O>(method,second,null);
    }
}

//////////////////////////////////////////////////////////////////////////////
