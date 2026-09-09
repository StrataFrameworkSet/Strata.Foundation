//////////////////////////////////////////////////////////////////////////////
// IEventReceiverGroup.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * <p>
 * Group of {@link IEventReceiver} instances exposed as a
 * {@link java.util.function.Supplier} of {@link java.util.Set},
 * enabling coordinated management of multiple receivers.
 * </p>
 * <p>
 * <h4>Type Parameters</h4>
 * <ul>
 * <li>{@code <E>} - event type</li>
 * <li>{@code <L>} - listener type</li>
 * <li>{@code <R>} - receiver type</li>
 * </ul>
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Access grouped receivers
 * IEventReceiverGroup&lt;String,IEventListener&lt;String&gt;,
 *     IEventReceiver&lt;String,IEventListener&lt;String&gt;&gt;&gt; group = ...;
 * Set&lt;IEventReceiver&lt;String,IEventListener&lt;String&gt;&gt;&gt; receivers = group.get();
 * </pre>
 * </p>
 */
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