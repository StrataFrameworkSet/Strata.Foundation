export
interface ICompletionStage<T>
{
    thenApply<U>(func: (input: T) => U): ICompletionStage<U>;

    thenCompose<U>(func: (input: T) => ICompletionStage<U>): ICompletionStage<U>;

    thenAccept(consumer: (result: T) => void,handler?: (error: Error) => void): ICompletionStage<T>;

    exceptionally(func: (error: Error) => T): ICompletionStage<T>;

    subscribe(consumer?: (result: T) => void,handler?: (error: Error) => void): void

}