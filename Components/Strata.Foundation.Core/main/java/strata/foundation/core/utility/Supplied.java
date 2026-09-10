//////////////////////////////////////////////////////////////////////////////
// Supplied.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * Result produced by an operation that accepts no input but returns
 * an output value, capturing the method name and either the output
 * or the {@link Throwable} thrown while producing it. This covers
 * any produce-without-input scenario —
 * {@link java.util.function.Supplier} invocations, factory methods,
 * configuration lookups, or any no-argument method that returns a
 * value. Exactly one of output or exception is present;
 * {@link #isSuccess()} and {@link #isFailure()} indicate which
 * outcome occurred.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * Supplied&lt;String&gt; result = Supplied.of("loadConfig","value");
 *
 * if (result.isSuccess())
 *     System.out.println(result.getOutput().get());
 *
 * Supplied&lt;String&gt; failure =
 *     Supplied.of("loadConfig",new RuntimeException("boom"));
 * </pre>
 *
 * @param <O> the type of the output value produced on success
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
