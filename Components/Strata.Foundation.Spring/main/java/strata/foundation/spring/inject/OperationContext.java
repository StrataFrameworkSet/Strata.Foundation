//////////////////////////////////////////////////////////////////////////////
// OperationContext.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import strata.foundation.core.concurrent.ThreadLocalStack;

import java.util.HashMap;
import java.util.Map;

public
class OperationContext
{
    private static final ThreadLocalStack<Map<String,Object>> itsScopes =
        initialize();

    private OperationContext() {}

    public static synchronized void
    beginScope() { itsScopes.push(new HashMap<>()); }

    public static synchronized Map<String,Object>
    endScope() { return itsScopes.pop(); }

    public static <T> void
    setInstance(String type,T instance)
    {
        getCurrentScope().put(type,instance);
    }

    public static synchronized Object
    getInstance(String type)
    {
        return getCurrentScope().get(type);
    }

    public static synchronized Object
    removeInstance(String type)
    {
        return getCurrentScope().remove(type);
    }

    public static synchronized Map<String,Object>
    getCurrentScope() { return itsScopes.peek(); }

    public static synchronized boolean
    hasCurrentScope() { return !itsScopes.isEmpty(); }

    private static synchronized ThreadLocalStack<Map<String,Object>>
    initialize()
    {
        ThreadLocalStack<Map<String,Object>> scopes = new ThreadLocalStack<>();

        scopes.push(new HashMap<>()); // global scope
        return scopes;
    }
}

//////////////////////////////////////////////////////////////////////////////
