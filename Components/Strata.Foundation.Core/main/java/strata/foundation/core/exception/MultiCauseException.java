//////////////////////////////////////////////////////////////////////////////
// MultiCauseException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * An exception that aggregates multiple underlying causes into a single
 * {@link java.lang.RuntimeException}. Use this when an operation can fail
 * for more than one reason at once and the caller needs to be notified of
 * every failure rather than just the first one encountered.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * List&lt;Throwable&gt; causes = new ArrayList&lt;&gt;();
 * causes.add(new IllegalStateException("bad state"));
 * causes.add(new IllegalArgumentException("bad argument"));
 *
 * throw new MultiCauseException("Validation failed",causes);
 * </pre>
 * </p>
 */
public
class MultiCauseException
    extends RuntimeException
{
    private List<Throwable> itsCauses;

    public
    MultiCauseException()
    {
        this(
            "This exception is an aggregate of multiple exceptions",
            new ArrayList<>());
    }

    public
    MultiCauseException(Throwable... causes)
    {
        this(Arrays.asList(causes));
    }

    public
    MultiCauseException(String message,Throwable... causes)
    {
        this(message,Arrays.asList(causes));
    }

    public
    MultiCauseException(List<Throwable> causes)
    {
        this(
            "This exception is an aggregate of multiple exceptions",
            causes);
    }

    public
    MultiCauseException(String message,List<Throwable> causes)
    {
        super(message);
        itsCauses = causes;
    }
}

//////////////////////////////////////////////////////////////////////////////
