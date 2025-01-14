//////////////////////////////////////////////////////////////////////////////
// IKeySelector.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.stream;

import java.io.Serializable;

public
interface IKeySelector<K,T>
    extends Serializable
{
    K
    getKey(T subject);
}

//////////////////////////////////////////////////////////////////////////////