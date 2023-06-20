//////////////////////////////////////////////////////////////////////////////
// MultiCauseException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public
class MultiCauseException
    extends Exception
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
