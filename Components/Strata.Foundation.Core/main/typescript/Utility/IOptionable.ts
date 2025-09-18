import {ISupplier} from "./ISupplier";
import {IConsumerOrLambda} from "./LambdaConsumer";
import {IFunctionOrLambda} from "./LambdaFunction";
import {ISupplierOrLambda} from "./LambdaSupplier";
import {IRunnableOrLambda} from "./LambdaRunnable";
import {IPredicateOrLambda} from "./LambdaPredicate";

export
interface IOptionable<T>
    extends ISupplier<T>
{
    ifPresent(consumer: IConsumerOrLambda<T>): void;

    ifPresentOrElse<U>(
        presentAction: IFunctionOrLambda<T,U>,
        emptyAction: ISupplierOrLambda<U>): U;

    ifPresentOrElseNoReturn(
        consumer: IConsumerOrLambda<T>,
        action: IRunnableOrLambda): void;

    or(supplier: ISupplierOrLambda<IOptionable<T>>): IOptionable<T>;

    orElse(alternative: T): T;

    orElseGet(supplier: ISupplierOrLambda<T>): T;

    orElseThrow(error: Error): T;

    orElseGetThrow(supplier: ISupplierOrLambda<Error>): T;

    filter(predicate: IPredicateOrLambda<T>): IOptionable<T>;

    map<U>(mapper: IFunctionOrLambda<T,U>): IOptionable<U>;

    flatMap<U>(mapper: IFunctionOrLambda<T,IOptionable<U>>): IOptionable<U>;

    isPresent(): boolean;

    isEmpty(): boolean;

}