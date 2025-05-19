//////////////////////////////////////////////////////////////////////////////
// Operation.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import strata.foundation.core.inject.IInjector;

import java.lang.annotation.Annotation;
import java.util.Objects;

public
class Operation
    implements ApplicationContextAware,AutoCloseable
{
    private IInjector injector;

    public
    Operation()
    {
        injector = null;
        OperationContext.beginScope();
    }

    public
    Operation(IInjector injector)
    {
        Objects.requireNonNull(injector,"Injector cannot be null");
        this.injector = injector;
        OperationContext.beginScope();
    }

    @Override
    public void
    setApplicationContext(ApplicationContext context)
        throws BeansException
    {
        this.injector = new SpringInjector(context);
    }

    @Override
    public void
    close()
    {
        OperationContext.endScope();
    }
    public <T> T
    getInstance(Class<T> type)
    {
        return injector.getInstance(type);
    }

    public <T> T
    getInstance(Class<T> type,Class<? extends Annotation> annotationType)
    {
        return injector.getInstance(type,annotationType);
    }

    public <T> T
    getInstance(Class<T> type,Annotation annotation)
    {
        return injector.getInstance(type,annotation);
    }

    public <T> T
    getInstance(Class<T> type,String name)
    {
        return injector.getInstance(type,name);
    }
}

//////////////////////////////////////////////////////////////////////////////
