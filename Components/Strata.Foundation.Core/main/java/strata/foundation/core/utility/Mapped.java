//////////////////////////////////////////////////////////////////////////////
// Mapped.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Objects;
import java.util.Optional;

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
    getThrowable() { return Optional.ofNullable(throwable); }

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
