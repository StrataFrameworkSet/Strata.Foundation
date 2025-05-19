/// ///////////////////////////////////////////////////////////////////////////
// BeanInspector.java
/// ///////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import jakarta.inject.Named;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.MergedAnnotations.SearchStrategy;
import strata.foundation.core.container.Pair;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

public
class BeanInspector
{
    private final ApplicationContext context;

    public BeanInspector(ApplicationContext context)
    {
        this.context = context;
    }

    public Map<Pair<Class<?>,String>,String>
    getQualifiedBeanNames()
    {
        ConfigurableListableBeanFactory factory =
            (ConfigurableListableBeanFactory)
                context.getAutowireCapableBeanFactory();
        Map<Pair<Class<?>,String>,String> qualifiedBeanNames = new HashMap<>();

        Arrays
            .stream(context.getBeanDefinitionNames())
            .map(
                beanName ->
                    Pair.create(beanName,factory.getBeanDefinition(beanName)))
            .map(
                nameAndDefinition ->
                    getTypesAndQualifiers(
                        factory,
                        nameAndDefinition.getFirst(),
                        nameAndDefinition.getSecond()))
            .flatMap(entries -> entries.stream())
            .forEach(
                entry ->
                    qualifiedBeanNames.put(entry.getKey(),entry.getValue()));

        return qualifiedBeanNames;
    }

    protected List<Entry<Pair<Class<?>,String>,String>>
    getTypesAndQualifiers(
        ConfigurableListableBeanFactory factory,
        String                          beanName,
        BeanDefinition                  definition)
    {
        Class<?>     type = factory.getType(beanName);
        List<String> qualifiers = getQualifiers(factory,definition);
        List<Entry<Pair<Class<?>,String>,String>> entries = new ArrayList<>();

        getHierarchy(type)
            .forEach(
                t ->
                    qualifiers.forEach(
                        qualifier ->
                            entries.add(
                                Map.entry(
                                    Pair.create(t,qualifier),beanName))));

        return entries;
    }

    protected String
    getQualifier(
        ConfigurableListableBeanFactory factory,
        BeanDefinition                  definition)
    {
        String factoryMethodName = definition.getFactoryMethodName();
        String factoryBeanName = definition.getFactoryBeanName();

        if (factoryMethodName != null && factoryBeanName != null)
        {
            try
            {
                Class<?> factoryClass = factory.getType(factoryBeanName);

                if (factoryClass != null)
                {
                    for (Method method: factoryClass.getDeclaredMethods())
                    {
                        if (method.getName().equals(factoryMethodName))
                        {
                            Qualifier qualifier =
                                AnnotationUtils.findAnnotation(method,Qualifier.class);

                            if (qualifier != null)
                                return qualifier.value();

                            Named named =
                                AnnotationUtils.findAnnotation(method,Named.class);

                            if (named != null)
                                return named.value();

                        }
                    }
                }
            }
            catch (Exception e)
            {
            }
        }

        try
        {
            Class<?> beanClass = factory.getType(definition.getBeanClassName());
            if (beanClass != null)
            {
                Qualifier qualifier =
                    AnnotationUtils.findAnnotation(beanClass,Qualifier.class);

                if (qualifier != null)
                    return qualifier.value();
            }
        }
        catch (Exception e)
        {
        }

        return null;
    }


    protected List<String>
    getQualifiers(
        ConfigurableListableBeanFactory factory,
        BeanDefinition                  definition)
    {
        String factoryMethodName = definition.getFactoryMethodName();
        String factoryBeanName = definition.getFactoryBeanName();
        List<String> qualifiers = new ArrayList<>();

        if (factoryMethodName != null && factoryBeanName != null)
        {
            try
            {
                Class<?> factoryClass = factory.getType(factoryBeanName);

                if (factoryClass != null)
                {
                    for (Method method: factoryClass.getDeclaredMethods())
                    {
                        if (method.getName().equals(factoryMethodName))
                        {
                            MergedAnnotations
                                .from(method,SearchStrategy.TYPE_HIERARCHY)
                                .stream()
                                .map(MergedAnnotation::synthesize)
                                .filter(annotation -> isQualifier(annotation))
                                .filter(annotation -> hasValue(annotation))
                                .map(annotation -> getValue(annotation))
                                .forEach(qualifiers::add);

                            if (!qualifiers.isEmpty())
                                return qualifiers;
                        }
                    }
                }
            }
            catch (Exception e)
            {
                System.err.println("getQualifiers 1: " + e.getMessage());
            }
        }

        try
        {
            Class<?> beanClass = factory.getType(definition.getBeanClassName());

            if (beanClass != null)
            {
                MergedAnnotations
                    .from(beanClass,SearchStrategy.TYPE_HIERARCHY)
                    .stream()
                    .map(MergedAnnotation::synthesize)
                    .filter(annotation -> isQualifier(annotation))
                    .filter(annotation -> hasValue(annotation))
                    .map(annotation -> getValue(annotation))
                    .forEach(qualifiers::add);

                if (!qualifiers.isEmpty())
                    return qualifiers;
            }
        }
        catch (Exception e)
        {
            System.err.println("getQualifiers 2: " + e.getMessage());
        }

        return qualifiers;
    }

    public Set<Class<?>>
    getHierarchy(Class<?> type)
    {
        Set<Class<?>> hierarchy = new HashSet<>();
        Class<?>      current = type;

        while (current != null)
        {
            hierarchy.add(current);

            Arrays
                .stream(current.getInterfaces())
                .forEach(hierarchy::add);

            current = current.getSuperclass();
        }

        return hierarchy;
    }

    protected boolean
    isQualifier(Annotation annotation)
    {
        return
            Qualifier
                .class
                .isAssignableFrom(annotation.annotationType()) ||
            Named
                .class
                .isAssignableFrom(annotation.annotationType()) ||
            jakarta.inject.Qualifier
                .class
                    .isAssignableFrom(annotation.annotationType());
    }

    protected boolean
    hasValue(Annotation annotation)
    {
        return
            AnnotationUtils
                .getAnnotationAttributes(annotation)
                .containsKey("value");
    }

    protected String
    getValue(Annotation annotation)
    {
        return
            String.valueOf(
                AnnotationUtils.getValue(annotation,"value"));
    }
}

//////////////////////////////////////////////////////////////////////////////
