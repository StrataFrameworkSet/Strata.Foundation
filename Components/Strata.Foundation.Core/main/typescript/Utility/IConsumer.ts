export
interface IConsumer<T>
{
    accept(value: T): void;
}