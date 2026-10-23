import "jest"
import {CompletionContext,ICompletionStage} from 'strata.foundation.core/Concurrent';
import {CompletableObservable,CompletionError} from "strata.foundation.core/Concurrent";
import {Observable,of} from "rxjs";
import {map} from "rxjs/operators";

describe(
    'CompletableObservable',
    () =>
    {
        it(
            "processing pipeline with apply, compose, and accept",
            () =>
            {
                let target: ICompletionStage<string> =
                    CompletableObservable.fromResult("step1");

                target
                    .thenApply(input => input + ".step2")
                    .thenCompose(input => CompletableObservable.fromResult(input))
                    .thenApply(input => input + ".step3")
                    .thenAccept(
                        input => expect(input).toBe("step1.step2.step3"))
                    .subscribe(
                        result => console.log("result = " + result.toString()),
                        error => console.log("error = " + error.message));
            });

        it(
            "processing pipeline with apply, compose, exceptionally, and accept",
            () =>
            {
                let target: ICompletionStage<string> =
                    CompletableObservable.supplyAsync(doStep1);

                target
                    .thenApply(input => doStep2(input))
                    .thenCompose(input => CompletableObservable.fromResult(input))
                    .thenApply(input => doStep3(input))
                    .exceptionally(error => error.message)
                    .thenAccept(
                        input => expect(input).toBe("error-test"))
                    .subscribe();
            });

        it(
            "processing pipeline with apply, compose, and accept (with error handler)",
            () =>
            {
                let target: ICompletionStage<string> =
                    CompletableObservable.supplyAsync(doStep1);

                target
                    .thenApply(input => doStep2(input))
                    .thenCompose(input => CompletableObservable.fromResult(input))
                    .thenApply(input => doStep3(input))
                    .thenAccept(
                        input => fail("should not reach this point"),
                        error => expect(error.message).toBe("error-test"))
                    .subscribe();
            });

        describe(
            'then',
            () =>
            {
                it(
                    "resolves with the result",
                    async () =>
                    {
                        let result: string = await CompletableObservable.fromResult("step1");

                        expect(result).toBe("step1");
                    });

                it(
                    "resolves with the result of a processing pipeline",
                    async () =>
                    {
                        let result: string =
                            await CompletableObservable
                                .fromResult("step1")
                                .thenApply(input => input + ".step2")
                                .thenCompose(input => CompletableObservable.fromResult(input))
                                .thenApply(input => input + ".step3");

                        expect(result).toBe("step1.step2.step3");
                    });

                it(
                    "passes the result to onFulfilled and resolves with its return value",
                    async () =>
                    {
                        let result: number =
                            await CompletableObservable
                                .fromResult("step1")
                                .then(input => input.length);

                        expect(result).toBe(5);
                    });

                it(
                    "resolves with the result of a promise",
                    async () =>
                    {
                        let result: number = await CompletableObservable.fromPromise(Promise.resolve(42));

                        expect(result).toBe(42);
                    });

                it(
                    "rejects with a CompletionError",
                    async () =>
                    {
                        expect.assertions(2);

                        try
                        {
                            await CompletableObservable.fromError<string>(new Error("error-test"));
                        }
                        catch (error)
                        {
                            expect(error).toBeInstanceOf(CompletionError);
                            expect(error.message).toBe("error-test");
                        }
                    });

                it(
                    "rejects with the error from a processing pipeline",
                    async () =>
                    {
                        expect.assertions(1);

                        try
                        {
                            await CompletableObservable
                                .supplyAsync(doStep1)
                                .thenApply(input => doStep2(input))
                                .thenApply(input => doStep3(input));
                        }
                        catch (error)
                        {
                            expect(error.message).toBe("error-test");
                        }
                    });

                it(
                    "passes the error to onRejected and resolves with its return value",
                    async () =>
                    {
                        let result: string =
                            await CompletableObservable
                                .supplyAsync(doStep1)
                                .thenApply(input => doStep2(input))
                                .then(
                                    input => fail("should not reach this point"),
                                    error => "handled " + error.message);

                        expect(result).toBe("handled error-test");
                    });

                it(
                    "resolves with the result of exceptionally",
                    async () =>
                    {
                        let result: string =
                            await CompletableObservable
                                .supplyAsync(doStep1)
                                .thenApply(input => doStep2(input))
                                .exceptionally(error => error.message);

                        expect(result).toBe("error-test");
                    });

                it(
                    "subscribes only once when called multiple times",
                    async () =>
                    {
                        let count: number = 0;
                        let target: ICompletionStage<string> =
                            CompletableObservable.supplyAsync(
                                () =>
                                {
                                    count++;
                                    return "step1";
                                });

                        let first: string  = await target;
                        let second: string = await target;

                        expect(first).toBe("step1");
                        expect(second).toBe("step1");
                        expect(count).toBe(1);
                    });
            });
    });

describe(
    'Observable',
    () =>
    {
        it(
            "processing pipeline with multiple pipe()s",
            () =>
            {
                let target: Observable<string> = of("step1");

                target
                    .pipe(map((input,ignore) => CompletionContext.ofResult(input)))
                    .pipe(map((context,ignore) => context.setResult(context.getResult() + ".step2")))
                    .pipe(map((context,ignore) => context.setResult(context.getResult() + ".step3")))
                    .pipe(map((context,ignore) => context.getResult()))
                    .subscribe(result => expect(result).toBe("step1.step2.step3"));
            });
    });

function doStep1(): string
{
    console.log("do step1");
    return "step1";
}

function doStep2(input: string): string
{
    console.log("throw error in step2");
    throw new Error("error-test");
}

function doStep3(input: string): string
{
    console.log("do step3");
    return input + ".step3";
}