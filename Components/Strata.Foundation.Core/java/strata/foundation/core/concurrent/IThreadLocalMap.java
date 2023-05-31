//////////////////////////////////////////////////////////////////////////////
// IThreadLocalMap.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public
interface IThreadLocalMap<T>
{
    IThreadLocalMap<T>
    insert(T value);

    IThreadLocalMap<T>
    remove(T value);

    IThreadLocalMap<T>
    remove();

    Optional<T>
    get();

    Collection<T>
    getAll();

    Stream<T>
    getAllAsStream();
}

//////////////////////////////////////////////////////////////////////////////