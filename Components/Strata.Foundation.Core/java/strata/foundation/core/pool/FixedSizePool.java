//////////////////////////////////////////////////////////////////////////////
// FixedSizePool.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;

public abstract
class FixedSizePool<T extends IPoolable<T,P>,P extends IPool<T,P>>
    implements IPool<T,P>
{
    private final BlockingQueue<T> itsPoolables;
    private final Supplier<T>      itsSupplier;

    protected
    FixedSizePool(int size,Supplier<T> supplier)
    {
        itsPoolables = new ArrayBlockingQueue<>(size);
        itsSupplier = supplier;
    }

    @Override
    public T
    checkOut()
    {
        try
        {
            T poolable =
                itsPoolables.size() > 0
                    ? itsPoolables.take()
                    : createAndTake();

            return poolable;
        }
        catch (InterruptedException e)
        {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean
    checkIn(T poolable)
    {
        return
            poolable.getPool() == this &&
                itsPoolables.offer(poolable);
    }

    @Override
    public P
    clear()
    {
        itsPoolables
            .stream()
            .forEach(poolable ->
            {
                try
                {
                    poolable.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });
        return getSelf();
    }

    @Override
    public int
    getCapacity()
    {
        return itsPoolables.size() + itsPoolables.remainingCapacity();
    }

    @Override
    public int
    getAvailability()
    {
        return itsPoolables.remainingCapacity();
    }

    @Override
    public boolean
    hasAvailability()
    {
        return getAvailability() > 0;
    }

    @Override
    public boolean
    isInPool(T poolable)
    {
        return poolable.getPool() == this;
    }

    @Override
    public boolean
    isCheckedIn(T poolable)
    {
        return
            isInPool(poolable)
                ? itsPoolables.contains(poolable)
                : false;
    }

    @Override
    public boolean
    isCheckedOut(T poolable)
    {
        return
            isInPool(poolable)
                ? !itsPoolables.contains(poolable)
                : false;
    }

    public FixedSizePool<T,P>
    addToPool(T poolable)
    {
        if (hasAvailability())
            itsPoolables.offer(poolable.setPool(getSelf()));

        return this;
    }

    protected T
    createAndTake()
        throws InterruptedException
    {
        T poolable = itsSupplier.get();


        addToPool(poolable);
        return itsPoolables.take();
    }

    protected P
    getSelf() { return (P)this; }
}

//////////////////////////////////////////////////////////////////////////////
