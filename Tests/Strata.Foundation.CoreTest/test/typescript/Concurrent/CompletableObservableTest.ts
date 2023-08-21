import "jest"
import {CompletionContext,ICompletionStage} from 'strata.foundation.core';
import {CompletableObservable} from "strata.foundation.core";
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