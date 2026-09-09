//////////////////////////////////////////////////////////////////////////////
// Consumed.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * Captures the outcome of invoking a {@link java.util.function.Consumer}
 * with a given input, recording the name of the method that was invoked,
 * the input value, and, when the invocation failed, the resulting
 * {@link Throwable}.
 * </p>
 * <h4>Type Parameter</h4>
 * {@code <I>} - the type of the input passed to the consumer
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * Consumed&lt;String&gt; result = Consumed.of("process",input);
 *
 * if (result.isFailure())
 *     log(result.getException());
 * </pre>
 * </p>
 */
public
class Consumed<I>
{
    private final String    method;
    private final I         input;
    private       Throwable throwable;

    public
    Consumed(String method,I input)
    {
        this.method    = Objects.requireNonNull(method,"method");
        this.input     = Objects.requireNonNull(input,"input");
        this.throwable = null;
    }

    public
    Consumed(String method,I input,Throwable throwable)
    {
        this.method = Objects.requireNonNull(method,"method");
        this.input  = Objects.requireNonNull(input,"input");

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

    public Optional<Throwable>
    getException() { return Optional.ofNullable(throwable); }

    public boolean
    isSuccess() { return this.throwable == null; }

    public boolean
    isFailure() { return this.throwable != null; }

    public static <I> Consumed<I>
    of(String method,I input) { return new Consumed<I>(method,input); }

    public static <I> Consumed<I>
    of(String method,I input,Throwable throwable) { return new Consumed<I>(method,input,throwable); }
}

//////////////////////////////////////////////////////////////////////////////
