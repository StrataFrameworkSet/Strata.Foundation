//////////////////////////////////////////////////////////////////////////////
// Operation.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;

import strata.foundation.core.reflect.TypeLiteral;

import java.lang.annotation.Annotation;

/**
 * <p>
 * An {@link AutoCloseable} scope wrapper for a unit-of-work operation.
 * Opening an {@code Operation} begins an {@link OperationContext} scope and
 * closing it ends that scope, while instance resolution is delegated to the
 * {@link IInjector} supplied at construction.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * try (Operation operation = new Operation(injector))
 * {
 *     IConfiguration configuration =
 *         operation.getInstance(IConfiguration.class);
 * }
 * </pre>
 * </p>
 */
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
