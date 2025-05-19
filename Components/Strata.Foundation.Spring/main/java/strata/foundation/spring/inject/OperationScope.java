/// ///////////////////////////////////////////////////////////////////////////
// OperationScope.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public
class OperationScope
    implements Scope
{

    @Override
    public Object
    get(String name,ObjectFactory<?> factory)
    {
        Object instance = OperationContext.getInstance(name);

        if (instance == null)
        {
            instance = factory.getObject();
            OperationContext.setInstance(name, instance);
        }

        return instance;
    }

    @Override
    public Object
    remove(String name)
    {
        return OperationContext.removeInstance(name);
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
