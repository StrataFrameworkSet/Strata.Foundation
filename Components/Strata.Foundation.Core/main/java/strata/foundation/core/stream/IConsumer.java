//////////////////////////////////////////////////////////////////////////////
// IConsumer.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.stream;

import java.io.Serializable;
import java.util.function.Consumer;

public
interface IConsumer<T>
    extends Consumer<T>, Serializable {}

//////////////////////////////////////////////////////////////////////////////