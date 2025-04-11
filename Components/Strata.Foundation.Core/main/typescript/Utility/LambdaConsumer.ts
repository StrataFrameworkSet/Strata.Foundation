import {IConsumer} from "./IConsumer";

export type IConsumerOrLambda<T> = IConsumer<T> | ((value: T) => void);

export
class LambdaConsumer<T>
    implements IConsumer<T>
{
    private readonly lambda: (value: T) => void;

    constructor(input: IConsumerOrLambda<T>)
    {
        if (LambdaConsumer.isConsumer(input))
            this.lambda = (value: T) => (<IConsumer<T>>input).accept(value);

        this.lambda = <(value: T) => void>input;
    }

    accept(value: T): void
    {
        this.lambda(value);
    }

    public static of<T>(input: IConsumerOrLambda<T>): LambdaConsumer<T>
    {
        return new LambdaConsumer<T>(input);
    }

    private static isConsumer<T>(input: IConsumerOrLambda<T>): boolean
    {
        return typeof input === "object" && "accept" in input;
    }
}