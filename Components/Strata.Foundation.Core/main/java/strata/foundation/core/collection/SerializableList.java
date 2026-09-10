//////////////////////////////////////////////////////////////////////////////
// SerializableList.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>
 * Serializable implementation of {@link IList} that extends
 * {@link java.util.ArrayList} to combine standard list behavior
 * with Strata collection serialization support.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation from existing collection
 * IList&lt;String&gt; list = SerializableList.of(List.of("a","b","c"));
 *
 * // Creation from iterable
 * IList&lt;String&gt; list = SerializableList.ofIterable(someIterable);
 *
 * // Direct construction
 * SerializableList&lt;String&gt; list = new SerializableList&lt;&gt;();
 * list.add("item");
 * </pre>
 *
 * @param <T> element type
 */
public
class SerializableList<T>
    extends ArrayList<T>
    implements IList<T>
{
    public
    SerializableList() {}

    public
    SerializableList(int initialCapacity)
    {
        super(initialCapacity);
    }

    public
    SerializableList(Collection<? extends T> c)
    {
        super(c);
    }

    public static <T> SerializableList<T>
    of(Collection<? extends T> c)
    {
        return new SerializableList<>(c);
    }

    public static <T> SerializableList<T>
    ofIterable(Iterable<T> iterable)
    {
        SerializableList<T> list = new SerializableList<>();

        for (T item : iterable)
            list.add(item);

        return list;
    }

    private void
    writeObject(ObjectOutputStream out)
        throws IOException
    {
        out.writeInt(size());
        for (T item : this)
            out.writeObject(item);
    }

    @SuppressWarnings("unchecked")
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
