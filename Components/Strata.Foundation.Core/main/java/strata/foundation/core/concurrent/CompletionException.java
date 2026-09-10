//////////////////////////////////////////////////////////////////////////////
// CompletionException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

/**
 * <p>
 * Thrown when an asynchronous completion fails, wrapping the
 * underlying cause of the failure.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Thrown by CompletedResult on failure
 * try
 * {
 *     CompletedResult&lt;String&gt; result = CompletedResult.of(new RuntimeException("fail"));
 *     result.get(); // throws CompletionException
 * }
 * catch (CompletionException e)
 * {
 *     Throwable cause = e.getCause();
 * }
 * </pre>
 */
public
class CompletionException
    extends RuntimeException
{
    public
    CompletionException(String message)
    {
        super(message);
    }

    public
    CompletionException(String message,Throwable cause)
    {
        super(message,cause);
    }

    public
    CompletionException(Throwable cause)
    {
        super(cause);
    }
}

//////////////////////////////////////////////////////////////////////////////

