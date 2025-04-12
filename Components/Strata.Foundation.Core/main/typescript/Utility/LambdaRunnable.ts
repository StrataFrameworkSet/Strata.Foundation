import {IRunnable} from "./IRunnable";
import {NullPointerException} from "./NullPointerException";

export type IRunnableOrLambda = IRunnable | (() => void);

export
class LambdaRunnable
    implements IRunnable
{
    private readonly lambda: () => void;

    constructor(input: IRunnableOrLambda)
    {
        if (input == null)
            throw new NullPointerException("input is null");

        if (LambdaRunnable.isRunnable(input))
            this.lambda = () => (<IRunnable>input).run();
        else
            this.lambda = <() => void>input;
    }

    run(): void
    {
        this.lambda();
    }

    public static of(input: IRunnableOrLambda): LambdaRunnable
    {
        return new LambdaRunnable(input);
    }

    private static isRunnable(input: IRunnableOrLambda): boolean
    {
        return typeof input === "object" && "run" in input;
    }
}