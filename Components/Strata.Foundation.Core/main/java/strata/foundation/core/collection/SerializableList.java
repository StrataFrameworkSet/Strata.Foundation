//////////////////////////////////////////////////////////////////////////////
// SerializableList.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;

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
