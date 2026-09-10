//////////////////////////////////////////////////////////////////////////////
// IPoolable.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.pool;

/**
 * <p>
 * An object managed by an {@link IPool}, capable of being returned to its
 * owning pool automatically when closed via {@link AutoCloseable}.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * try (Connection connection = pool.checkOut())
 * {
 *     // use connection; it is returned to the pool on close
 * }
 * </pre>
 *
 * @param <T> - the concrete poolable type implementing this
 * interface
 * @param <P> - the type of pool that owns instances of this
 * poolable
 */
public
interface IPoolable<T extends IPoolable<T,P>,P extends IPool<T,P>>
    extends AutoCloseable
{
    T
    setPool(P pool);

    P
    getPool();
}

//////////////////////////////////////////////////////////////////////////////
