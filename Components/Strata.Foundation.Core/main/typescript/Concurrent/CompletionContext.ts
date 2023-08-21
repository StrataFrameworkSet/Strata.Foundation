import {CompletionError} from "./CompletionError";

export
class CompletionContext<T>
{
    private result: T;
    private error: CompletionError;

    protected constructor(result: T,error: CompletionError)
    {
        this.result = result;
        this.error = error;
    }

    setResult(result: T): CompletionContext<T>
    {
        this.result = result;
        return this;
    }

    clearResult(): CompletionContext<T>
    {
        this.result = null;
        return this;
    }

    setError(error: CompletionError): CompletionContext<T>
    {
        this.error = error;
        return this;
    }

    clearError(): CompletionContext<T>
    {
        this.error = null;
        return this;
    }

    getResult(): T
    {
        return this.result;
    }

    getError(): CompletionError
    {
        return this.error;
    }

    hasResult(): boolean
    {
        return this.result != null;
    }

    hasError(): boolean
    {
        return this.error != null;
    }

    combine(other: CompletionContext<T>,combiner: (x: T,y: T) => T): CompletionContext<T>
    {
        if (this.hasError() || other.hasError())
        {
            let x: CompletionError = this.getError();
            let y: CompletionError = other.getError();

            if (x != null && y != null)
                return new CompletionContext<T>(
                    combiner(this.getResult(),other.getResult()),
                    new CompletionError([x.getCause(),y.getCause()]));
            else if (x != null)
                return new CompletionContext<T>(
                    combiner(this.getResult(),other.getResult()),
                    x);
            else
                return new CompletionContext<T>(
                    combiner(this.getResult(),other.getResult()),
                    y);
        }
        else
        {
            return CompletionContext.ofResult(
                combiner(this.getResult(),other.getResult()));
        }
    }

    accept(consumer: (context: CompletionContext<T>) => void): CompletionContext<T>
    {
        consumer(this);
        return this;
    }

    static empty<T>(): CompletionContext<T>
    {
        return new CompletionContext<T>(null,null);
    }

    static ofResult<T>(result: T): CompletionContext<T>
    {
        return new CompletionContext<T>(result,null);
    }

    static ofError<T>(error: CompletionError): CompletionContext<T>
    {
        return new CompletionContext<T>(null,error);
    }
}