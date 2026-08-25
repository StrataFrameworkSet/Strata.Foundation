//////////////////////////////////////////////////////////////////////////////
// Executed.ts
//////////////////////////////////////////////////////////////////////////////

import {Optional}              from "./Optional";
import {NullPointerException}  from "./NullPointerException";

export
class Executed
{
    private readonly method:    string;
    private readonly throwable: Error | null;

    private
    constructor(method: string,throwable: Error | null)
    {
        this.method    = method;
        this.throwable = throwable;
    }

    getMethod(): string { return this.method; }

    getException(): Optional<Error> { return Optional.ofNullable<Error>(this.throwable); }

    isSuccess(): boolean { return this.throwable == null; }

    isFailure(): boolean { return this.throwable != null; }

    static of(method: string): Executed;
    static of(method: string,throwable: Error): Executed;
    static of(method: string,throwable?: Error): Executed
    {
        if (method == null)
            throw new NullPointerException("method is null");

        if (throwable !== undefined)
        {
            if (throwable == null)
                return new Executed(method,new NullPointerException("throwable is null"));

            return new Executed(method,throwable);
        }

        return new Executed(method,null);
    }
}

//////////////////////////////////////////////////////////////////////////////
