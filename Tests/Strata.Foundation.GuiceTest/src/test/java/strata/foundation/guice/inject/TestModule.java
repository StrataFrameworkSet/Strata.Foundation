//////////////////////////////////////////////////////////////////////////////
// TestModule.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.guice.inject;

import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import strata.foundation.core.inject.GuiceInjector;
import strata.foundation.core.inject.IInjector;
import strata.foundation.core.inject.IOperationProvider;
import strata.foundation.core.inject.TransientOperationProvider;

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
        bind(IInjector.class)
            .to(GuiceInjector.class)
            .in(Scopes.SINGLETON);

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
