//////////////////////////////////////////////////////////////////////////////
// IPool.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.pool;

/**
 * <p>
 * An <a href="https://en.wikipedia.org/wiki/Object_pool_pattern">Object pool
 * pattern</a> abstraction that manages check-out and check-in of a fixed set
 * of {@link IPoolable} instances, avoiding the cost of repeatedly creating
 * and destroying expensive objects.
 * </p>
 * <h4>Type Parameters</h4>
 * <ul>
 * <li>{@code <T>} - the type of poolable object managed by this pool</li>
 * <li>{@code <P>} - the concrete pool type, used so poolables can reference
 * the pool that owns them</li>
 * </ul>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * IPool&lt;Connection,ConnectionPool&gt; pool = ...;
 *
 * Connection connection = pool.checkOut();
 * try
 * {
 *     // use connection
 * }
 * finally
 * {
 *     pool.checkIn(connection);
 * }
 * </pre>
 * </p>
 */
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