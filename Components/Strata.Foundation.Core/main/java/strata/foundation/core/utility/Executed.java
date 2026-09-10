//////////////////////////////////////////////////////////////////////////////
// Executed.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * Result produced by an operation that accepts no input and returns
 * no output, capturing the method name and, when the invocation
 * failed, the resulting {@link Throwable}. This covers any
 * fire-and-forget scenario — {@link Runnable} execution, cache
 * invalidation, connection resets, lifecycle hooks, or any void
 * method invoked without arguments.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * Executed result = Executed.of("refresh");
 *
 * if (result.isFailure())
 *     log(result.getException());
 * </pre>
 */
public
class Executed
{
    private final String    method;
    private       Throwable throwable;

    public
    Executed(String method)
    {
        this.method    = Objects.requireNonNull(method,"method");
        this.throwable = null;
    }

    public
    Executed(String method,Throwable throwable)
    {
        this.method = Objects.requireNonNull(method,"method");

        try
        {
            this.throwable =
                Objects.requireNonNull(throwable,"throwable");
        }
        catch (Exception e)
        {
            this.throwable = e;
        }
    }

    public String
    getMethod() { return method; }

    public Optional<Throwable>
    getException() { return Optional.ofNullable(throwable); }

    public boolean
    isSuccess() { return this.throwable == null; }

    public boolean
    isFailure() { return this.throwable != null; }

    public static Executed
    of(String method) { return new Executed(method); }

    public static Executed
    of(String method,Throwable throwable) { return new Executed(method,throwable); }
}

//////////////////////////////////////////////////////////////////////////////
