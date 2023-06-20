//////////////////////////////////////////////////////////////////////////////
// OperationContext.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;

import strata.foundation.core.concurrent.ThreadLocalStack;

import java.util.HashMap;
import java.util.Map;

public
class OperationContext
{
    private static final ThreadLocalStack<Map<Class<?>,Object>> itsScopes =
        initialize();

    private
    OperationContext() {}

    public static synchronized void
    beginScope() { itsScopes.push(new HashMap<>()); }

    public static synchronized Map<Class<?>,Object>
    endScope() { return itsScopes.pop(); }

    public static <T> void
    setInstance(Class<T> type,T instance)
    {
        getCurrentScope().put(type,instance);
    }

    public static synchronized <T> T
    getInstance(Class<T> type)
    {
        return type.cast(getCurrentScope().get(type));
    }

    public static synchronized Map<Class<?>,Object>
    getCurrentScope() { return itsScopes.peek(); }

    public static synchronized boolean
    hasCurrentScope() { return !itsScopes.isEmpty(); }

    private static synchronized ThreadLocalStack<Map<Class<?>,Object>>
    initialize()
    {
        ThreadLocalStack<Map<Class<?>,Object>> scopes = new ThreadLocalStack<>();

        scopes.push(new HashMap<>()); // global scope
        return scopes;
    }
}

//////////////////////////////////////////////////////////////////////////////
