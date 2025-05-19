/// ///////////////////////////////////////////////////////////////////////////
// SpringInjector.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import jakarta.inject.Named;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import strata.foundation.core.container.Pair;
import strata.foundation.core.inject.IInjector;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Objects;

public
class SpringInjector
    implements IInjector,ApplicationContextAware
{
    private ApplicationContext                context;
    private Map<Pair<Class<?>,String>,String> typeQualifierToBeanNames;

    public
    SpringInjector()
    {
        context = null;
    }

    public
    SpringInjector(ApplicationContext context)
    {
        Objects.requireNonNull(context,"Context cannot be null");
        this.context = context;
    }

    @Override
    public <T> T
    getInstance(Class<T> type)
    {
        return context.getBean(type);
    }

    @Override
    public <T> T
    getInstance(Class<T> type,Class<? extends Annotation> annotationType)
    {
        return context.getBean(type,annotationType.getSimpleName());
    }

    @Override
    public <T> T
    getInstance(Class<T> type,Annotation annotation)
    {
        if (annotation instanceof Qualifier qualifier)
            return getInstance(type,qualifier.value());

        if (annotation instanceof Named named)
            return getInstance(type,named.value());

        return getInstance(type,annotation.annotationType());
    }

    @Override
    public <T> T
    getInstance(Class<T> type,String name)
    {
        try
        {
            return context.getBean(name,type);
        }
        catch (Exception e)
        {
            String beanName =
                typeQualifierToBeanNames.get(Pair.create(type,name));

            return
                beanName != null
                    ? context.getBean(beanName,type)
                    : context.getBean(type);
        }
    }

    @Override
    public void
    setApplicationContext(ApplicationContext context) throws BeansException
    {
        this.context = context;
        this.typeQualifierToBeanNames =
            new BeanInspector(context).getQualifiedBeanNames();
    }
}

//////////////////////////////////////////////////////////////////////////////
