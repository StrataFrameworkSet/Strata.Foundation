import {ISupplier} from "./ISupplier";
import {NoSuchElementException} from "./NoSuchElementException";
import {NullPointerException} from "./NullPointerException";
import {IConsumerOrLambda, LambdaConsumer} from "./LambdaConsumer";
import {IFunctionOrLambda, LambdaFunction} from "./LambdaFunction";
import {ISupplierOrLambda, LambdaSupplier} from "./LambdaSupplier";
import {IRunnableOrLambda, LambdaRunnable} from "./LambdaRunnable";
import {IPredicateOrLambda, LambdaPredicate} from "./LambdaPredicate";

export
class Optional<T>
    implements ISupplier<T>
{
    private readonly subject: T;

    constructor(subject: T)
    {
        this.subject = subject;
    }

    get(): T
    {
        if (this.isPresent())
            return this.subject;

        throw new NoSuchElementException("subject is null");
    }

    ifPresent(consumer: IConsumerOrLambda<T>): void
    {
        if (this.isPresent())
            LambdaConsumer.of(consumer).accept(this.subject);
    }

    ifPresentOrElse<U>(
        presentAction: IFunctionOrLambda<T,U>,
        emptyAction: ISupplierOrLambda<U>): U
    {
        if (this.isPresent())
            return LambdaFunction.of(presentAction).apply(this.subject);

        return LambdaSupplier.of(emptyAction).get();
    }

    ifPresentOrElseNoReturn(consumer: IConsumerOrLambda<T>,action: IRunnableOrLambda): void
    {
        if (this.isPresent())
            LambdaConsumer.of(consumer).accept(this.subject);
        else
            LambdaRunnable.of(action).run();
    }

    or(supplier: ISupplierOrLambda<Optional<T>>): Optional<T>
    {
        if (this.isPresent())
            return this;

        return LambdaSupplier.of(supplier).get();
    }

    orElse(alternative: T): T
    {
        if (this.isPresent())
            return this.subject;

        return alternative;
    }

    orElseGet(supplier: ISupplierOrLambda<T>): T
    {
        if (this.isPresent())
            return this.subject;

        return LambdaSupplier.of(supplier).get();
    }

    orElseThrow(error: Error): T
    {
        if (this.isPresent())
            return this.subject;

        throw error;
    }

    orElseGetThrow(supplier: ISupplierOrLambda<Error>): T
    {
        if (this.isPresent())
            return this.subject;

        throw LambdaSupplier.of(supplier).get();
    }

    filter(predicate: IPredicateOrLambda<T>): Optional<T>
    {
        if (predicate == null)
            throw new NullPointerException("predicate is null");

        if (this.isPresent() && LambdaPredicate.of(predicate).test(this.subject))
            return this;

        return Optional.empty();
    }

    map<U>(mapper: IFunctionOrLambda<T,U>): Optional<U>
    {
        if (mapper == null)
            throw new NullPointerException("mapper is null");

        if (this.isPresent())
            return Optional.ofNullable(
                LambdaFunction.of(mapper).apply(this.subject));

        return Optional.empty();
    }

    flatMap<U>(mapper: IFunctionOrLambda<T,Optional<U>>): Optional<U>
    {
        if (mapper == null)
            throw new NullPointerException("mapper is null");

        if (this.isPresent())
        {
            const output: Optional<U> =
                LambdaFunction.of(mapper).apply(this.subject);

            if (output != null)
                return output;

            throw new NullPointerException("mapper output is null");
        }

        return Optional.empty();
    }

    isPresent(): boolean
    {
        return this.subject != null;
    }

    isEmpty(): boolean
    {
        return !this.isPresent();
    }

    static of<T>(subject: T): Optional<T>
    {
        if (subject == null)
            throw new NullPointerException("subject is null");

        return new Optional<T>(subject);
    }

    static ofNullable<T>(subject: T): Optional<T>
    {
        return new Optional<T>(subject);
    }

    static empty<T>(): Optional<T>
    {
        return new Optional<T>(null);
    }
}