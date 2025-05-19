/// ///////////////////////////////////////////////////////////////////////////
// ICompletableResource.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.resource;

import strata.foundation.core.concurrent.ICompletableBoolean;
import strata.foundation.core.concurrent.ICompletableVoid;

public
interface ICompletableResource
{
    ICompletableVoid
    open();

    ICompletableVoid
    close();

    ICompletableBoolean
    isOpen();

    ICompletableBoolean
    isClosed();
}

//////////////////////////////////////////////////////////////////////////////