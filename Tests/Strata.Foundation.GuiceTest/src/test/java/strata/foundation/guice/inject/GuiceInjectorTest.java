//////////////////////////////////////////////////////////////////////////////
// GuiceInjectorTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.guice.inject;

import com.google.inject.Guice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import strata.foundation.core.inject.IInjector;
import strata.foundation.core.reflect.TypeLiteral;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class GuiceInjectorTest
{
    private IInjector injector;

    @BeforeEach
    public void
    setUp()
    {
        injector =
            new GuiceInjector(
                Guice.createInjector(new TestModule()));
    }

    @Test
    public void
    testGetInstanceTypeLiteral()
    {
        List<String> strings =
            injector.getInstance(new TypeLiteral<>() {});
        Optional<List<String>> optional =
            injector.getInstance(new TypeLiteral<>() {});

        assertNotNull(strings);
        assertEquals(List.of("one", "two", "three"), strings);
        assertTrue(optional.isPresent());
        optional.ifPresent(list -> assertEquals(List.of("aaa", "bbb", "ccc"), list));
    }
}

//////////////////////////////////////////////////////////////////////////////
