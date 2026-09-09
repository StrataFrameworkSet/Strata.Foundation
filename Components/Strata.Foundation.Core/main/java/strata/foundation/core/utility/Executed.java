//////////////////////////////////////////////////////////////////////////////
// Executed.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * Captures the outcome of invoking a method that takes no distinguished
 * input, recording the name of the method that was invoked and, when the
 * invocation failed, the resulting {@link Throwable}.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * Executed result = Executed.of("refresh");
 *
 * if (result.isFailure())
 *     log(result.getException());
 * </pre>
 * </p>
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
