//////////////////////////////////////////////////////////////////////////////
// IFunction.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.stream;

import java.io.Serializable;
import java.util.function.Function;

public
interface IFunction<I,O>
    extends Function<I,O>, Serializable {}

//////////////////////////////////////////////////////////////////////////////