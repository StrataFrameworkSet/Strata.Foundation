//////////////////////////////////////////////////////////////////////////////
// ThreadScope.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public
class ThreadScope
    implements Scope
{
    @Override
    public Object
    get(String name,ObjectFactory<?> objectFactory)
    {
        return objectFactory.getObject();
    }

    @Override
    public Object
    remove(String name)
    {
        return null;
    }

    @Override
    public void
    registerDestructionCallback(String name,Runnable callback)
    {

    }

    @Override
    public Object
    resolveContextualObject(String key)
    {
        return null;
    }

    @Override
    public String
    getConversationId()
    {
        return null;
    }
}

//////////////////////////////////////////////////////////////////////////////
