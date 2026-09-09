//////////////////////////////////////////////////////////////////////////////
// Supplied.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * Represents the outcome of invoking a named method that either produces an
 * output value or fails with a {@link Throwable}, capturing the method name
 * alongside whichever of the two occurred. Exactly one of the output or the
 * exception is expected to be non-null; {@link #isSuccess()} and
 * {@link #isFailure()} report which case applies, and {@link #getOutput()}
 * and {@link #getException()} expose the result as an {@link Optional}.
 * </p>
 * <h4>Type Parameter</h4>
 * {@code <O>} - the type of the output value produced on success
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * Supplied&lt;String&gt; result = Supplied.of("loadConfig", "value");
 * if (result.isSuccess())
 *     System.out.println(result.getOutput().get());
 *
 * Supplied&lt;String&gt; failure = Supplied.of("loadConfig", new RuntimeException("boom"));
 * </pre>
 * </p>
 */
public
class Supplied<O>
{
    private final String    method;
    private       O         output;
    private       Throwable throwable;

    public
    Supplied(String method,O output)
    {
        this.method = Objects.requireNonNull(method,"method");

        try
        {
            this.output =
                Objects.requireNonNull(output,"output");
            this.throwable = null;
        }
        catch (Exception e)
        {
            this.output    = null;
            this.throwable = e;
        }
    }

    public
    Supplied(String method,Throwable throwable)
    {
        this.method = Objects.requireNonNull(method,"method");
        this.output = null;

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

    public Optional<O>
    getOutput() { return Optional.ofNullable(output); }

    public Optional<Throwable>
    getException() { return Optional.ofNullable(throwable); }

    public boolean
    isSuccess() { return this.output != null; }

    public boolean
    isFailure() { return this.throwable != null; }

    public static <O> Supplied<O>
    of(String method,O output) { return new Supplied<O>(method,output); }

    public static <O> Supplied<O>
    of(String method,Throwable throwable) { return new Supplied<O>(method,throwable); }
}

//////////////////////////////////////////////////////////////////////////////
