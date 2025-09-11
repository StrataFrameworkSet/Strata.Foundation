//////////////////////////////////////////////////////////////////////////////
// Operation.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;

import strata.foundation.core.reflect.TypeLiteral;

import java.lang.annotation.Annotation;

public
class Operation
    implements AutoCloseable
{
    private final IInjector  itsInjector;

    public
    Operation(IInjector injector)
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
        return itsInjector.getInstance(type);
    }

    public <T> T
    getInstance(Class<T> type,Class<? extends Annotation> annotationType)
    {
        return itsInjector.getInstance(type,annotationType);
    }

    public <T> T
    getInstance(TypeLiteral<T> type,Class<? extends Annotation> annotationType)
    {
        return itsInjector.getInstance(type,annotationType);
    }

    public <T> T
    getInstance(Class<T> type,Annotation annotation)
    {
        return itsInjector.getInstance(type,annotation);
    }

    public <T> T
    getInstance(TypeLiteral<T> type,Annotation annotation)
    {
        return itsInjector.getInstance(type,annotation);
    }

    public <T> T
    getInstance(Class<T> type,String name)
    {
        return itsInjector.getInstance(type,name);
    }

    public <T> T
    getInstance(TypeLiteral<T> type,String name)
    {
        return itsInjector.getInstance(type,name);
    }

    @Override
    public void
    close()
    {
        OperationContext.endScope();
    }
}

//////////////////////////////////////////////////////////////////////////////
