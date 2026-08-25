//////////////////////////////////////////////////////////////////////////////
// Consumed.ts
//////////////////////////////////////////////////////////////////////////////

import {Optional}              from "./Optional";
import {NullPointerException}  from "./NullPointerException";

export
class Consumed<I>
{
    private readonly method:    string;
    private readonly input:     I;
    private readonly throwable: Error | null;

    private
    constructor(method: string,input: I,throwable: Error | null)
    {
        this.method    = method;
        this.input     = input;
        this.throwable = throwable;
    }

    getMethod(): string { return this.method; }

    getInput(): I { return this.input; }

    getException(): Optional<Error> { return Optional.ofNullable<Error>(this.throwable); }

    isSuccess(): boolean { return this.throwable == null; }

    isFailure(): boolean { return this.throwable != null; }

    static of<I>(method: string,input: I): Consumed<I>;
    static of<I>(method: string,input: I,throwable: Error): Consumed<I>;
    static of<I>(method: string,input: I,throwable?: Error): Consumed<I>
    {
        if (method == null)
            throw new NullPointerException("method is null");

        if (input == null)
            throw new NullPointerException("input is null");

        if (throwable !== undefined)
        {
            if (throwable == null)
                return new Consumed<I>(method,input,new NullPointerException("throwable is null"));

            return new Consumed<I>(method,input,throwable);
        }

        return new Consumed<I>(method,input,null);
    }
}

//////////////////////////////////////////////////////////////////////////////
