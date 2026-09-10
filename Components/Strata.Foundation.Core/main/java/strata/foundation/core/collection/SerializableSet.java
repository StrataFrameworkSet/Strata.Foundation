/// ///////////////////////////////////////////////////////////////////////////
// SerializableSet.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.*;

/**
 * <p>
 * Serializable implementation of {@link ISet} that extends
 * {@link java.util.HashSet} to combine standard set behavior
 * with Strata collection serialization support.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation from existing collection
 * ISet&lt;String&gt; set = SerializableSet.of(Set.of("a","b","c"));
 *
 * // Creation from iterable
 * ISet&lt;String&gt; set = SerializableSet.ofIterable(someIterable);
 *
 * // Direct construction
 * SerializableSet&lt;String&gt; set = new SerializableSet&lt;&gt;();
 * set.add("item");
 * </pre>
 *
 * @param <T> - element type
 */
public
class SerializableSet<T>
    extends HashSet<T>
    implements ISet<T>
{
    public
    SerializableSet()
    {
        super();
    }

    public
    SerializableSet(Collection<? extends T> c)
    {
        super(c);
    }

    public
    SerializableSet(SortedSet<T> s)
    {
        super(s);
    }


    public static <T> SerializableSet<T>
    of(Collection<? extends T> c)
    {
        return new SerializableSet<>(c);
    }

    public static <T> SerializableSet<T>
    ofIterable(Iterable<T> iterable)
    {
        SerializableSet<T> set = new SerializableSet<>();

        for (T item : iterable)
            set.add(item);

        return set;
    }

    @Serial
    private void
    writeObject(ObjectOutputStream out)
        throws IOException
    {
        out.writeInt(size());
        for (T item : this)
            out.writeObject(item);
    }

    @SuppressWarnings("unchecked")
    @Serial
    private void
    readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        int size = in.readInt();

        clear();

        for (int i = 0;i < size;++i)
            add((T)in.readObject());
    }

}

//////////////////////////////////////////////////////////////////////////////
