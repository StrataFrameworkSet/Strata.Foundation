/// ///////////////////////////////////////////////////////////////////////////
// IEventReceiverGroup.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

public
interface IEventReceiverGroup<
    E,
    L extends IEventListener<E>,
    R extends IEventReceiver<E,L>>
    extends Supplier<Set<R>>
{
    IEventReceiverGroup<E,L,R>
    insert(R receiver);

    IEventReceiverGroup<E,L,R>
    remove(R receiver);

    IEventReceiverGroup<E,L,R>
    clear();

    Set<R>
    get();

    boolean
    contains(R receiver);

    int
    size();

    void
    startListening();

    void
    stopListening();

    boolean
    isListening();

    Stream<R>
    stream();
}

//////////////////////////////////////////////////////////////////////////////