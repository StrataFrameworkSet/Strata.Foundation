//////////////////////////////////////////////////////////////////////////////
// Mapped.ts
//////////////////////////////////////////////////////////////////////////////

import {Optional}              from "./Optional";
import {NullPointerException}  from "./NullPointerException";

export
class Mapped<I,O>
{
    private readonly method:    string;
    private readonly input:     I;
    private readonly output:    O | null;
    private readonly throwable: Error | null;

    private
    constructor(method: string,input: I,output: O | null,throwable: Error | null)
    {
        this.method    = method;
        this.input     = input;
        this.output    = output;
        this.throwable = throwable;
    }

    getMethod(): string { return this.method; }

    getInput(): I { return this.input; }

    getOutput(): Optional<O> { return Optional.ofNullable<O>(this.output); }

    getThrowable(): Optional<Error> { return Optional.ofNullable<Error>(this.throwable); }

    isSuccess(): boolean { return this.output != null; }

    isFailure(): boolean { return this.throwable != null; }

    static of<I,O>(method: string,input: I,output: O): Mapped<I,O>;
    static of<I,O>(method: string,input: I,throwable: Error): Mapped<I,O>;
    static of<I,O>(method: string,input: I,third: O | Error): Mapped<I,O>
    {
        if (method == null)
            throw new NullPointerException("method is null");

        if (input == null)
            throw new NullPointerException("input is null");

        if (third instanceof Error)
            return new Mapped<I,O>(method,input,null,third);

        if (third == null)
            return new Mapped<I,O>(method,input,null,new NullPointerException("output is null"));

        return new Mapped<I,O>(method,input,third,null);
    }
}

//////////////////////////////////////////////////////////////////////////////
