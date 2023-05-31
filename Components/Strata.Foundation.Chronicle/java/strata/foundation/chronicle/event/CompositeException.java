//////////////////////////////////////////////////////////////////////////////
// CompositeException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.chronicle.event;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public
class CompositeException
    extends RuntimeException
{
    private List<Exception> causes;

    public
    CompositeException()
    {
        causes = new ArrayList<>();
    }

    public CompositeException
    setCause(Exception cause)
    {
        causes.add(cause);
        return this;
    }

    public List<Exception>
    getCauses() { return causes; }

    public Stream<Exception>
    getCausesAsStream() { return causes.stream(); }

    public boolean
    hasCauses() { return !causes.isEmpty(); }
}

//////////////////////////////////////////////////////////////////////////////
