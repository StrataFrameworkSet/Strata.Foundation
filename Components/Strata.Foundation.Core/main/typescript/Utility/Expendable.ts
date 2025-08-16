import {IOptional} from "./IOptional";
import {ExpendableContext} from "./ExpendableContext";
import {Optional} from "./Optional";
import {IConsumerOrLambda, LambdaConsumer} from "./LambdaConsumer";
import {IFunctionOrLambda, LambdaFunction} from "./LambdaFunction";
import {NoSuchElementException} from "./NoSuchElementException";
import {ISupplierOrLambda, LambdaSupplier} from "./LambdaSupplier";
import {IRunnableOrLambda, LambdaRunnable} from "./LambdaRunnable";
import {IPredicateOrLambda, LambdaPredicate} from "./LambdaPredicate";

export
class Expendable<T>
    implements IOptional<T>
{
    private readonly allowed: number;
    private          context: Optional<ExpendableContext<T>>;

    public constructor(value?:T, limit?: number)
    {
        if (value != null)
        {
            this.allowed = (limit != null && limit > 0) ? limit : 1;
            this.context = Optional.of(new ExpendableContext<T>(value, this.allowed));
        }
        else
        {
            this.allowed = 0;
            this.context = Optional.empty();
        }
    }

    public get(): T
    {
        return this.context
                .map<T>(context => this.apply(context,(value:T) => value))
                .orElseThrow(new NoSuchElementException("No value present"));
    }

    public ifPresent(consumer: IConsumerOrLambda<T>): void
    {
        this.context.ifPresent(context => this.accept(context, consumer));
    }

    public ifPresentOrElse<U>(present: IFunctionOrLambda<T,U>, notPresent: ISupplierOrLambda<U>): U
    {
        return this.context
            .map(context => this.apply(context, present))
            .orElseGet(LambdaSupplier.of(notPresent));
    }

    public ifPresentOrThrow<U>(present: IFunctionOrLambda<T,U>, exception: Error): U
    {
        return this.context
            .map(context => this.apply(context, present))
            .orElseThrow(exception);
    }

    public ifPresentOrElseNoReturn(
        present:    IConsumerOrLambda<T>,
        notPresent: IRunnableOrLambda): void
    {
        if (this.context.isPresent())
            this.context.ifPresent(context => this.accept(context, present));
        else
            LambdaRunnable.of(notPresent).run();
    }

    public ifPresentOrThrowNoReturn(
        present:    IConsumerOrLambda<T>,
        notPresent: Error): void
    {
        if (this.context.isPresent())
            this.context.ifPresent(context => this.accept(context, present));
        else
            throw notPresent;
    }

    public ifNotPresent(notPresent: IRunnableOrLambda): void
    {
        if (!this.context.isPresent())
            LambdaRunnable.of(notPresent).run();
    }

    public filter(predicate: IPredicateOrLambda<T>): Expendable<T>
    {
        return this.context
            .filter(context => LambdaPredicate.of(predicate).test(context.getValue()))
            .map(context => new Expendable<T>(context.getValue(), this.allowed))
            .orElse(Expendable.empty());
    }

    public map<U>(mapper: IFunctionOrLambda<T,U>): Expendable<U>
    {
        return this.context
            .map(context => new Expendable<U>(this.apply(context, mapper), this.allowed))
            .orElse(Expendable.empty());
    }

    public flatMap<U>(mapper: IFunctionOrLambda<T,IOptional<U>>): Expendable<U>
    {
        return this.context
            .map(context => this.apply(context, mapper))
            .orElse(Expendable.empty()) as Expendable<U>;
    }

    or(supplier: ISupplierOrLambda<IOptional<T>>): Expendable<T>
    {
        if (this.isPresent())
            return this;

        return LambdaSupplier.of(supplier).get() as Expendable<T>;
    }

    public orElse(other: T): T
    {
        return this.context
            .map(context => this.apply(context,(value:T) => value))
            .orElse(other);
    }

    public orElseGet(other: ISupplierOrLambda<T>): T
    {
        return this.context
            .map(context => this.apply(context,(value:T) => value))
            .orElseGet(LambdaSupplier.of(other));
    }

    public orElseThrow(exception: Error): T
    {
        return this.context
            .map(context => this.apply(context,(value:T) => value))
            .orElseThrow(exception);
    }

    orElseGetThrow(supplier: ISupplierOrLambda<Error>): T
    {
        return this.context
            .map(context => this.apply(context,(value:T) => value))
            .orElseGetThrow(supplier);
    }

    public isPresent(): boolean
    {
        return this.context.isPresent();
    }

    public isEmpty(): boolean
    {
        return this.context.isEmpty();
    }

    public isExpended(): boolean
    {
        return this.context
            .map(context => context.isExpended())
            .orElse(true);
    }

    public getAllowed(): number
    {
        return this.allowed;
    }

    public getRemaining(): number
    {
        return this.context
            .map(context => context.getRemaining())
            .orElse(0);
    }

    public static of<T>(value?: T, limit?: number): Expendable<T>
    {
        return new Expendable<T>(value, limit);
    }

    public static empty<T>(limit?: number): Expendable<T>
    {
        return new Expendable<T>(undefined, limit);
    }


    private accept(context: ExpendableContext<T>, consumer: IConsumerOrLambda<T>): void
    {
        LambdaConsumer.of(consumer).accept(context.getValue());
        context.decrementRemaining();

        if (context.isExpended())
            this.context = Optional.empty();
    }

    private apply<U>(context: ExpendableContext<T>, func: IFunctionOrLambda<T,U>): U
    {
        const result: U = LambdaFunction.of(func).apply(context.getValue());
        context.decrementRemaining();

        if (context.isExpended())
            this.context = Optional.empty();

        return result;
    }
}