/// ///////////////////////////////////////////////////////////////////////////
// ExpendableContext.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.io.Serializable;

class ExpendableContext<T>
    implements Serializable
{
    private final T   value;
    private final int allowed;
    private int       remaining;

    public
    ExpendableContext(T value,int allowed)
    {
        this.value = value;
        this.allowed = allowed;
        this.remaining = this.allowed;
    }

    public ExpendableContext<T>
    decrementRemaining()
    {
        --remaining;
        return this;
    }

    public T
    getValue() { return value; }

    public int
    getAllowed() { return allowed; }

    public int
    getRemaining() { return remaining; }

    public boolean
    isExpended()
    {
        return remaining <= 0;
    }
}

//////////////////////////////////////////////////////////////////////////////
