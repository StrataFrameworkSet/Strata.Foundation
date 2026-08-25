//////////////////////////////////////////////////////////////////////////////
// Consumed.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Objects;
import java.util.Optional;

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
    getThrowable() { return Optional.ofNullable(throwable); }

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
