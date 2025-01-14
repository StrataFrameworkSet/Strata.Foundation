//////////////////////////////////////////////////////////////////////////////
// Holder.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.container;

public
class Holder<T>
{
    private T itsItem;

    public
    Holder() { itsItem = null; }

    public Holder<T>
    setItem(T item)
    {
        itsItem = item;
        return this;
    }

    public T
    getItem() { return itsItem; }
}

//////////////////////////////////////////////////////////////////////////////
