/// ///////////////////////////////////////////////////////////////////////////
// IReceiver.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.Optional;
import java.util.function.Consumer;

public
interface IReceiver<T,C extends Consumer<T>>
{
    IReceiver<T,C>
    setConsumer(C consumer);

    Optional<C>
    getConsumer();

    boolean
    hasConsumer();

    void
    startConsuming(C consumer) throws StartFailedException;

    void
    startConsuming() throws StartFailedException;

    void
    stopConsuming();

    boolean
    isConsuming();
}

//////////////////////////////////////////////////////////////////////////////