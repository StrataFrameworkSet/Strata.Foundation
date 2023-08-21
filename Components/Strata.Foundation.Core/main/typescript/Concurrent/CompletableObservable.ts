import {ICompletionStage} from "./ICompletionStage";
import {from,Observable,of} from "rxjs";
import {map,mergeMap} from 'rxjs/operators';
import {CompletionContext} from "./CompletionContext";
import {CompletionError} from "./CompletionError";

export
class CompletableObservable<T>
    implements ICompletionStage<T>
{
    private observable: Observable<CompletionContext<T>>;

    protected constructor(
        observable: Observable<T>|Observable<CompletionContext<T>>,
        hasContext: boolean = false)
    {
        if (hasContext)
            this.observable = <Observable<CompletionContext<T>>>observable;
        else
            this.observable = this.initializeObservable(<Observable<T>>observable);

    }

    thenApply<U>(func: (input: T) => U): ICompletionStage<U>
    {
        return new CompletableObservable<U>(
            this
                .observable
                .pipe(map(context => this.doApply(context,func))),
            true);
    }

    thenCompose<U>(func: (input: T) => ICompletionStage<U>): ICompletionStage<U>
    {
        return new CompletableObservable<U>(
            this
                .observable
                .pipe<CompletionContext<U>>(
                    mergeMap(
                        context =>
                            this.toObservable<U>(this.doCompose<U>(func,context))
                                .pipe(map(context => context)))),
            true);
    }

    thenAccept(consumer: (input: T) => void,handler?: (error: Error) => void): ICompletionStage<T>
    {
        return new CompletableObservable<T>(
            this
                .observable
                .pipe(map(context => this.doAccept(context,consumer,handler))),
            true);
    }

    exceptionally(func: (error: Error) => T): ICompletionStage<T>
    {
        return new CompletableObservable<T>(
            this
                .observable
                .pipe(
                    map(context => this.doExceptionally(context,func))),
            true);
    }

    subscribe(
        consumer?: (result: T) => void,
        handler?: (error: CompletionError) => void): void
    {
        if (consumer == null)
            consumer = result => {};

        if (handler == null)
            handler = error => {};

        this
            .observable
            .subscribe(
                context =>
                {
                    if (context.hasError())
                        handler(context.getError());
                    else
                        consumer(context.getResult());
                },
                    error => handler(error));
    }

    static fromResult<U>(result: U): ICompletionStage<U>
    {
        return new CompletableObservable<U>(of(result),false);
    }

    static fromObservable<U>(observable: Observable<U>): ICompletionStage<U>
    {
        return new CompletableObservable<U>(observable,false);
    }

    static fromPromise<U>(promise: Promise<U>): ICompletionStage<U>
    {
        return new CompletableObservable<U>(<Observable<U>>from(promise),false);
    }

    static fromError<U>(error: Error): ICompletionStage<U>
    {
        return new CompletableObservable<U>(
            new Observable<CompletionContext<U>>(
                observer =>
                    observer.next(
                        CompletionContext.ofError<U>(
                            new CompletionError(error)))),
            true);
    }

    static supplyAsync<U>(supplier: () => U): ICompletionStage<U>
    {
        return new CompletableObservable<U>(
            new Observable<U>(
                observer => observer.next(supplier())),
            false);
    }

    private initializeObservable(input: Observable<T>): Observable<CompletionContext<T>>
    {
        return input.pipe(map(value => CompletionContext.ofResult(value)));
    }

    private doApply<U>(context: CompletionContext<T>,func: (input: T) => U): CompletionContext<U>
    {
        console.log("doApply");
        if (context.hasError())
            return CompletionContext.ofError<U>(context.getError());

        try
        {
            return CompletionContext.ofResult(func(context.getResult()));
        }
        catch (e)
        {
            console.log("Caught exception in doApply");
            return CompletionContext.ofError<U>(new CompletionError(e));
        }
    }

    private doCompose<U>(
        func: (input: T) => ICompletionStage<U>,
        context: CompletionContext<T>): CompletionContext<ICompletionStage<U>>
    {
        console.log("doCompose");
        if (context.hasError())
            return CompletionContext.ofError<ICompletionStage<U>>(context.getError());

        try
        {
            return CompletionContext.ofResult(func(context.getResult()));
        }
        catch (e)
        {
            console.log("Caught exception in doCompose");
            return CompletionContext.ofError<ICompletionStage<U>>(e);
        }
    }

    private doAccept(
        context: CompletionContext<T>,
        consumer: (input: T) => void,
        handler?: (error: Error) => void): CompletionContext<T>
    {
        console.log("doAccept");
        if (context.hasError())
        {
            if (handler != null)
            {
                try
                {
                    handler(context.getError());
                }
                catch (e)
                {
                    console.log("Caught exception in doAccept");
                    return context.setError(new CompletionError(e));
                }
            }
        }
        else
        {
            try
            {
                consumer(context.getResult());
            }
            catch (e)
            {
                return context.setError(new CompletionError(e));
            }
        }

        return context;
    }

    private doExceptionally(context: CompletionContext<T>,func: (error: Error) => T): CompletionContext<T>
    {
        console.log("doExceptionally");
        if (context.hasError())
            return CompletionContext.ofResult(func(context.getError()));

        return context;
    }

    private toObservable<U>(input: CompletionContext<ICompletionStage<U>>): Observable<CompletionContext<U>>
    {
        if (input.hasResult())
            return (<CompletableObservable<U>>input.getResult()).observable;

        return this.observable.pipe(
            map(
                (context,ignore) =>
                    CompletionContext.ofError<U>(input.getError())))
    }
}