//////////////////////////////////////////////////////////////////////////////
// BackoffStrategy.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

/**
 * <p>
 * Enumerates the strategies available to {@link IRetryExecutor} for
 * calculating the delay between successive retry attempts.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * IRetryExecutor executor =
 *     new BasicRetryExecutor().setBackoffStrategy(BackoffStrategy.EXPONENTIAL);
 * </pre>
 */
public
enum BackoffStrategy
{
    NONE,
    EXPONENTIAL,
    LINEAR
}

//////////////////////////////////////////////////////////////////////////////
