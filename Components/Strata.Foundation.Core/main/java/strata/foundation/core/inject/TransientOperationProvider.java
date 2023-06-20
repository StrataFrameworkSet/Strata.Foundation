//////////////////////////////////////////////////////////////////////////////
// TransientOperationProvider.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;

import com.google.inject.Inject;
import com.google.inject.Injector;

public
class TransientOperationProvider
    implements IOperationProvider
{
    private final Injector itsInjector;

    @Inject
    public
    TransientOperationProvider(Injector injector)
    {
        itsInjector = injector;
    }
    @Override
    public Operation
    get()
    {
        return new Operation(itsInjector);
    }
}

//////////////////////////////////////////////////////////////////////////////
