import {IConsumer} from "./IConsumer";
import {ISupplier} from "./ISupplier";

export
class Holder<T>
    implements IConsumer<T>, ISupplier<T>
{
    private value: T;

    constructor()
    {
        this.value = null;
    }

    accept(value: T): void
    {
        this.value = value;
    }

    get(): T
    {
        return this.value;
    }

}