//////////////////////////////////////////////////////////////////////////////
// Mapped.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * Represents the result of invoking a method that maps an input value to
 * an output value, capturing the method's name, the input that was mapped,
 * and either the resulting output or the {@link Throwable} thrown while
 * producing it. Exactly one of output or throwable is present, and
 * {@link #isSuccess()} / {@link #isFailure()} indicate which outcome
 * occurred.
 * </p>
 * <h4>Type Parameters</h4>
 * <ul>
 * <li>{@code <I>} - the type of the input value that was mapped</li>
 * <li>{@code <O>} - the type of the output value produced by the mapping</li>
 * </ul>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * Mapped&lt;String,Integer&gt; result =
 *     Mapped.of("parseInt","42",Integer.parseInt("42"));
 *
 * if (result.isSuccess())
 * {
 *     Integer value = result.getOutput().get();
 * }
 * </pre>
 * </p>
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
