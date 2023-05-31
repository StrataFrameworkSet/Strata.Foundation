//////////////////////////////////////////////////////////////////////////////
// TestModule.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;

import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;

import java.util.ArrayList;
import java.util.List;

public
class TestModule
    extends AbstractModule
{
    @Override
    protected void
    configure()
    {
        bind(IOperationProvider.class)
            .to(TransientOperationProvider.class)
            .in(Scopes.SINGLETON);

        bind(new TypeLiteral<List<Integer>>() {})
            .to(new TypeLiteral<ArrayList<Integer>>() {})
            .in(new OperationScope());
    }
}

//////////////////////////////////////////////////////////////////////////////
