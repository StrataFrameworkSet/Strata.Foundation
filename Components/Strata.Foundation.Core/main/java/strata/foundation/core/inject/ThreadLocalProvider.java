// ##########################################################################
// # File Name:	ThreadLocalProvider.java
// ##########################################################################

package strata.foundation.core.inject;

import jakarta.inject.Provider;

public
class ThreadLocalProvider<T>
    implements Provider<T>
{
    private final Provider<T> itsSource;
    private ThreadLocal<T>    itsInstances;
    
    public
    ThreadLocalProvider(Provider<T> source)
    {
        itsSource = source;
        itsInstances =
            ThreadLocal.withInitial(() -> itsSource.get());
    }

    @Override
    public T 
    get()
    {
        return itsInstances.get();
    }

}

// ##########################################################################
