/// ///////////////////////////////////////////////////////////////////////////
// SerializableSortedSet.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public
class SerializableSortedSet<T extends Comparable<? super T>>
    extends TreeSet<T>
    implements ISortedSet<T>
{
    public
    SerializableSortedSet()
    {
        super();
    }

    public
    SerializableSortedSet(Comparator<? super T> comparator)
    {
        super(comparator);
    }

    public
    SerializableSortedSet(Collection<? extends T> c)
    {
        super(c);
    }

    public
    SerializableSortedSet(SortedSet<T> s)
    {
        super(s);
    }

    public static <T extends Comparable<? super T>> SerializableSortedSet<T>
    of(Collection<? extends T> c)
    {
        return new SerializableSortedSet<>(c);
    }

    public static <T extends Comparable<? super T>> SerializableSortedSet<T>
    ofIterable(Iterable<T> iterable)
    {
        SerializableSortedSet<T> set = new SerializableSortedSet<>();

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

        for (T item: this)
            out.writeObject(item);
    }

    @SuppressWarnings("unchecked")
    @Serial
    private void
    readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        int size = in.readInt();

        for (int i = 0; i < size; i++)
        {
            T item = (T)in.readObject();
            add(item);
        }
    }
}

//////////////////////////////////////////////////////////////////////////////
