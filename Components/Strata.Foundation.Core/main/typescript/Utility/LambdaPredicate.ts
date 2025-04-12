import {IPredicate} from "./IPredicate";
import {NullPointerException} from "./NullPointerException";

export type IPredicateOrLambda<T> = IPredicate<T> | ((value: T) => boolean);

export
class LambdaPredicate<T>
    implements IPredicate<T>
{
    private readonly lambda: (value: T) => boolean;

    constructor(input: IPredicateOrLambda<T>)
    {
        if (input == null)
            throw new NullPointerException("input is null");

        if (LambdaPredicate.isPredicate(input))
            this.lambda = (value: T) => (<IPredicate<T>>input).test(value);
        else
            this.lambda = <(value: T) => boolean>input;
    }

    test(value: T): boolean
    {
        return this.lambda(value);
    }

    public static of<T>(input: IPredicateOrLambda<T>): LambdaPredicate<T>
    {
        return new LambdaPredicate<T>(input);
    }

    private static isPredicate<T>(input: IPredicateOrLambda<T>): boolean
    {
        return typeof input === "object" && "test" in input;
    }
}