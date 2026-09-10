//////////////////////////////////////////////////////////////////////////////
// Mapped.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * Result produced by an operation that accepts an input and returns
 * an output, capturing the method name, the input value, and either
 * the resulting output or the {@link Throwable} thrown while
 * producing it. This covers any transform scenario —
 * {@link java.util.function.Function} applications, type
 * converters, serializers, parsers, or any method that maps one
 * value to another. Exactly one of output or exception is present;
 * {@link #isSuccess()} and {@link #isFailure()} indicate which
 * outcome occurred.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * Mapped&lt;String,Integer&gt; result =
 *     Mapped.of("parseInt","42",Integer.parseInt("42"));
 *
 * if (result.isSuccess())
 * {
 *     Integer value = result.getOutput().get();
 * }
 * </pre>
 *
 * @param <I> - the type of the input value passed to the operation
 * @param <O> - the type of the output value produced by the operation
 */
public
class Mapped<I,O>
{
    private final String    method;
    private final I         input;
    private       O         output;
    private       Throwable throwable;

    public
    Mapped(String method,I input,O output)
    {
        this.method = Objects.requireNonNull(method,"method");
        this.input  = Objects.requireNonNull(input,"input");

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
    Mapped(String method,I input,Throwable throwable)
    {
        this.method = Objects.requireNonNull(method,"method");
        this.input  = Objects.requireNonNull(input,"input");
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

    public I
    getInput() { return input; }

    public Optional<O>
    getOutput() { return Optional.ofNullable(output); }

    public Optional<Throwable>
    getException() { return Optional.ofNullable(throwable); }

    public boolean
    isSuccess() { return this.output != null; }

    public boolean
    isFailure() { return this.throwable != null; }

    public static <I,O> Mapped<I,O>
    of(String method,I input,O output) { return new Mapped<I,O>(method,input,output); }

    public static <I,O> Mapped<I,O>
    of(String method,I input,Throwable throwable) { return new Mapped<I,O>(method,input,throwable); }
}

//////////////////////////////////////////////////////////////////////////////
