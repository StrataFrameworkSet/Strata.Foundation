/// ///////////////////////////////////////////////////////////////////////////
// GuiceInjector.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import jakarta.inject.Inject;
import strata.foundation.core.reflect.TypeLiteral;

import java.lang.annotation.Annotation;

public
class GuiceInjector
    implements IInjector
{
    private final Injector injector;

    @Inject
    public
    GuiceInjector(Injector injector)
    {
        this.injector = injector;
    }

    @Override
    public <T> T
    getInstance(Class<T> type)
    {
        return injector.getInstance(type);
    }

    @Override
    public <T> T
    getInstance(TypeLiteral<T> type)
    {
        com.google.inject.TypeLiteral<T> typeLiteral = toGuiceTypeLiteral(type);
        Key<T>                           key = Key.get(typeLiteral);

        return injector.getInstance(key);
    }

    @Override
    public <T> T
    getInstance(Class<T> type,Class<? extends Annotation> annotationType)
    {
        return injector.getInstance(Key.get(type,annotationType));
    }

    @Override
    public <T> T
    getInstance(TypeLiteral<T> type,Class<? extends Annotation> annotationType)
    {
        com.google.inject.TypeLiteral<T> typeLiteral = toGuiceTypeLiteral(type);
        Key<T>                           key = Key.get(typeLiteral,annotationType);

        return injector.getInstance(key);
    }

    @Override
    public <T> T
    getInstance(Class<T> type,Annotation annotation)
    {
        return injector.getInstance(Key.get(type,annotation));
    }

    @Override
    public <T> T
    getInstance(TypeLiteral<T> type,Annotation annotation)
    {
        com.google.inject.TypeLiteral<T> typeLiteral = toGuiceTypeLiteral(type);
        Key<T>                           key = Key.get(typeLiteral,annotation);

        return injector.getInstance(key);
    }

    @Override
    public <T> T
    getInstance(Class<T> type,String name)
    {
        return injector.getInstance(Key.get(type,Names.named(name)));
    }

    @Override
    public <T> T
    getInstance(TypeLiteral<T> type,String name)
    {
        com.google.inject.TypeLiteral<T> typeLiteral = toGuiceTypeLiteral(type);
        Key<T>                           key = Key.get(typeLiteral,Names.named(name));

        return injector.getInstance(key);
    }

    @SuppressWarnings("unchecked")
    private static <T> com.google.inject.TypeLiteral<T>
    toGuiceTypeLiteral(TypeLiteral<T> type)
    {
        return
            (com.google.inject.TypeLiteral<T>)
                com.google.inject.TypeLiteral.get(type.getType());
    }
}

//////////////////////////////////////////////////////////////////////////////
