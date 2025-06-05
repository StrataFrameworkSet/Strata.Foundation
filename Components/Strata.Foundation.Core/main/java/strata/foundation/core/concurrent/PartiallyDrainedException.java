/// ///////////////////////////////////////////////////////////////////////////
// PartiallyDrainedException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.ArrayList;
import java.util.List;

public
class PartiallyDrainedException
    extends StoppedException
{
    private List<Object> drained;

    public
    PartiallyDrainedException(List<?> drained)
    {
        super(
            drained.isEmpty()
                ? "Queue was stopped."
                : "Queue was partially drained then stopped.");
        this.drained = new ArrayList<>(drained);
    }

    public
    PartiallyDrainedException(List<?> drained,Throwable cause)
    {
        super(
            drained.isEmpty()
                ? "Queue was stopped."
                : "Queue was partially drained then stopped.",
            cause);
        this.drained = new ArrayList<>(drained);
    }

    public <T> List<T>
    getDrained(Class<T> type)
    {
        return
            drained
                .stream()
                .filter(type::isInstance)
                .map(type::cast)
                .toList();
    }

    public <T> boolean
    hasDrained(Class<T> type)
    {
        return
            drained
                .stream()
                .anyMatch(type::isInstance);
    }
}

//////////////////////////////////////////////////////////////////////////////

