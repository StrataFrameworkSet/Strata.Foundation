export
interface IPredicate<T>
{
    test(value: T): boolean;
}