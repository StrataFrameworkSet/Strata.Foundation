//////////////////////////////////////////////////////////////////////////////
// IUnboundedStreamSink.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.stream;

import java.io.Serializable;
import java.util.function.Consumer;

public
interface IUnboundedStreamSink<T>
    extends Consumer<IUnboundedStream<T>>, Serializable {}

//////////////////////////////////////////////////////////////////////////////