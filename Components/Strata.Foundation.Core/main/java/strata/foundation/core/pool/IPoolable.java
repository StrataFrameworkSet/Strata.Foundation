//////////////////////////////////////////////////////////////////////////////
// IPoolable.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.pool;

/**
 * <p>
 * An object managed by an {@link IPool}, capable of being returned to its
 * owning pool automatically when closed via {@link AutoCloseable}.
 * </p>
 * <h4>Type Parameters</h4>
 * <ul>
 * <li>{@code <T>} - the concrete poolable type implementing this
 * interface</li>
 * <li>{@code <P>} - the type of pool that owns instances of this
 * poolable</li>
 * </ul>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * try (Connection connection = pool.checkOut())
 * {
 *     // use connection; it is returned to the pool on close
 * }
 * </pre>
 * </p>
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