import {ISupplier} from "./ISupplier";
import { Optional } from "./Optional";
import {ISupplierOrLambda, LambdaSupplier} from "./LambdaSupplier";
import {IRunnableOrLambda, LambdaRunnable} from "./LambdaRunnable";

export
class Conditional
    implements ISupplier<boolean>
{
    public static readonly TRUE: Conditional = Conditional.of(true);
    public static readonly FALSE: Conditional = Conditional.of(false);

    private readonly condition: boolean;

    constructor(condition: boolean)
    {
        this.condition = condition;
    }

    get(): boolean
    {
        return this.condition;
    }

    ifTrue<T>(supplier: ISupplierOrLambda<T>): Optional<T>
    {
        return this.condition
            ? Optional.ofNullable(LambdaSupplier.of(supplier).get())
            : Optional.empty();
    }

    ifFalse<T>(supplier: ISupplierOrLambda<T>): Optional<T>
    {
        return !this.condition
            ? Optional.ofNullable(LambdaSupplier.of(supplier).get())
            : Optional.empty();
    }

    ifTrueNoReturn(action:IRunnableOrLambda)
    {
        if (this.condition)
            LambdaRunnable.of(action).run();
    }

    ifFalseNoReturn(action:IRunnableOrLambda)
    {
        if (!this.condition)
            LambdaRunnable.of(action).run();
    }

    ifTrueOrThrow<T>(supplier: ISupplierOrLambda<T>,error: Error): T
    {
        if (this.condition)
            return LambdaSupplier.of(supplier).get();

        throw error;
    }

    ifFalseOrThrow<T>(supplier: ISupplierOrLambda<T>,error: Error): T
    {
        if (!this.condition)
            return LambdaSupplier.of(supplier).get();

        throw error;
    }

    ifTrueOrThrowNoReturn(action: IRunnableOrLambda,error: Error): void
    {
        if (this.condition)
            LambdaRunnable.of(action).run();
        else
            throw error;
    }

    ifFalseOrThrowNoReturn(action: IRunnableOrLambda,error: Error): void
    {
        if (!this.condition)
            LambdaRunnable.of(action).run();
        else
            throw error;
    }

    ifTrueOrElse<T>(
        trueSupplier: ISupplierOrLambda<T>,
        falseSupplier: ISupplierOrLambda<T>): T
    {
        if (this.condition)
            return LambdaSupplier.of(trueSupplier).get();

        return LambdaSupplier.of(falseSupplier).get();
    }

    ifTrueOrElseNoReturn(
        trueAction: IRunnableOrLambda,
        falseAction: IRunnableOrLambda): void
    {
        if (this.condition)
            LambdaRunnable.of(trueAction).run();
        else
            LambdaRunnable.of(falseAction).run();
    }

    and(other: Conditional|boolean): Conditional
    {
        if (typeof other === 'boolean')
        {
            const input: boolean = other;

            return Conditional.of(this.condition && input);
        }

        return Conditional.of(this.condition && other.condition);
    }

    or(other: Conditional|boolean): Conditional
    {
        if (typeof other === 'boolean')
        {
            const input: boolean = other;

            return Conditional.of(this.condition || input);
        }

        return Conditional.of(this.condition || other.condition);
    }

    xor(other: Conditional|boolean): Conditional
    {
        if (typeof other === 'boolean')
        {
            const input: boolean = other;

            return Conditional.of(this.condition !== input);
        }

        return Conditional.of(this.condition !== other.condition);
    }

    not(): Conditional
    {
        return Conditional.of(!this.condition);
    }

    public static of(source: boolean): Conditional
    {
        return new Conditional(source);
    }

}