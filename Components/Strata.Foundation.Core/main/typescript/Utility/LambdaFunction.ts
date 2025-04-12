import {IFunction} from "./IFunction";
import {NullPointerException} from "./NullPointerException";

export type IFunctionOrLambda<I,O> = IFunction<I,O> | ((input: I) => O);

export
class LambdaFunction<I,O>
    implements IFunction<I,O>
{
    private readonly lambda: (input: I) => O;

    constructor(input: IFunctionOrLambda<I,O>)
    {
        if (input == null)
            throw new NullPointerException("input is null");

        if (LambdaFunction.isFunction(input))
            this.lambda = (x: I) => (<IFunction<I,O>>input).apply(x);
        else
            this.lambda = <(x: I) => O>input;
    }

    apply(input: I): O
    {
        return this.lambda(input);
    }

    public static of<I,O>(input: IFunctionOrLambda<I,O>): LambdaFunction<I,O>
    {
        return new LambdaFunction<I, O>(input);
    }

    private static isFunction<I,O>(input: IFunctionOrLambda<I,O>): boolean
    {
        return typeof input === "object" && "apply" in input;
    }
}