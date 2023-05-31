//////////////////////////////////////////////////////////////////////////////
// IPool.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.pool;

public
interface IPool<T extends IPoolable<T,P>,P extends IPool<T,P>>
{
    T
    checkOut();

    boolean
    checkIn(T poolable);

    P
    clear();

    int
    getCapacity();

    int
    getAvailability();

    boolean
    hasAvailability();

    boolean
    isInPool(T poolable);

    boolean
    isCheckedIn(T poolable);

    boolean
    isCheckedOut(T poolable);
}

//////////////////////////////////////////////////////////////////////////////