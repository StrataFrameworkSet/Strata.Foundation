import {IFormatter} from "./IFormatter";
import {NullPointerException} from "../Utility/NullPointerException";

export type IFormatterOrLambda<V,O> = IFormatter<V,O> | ((input: V) => O);

export
class LambdaFormatter<V,O>
    implements IFormatter<V,O>
{
    private readonly lambda: (input: V) => O;

    public constructor(input: IFormatterOrLambda<V,O>)
    {
        if (input == null)
            throw new NullPointerException("input is null");

        if (LambdaFormatter.isFormatter(input))
            this.lambda = (x: V) => (<IFormatter<V,O>>input).format(x);
        else
            this.lambda = <(x: V) => O>input;
    }

    public format(input: V): O
    {
        return this.lambda(input);
    }

    public static of<V,O>(input: IFormatterOrLambda<V,O>): LambdaFormatter<V,O>
    {
        return new LambdaFormatter<V,O>(input);
    }

    private static isFormatter<V,O>(input: IFormatterOrLambda<V,O>): boolean
    {
        return typeof input === "object" && "format" in input;
    }
}