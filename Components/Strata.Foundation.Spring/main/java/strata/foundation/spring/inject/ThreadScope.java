//////////////////////////////////////////////////////////////////////////////
// ThreadScope.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.HashMap;
import java.util.Map;

public
class ThreadScope
    implements Scope
{
    public final static String THREAD_KEY = "THREAD";
    public final static String THREAD_ID_KEY = "THREAD_ID";
    public final static String THREAD_NAME_KEY = "THREAD_NAME";

    private final ThreadLocal<Map<String,Object>> threadScope =
        ThreadLocal.withInitial(HashMap::new);

    @Override
    public Object
    get(String name,ObjectFactory<?> factory)
    {
        Map<String,Object> scope = threadScope.get();

        if (!scope.containsKey(name))
            scope.put(name, factory.getObject());

        return scope.get(name);
    }

    @Override
    public Object
    remove(String name)
    {
        Map<String,Object> scope = threadScope.get();

        return scope.remove(name);
    }

    @Override
    public void
    registerDestructionCallback(String name, Runnable callback) {}

    @Override
    public Object
    resolveContextualObject(String key)
    {
        if (THREAD_KEY.equals(key))
            return Thread.currentThread();
        else if (THREAD_ID_KEY.equals(key))
            return Thread.currentThread().threadId();
        else if (THREAD_NAME_KEY.equals(key))
            return Thread.currentThread().getName();

        return null;
    }

    @Override
    public String
    getConversationId()
    {
        return Thread.currentThread().getName();
    }

    public void
    clear()
    {
        threadScope.remove();
    }
}

//////////////////////////////////////////////////////////////////////////////
