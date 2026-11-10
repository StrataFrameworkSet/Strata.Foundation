//////////////////////////////////////////////////////////////////////////////
// OwnedCloseable.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Objects;

public
class OwnedCloseable<T extends AutoCloseable>
    implements AutoCloseable
{
    private final T       closeable;
    private final boolean owner;

    public
    OwnedCloseable(T closeable,boolean owner)
    {
        this.closeable = closeable;
        this.owner = owner;
    }

    @Override
    public void
    close() throws Exception
    {
        if (owner && Objects.nonNull(closeable))
            closeable.close();
    }

    public T
    get() { return closeable; }

    public static <T extends AutoCloseable> OwnedCloseable<T>
    ofOwned(T closeable)
    {
        return new OwnedCloseable<T>(closeable,true);
    }

    public static <T extends AutoCloseable> OwnedCloseable<T>
    ofBorrowed(T closeable)
    {
        return new OwnedCloseable<T>(closeable,false);
    }
}

//////////////////////////////////////////////////////////////////////////////
