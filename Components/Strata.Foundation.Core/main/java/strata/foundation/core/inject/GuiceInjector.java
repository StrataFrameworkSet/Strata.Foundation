/// ///////////////////////////////////////////////////////////////////////////
// GuiceInjector.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import jakarta.inject.Inject;

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
    getInstance(Class<T> type,Class<? extends Annotation> annotationType)
    {
        return injector.getInstance(Key.get(type,annotationType));
    }

    @Override
    public <T> T
    getInstance(Class<T> type,Annotation annotation)
    {
        return injector.getInstance(Key.get(type,annotation));
    }

    @Override
    public <T> T
    getInstance(Class<T> type,String name)
    {
        return injector.getInstance(Key.get(type,Names.named(name)));
    }
}

//////////////////////////////////////////////////////////////////////////////
