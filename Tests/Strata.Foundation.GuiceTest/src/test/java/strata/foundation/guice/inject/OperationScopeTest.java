//////////////////////////////////////////////////////////////////////////////
// OperationScopeTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.guice.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.*;
import strata.foundation.core.inject.IOperationProvider;
import strata.foundation.core.inject.Operation;
import strata.foundation.core.reflect.TypeLiteral;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Tag("CommitStage")
public
class OperationScopeTest
{
    private IOperationProvider itsProvider;

    @BeforeEach
    public void
    setUp()
    {
        Injector injector = Guice.createInjector(new TestModule());

        itsProvider = injector.getInstance(IOperationProvider.class);
    }

    @AfterEach
    public void
    tearDown()
    {
        itsProvider = null;
    }

    @Test
    public void
    testOperationScope()
    {
        Map<Integer,List<Integer>> lists = new TreeMap<>();

        for (int i=0;i < 5;i++)
        {
            try (Operation operation = itsProvider.get())
            {
                List<Integer> list =
                    operation.getInstance(new TypeLiteral<>(){});

                list.addAll(Arrays.asList(i,i,i,i,i));
                lists.put(i,list);
            }
        }

        Assertions.assertEquals(5,lists.size());

        Assertions.assertTrue(lists.containsKey(0));
        Assertions.assertTrue(lists.containsKey(1));
        Assertions.assertTrue(lists.containsKey(2));
        Assertions.assertTrue(lists.containsKey(3));
        Assertions.assertTrue(lists.containsKey(4));

        for (Integer i: lists.keySet())
        {
            List<Integer> list = lists.get(i);

            Assertions.assertEquals(5,list.size());

            for (Integer j: list)
                Assertions.assertEquals(i,j);
        }
    }
}

//////////////////////////////////////////////////////////////////////////////
