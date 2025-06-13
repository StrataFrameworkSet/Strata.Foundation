/// ///////////////////////////////////////////////////////////////////////////
// SpringInjector.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import jakarta.inject.Named;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import strata.foundation.core.container.Pair;
import strata.foundation.core.inject.IInjector;
import strata.foundation.core.reflect.TypeLiteral;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
    getInstance(TypeLiteral<T> type)
    {
        ResolvableType resolvableType = ResolvableType.forType(type.getType());
        String[]       names = context.getBeanNamesForType(resolvableType);

        if (names.length > 1)
            throw new NoUniqueBeanDefinitionException(
                resolvableType.resolve(),
                names.length,
                "Expected 1 bean but found " + names.length);

        if (names.length == 0)
            return null;

        return context.getBean(names[0],type.getRawType());
    }

    @Override
    public <T> T
    getInstance(Class<T> type,Class<? extends Annotation> annotationType)
    {
        try
        {
            Annotation annotation =
                annotationType
                    .getDeclaredConstructor()
                    .newInstance();

            return getInstance(type,annotation);
        }
        catch (BeansException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw
                new BeanInstantiationException(
                    type,
                    "Error constructing annotation",
                    e);
        }
    }

    @Override
    public <T> T
    getInstance(TypeLiteral<T> type,Class<? extends Annotation> annotationType)
    {
        try
        {
            Annotation annotation =
                annotationType
                    .getDeclaredConstructor()
                    .newInstance();

            return getInstance(type,annotation);
        }
        catch (BeansException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw
                new BeanInstantiationException(
                    type.getRawType(),
                    "Error constructing annotation",
                    e);
        }
    }

    @Override
    public <T> T
    getInstance(Class<T> type,Annotation annotation)
    {
        if (annotation instanceof Qualifier qualifier)
            return getInstance(type,qualifier.value());

        if (annotation instanceof Named named)
            return getInstance(type,named.value());

        return getInstance(type,annotation.toString());
    }

    @Override
    public <T> T
    getInstance(TypeLiteral<T> type,Annotation annotation)
    {
        if (annotation instanceof Qualifier qualifier)
            return getInstance(type,qualifier.value());

        if (annotation instanceof Named named)
            return getInstance(type,named.value());

        return getInstance(type,annotation.toString());
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
    public <T> T
    getInstance(TypeLiteral<T> type,String name)
    {
        ResolvableType resolvable = ResolvableType.forType(type.getType());
        List<String>   names = Arrays.asList(context.getBeanNamesForType(resolvable));

        try
        {
            if (!names.contains(name))
                throw new NoSuchElementException();

            return context.getBean(name,type.getRawType());
        }
        catch (Exception e)
        {
            String beanName =
                typeQualifierToBeanNames.get(Pair.create(type.getRawType(),name));

            return
                beanName != null && names.contains(beanName)
                    ? context.getBean(beanName,type.getRawType())
                    : context.getBean(type.getRawType());
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
