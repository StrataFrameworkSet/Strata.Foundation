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
    private final IInjector itsInjector;

    @Inject
    public
    TransientOperationProvider(IInjector injector)
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
