//////////////////////////////////////////////////////////////////////////////
// TestModule.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;

import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        bind(new TypeLiteral<List<String>>() {})
            .toProvider(() -> List.of("one", "two", "three"))
            .in(Scopes.SINGLETON);

        bind(new TypeLiteral<Optional<List<String>>>() {})
            .toProvider(() -> Optional.of(List.of("aaa", "bbb", "ccc")))
            .in(Scopes.SINGLETON);
    }
}

//////////////////////////////////////////////////////////////////////////////
