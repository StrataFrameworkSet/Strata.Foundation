//////////////////////////////////////////////////////////////////////////////
// Operation.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

public
class Operation
    implements AutoCloseable
{
    private final Injector  itsInjector;

    public
    Operation(Injector injector)
    {
        itsInjector = injector;
        OperationContext.beginScope();
    }

    public <T> T
    getInstance(Class<T> type)
    {
        return itsInjector.getInstance(type);
    }

    public <T> T
    getInstance(TypeLiteral<T> type)
    {
        return (T)itsInjector.getInstance(Key.get(type));
    }

    public <T> T
    getInstance(Key<T> key)
    {
        return itsInjector.getInstance(key);
    }

    @Override
    public void
    close()
    {
        OperationContext.endScope();
    }
}

//////////////////////////////////////////////////////////////////////////////
