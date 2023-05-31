//////////////////////////////////////////////////////////////////////////////
// AbstractModule.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;


import com.google.inject.Scope;

public abstract
class AbstractModule
    extends com.google.inject.AbstractModule
{
    private static Scope defaultScope = new GuiceThreadScope();

    public static void
    setDefaultScope(Scope scope)
    {
        defaultScope = scope;
    }

    public Scope
    getDefaultScope()
    {
        return defaultScope;
    }
}

//////////////////////////////////////////////////////////////////////////////
