export
class ExpendableContext<T>
{
    private readonly value: T;
    private readonly allowed: number;
    private          remaining: number;

    public constructor(value:T, limit:number)
    {
        this.value = value;
        this.allowed = limit;
        this.remaining = limit;
    }

    public decrementRemaining(): ExpendableContext<T>
    {
        --this.remaining;
        return this;
    }

    public getValue(): T
    {
        return this.value;
    }

    public getAllowed(): number
    {
        return this.allowed;
    }

    public getRemaining(): number
    {
        return this.remaining;
    }

    public isExpended(): boolean
    {
        return this.remaining <= 0;
    }

}