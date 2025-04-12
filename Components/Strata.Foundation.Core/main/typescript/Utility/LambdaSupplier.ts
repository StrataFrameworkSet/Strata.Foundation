import {ISupplier} from "./ISupplier";
import {NullPointerException} from "./NullPointerException";

export type ISupplierOrLambda<T> = ISupplier<T> | (() => T);

export
class LambdaSupplier<T>
    implements ISupplier<T>
{
    private readonly lambda: () => T;

    constructor(input: ISupplierOrLambda<T>)
    {
        if (input == null)
            throw new NullPointerException("input is null");

        if (LambdaSupplier.isSupplier(input))
            this.lambda = () => (<ISupplier<T>>input).get();
        else
            this.lambda = <() => T>input;
    }

    get(): T
    {
        return this.lambda();
    }

    public static of<T>(input: ISupplierOrLambda<T>): LambdaSupplier<T>
    {
        return new LambdaSupplier<T>(input);
    }

    private static isSupplier<T>(input: ISupplierOrLambda<T>): boolean
    {
        return typeof input === "object" && "get" in input;
    }
}