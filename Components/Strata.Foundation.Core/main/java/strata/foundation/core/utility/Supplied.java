//////////////////////////////////////////////////////////////////////////////
// Supplied.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Objects;
import java.util.Optional;

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
    getThrowable() { return Optional.ofNullable(throwable); }

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
