/// ///////////////////////////////////////////////////////////////////////////
// OperationScopeTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("CommitStage")
public
class OperationScopeTest
{
    private static ApplicationContext context;
    private ExecutorService executor;

    @BeforeAll
    public static void
    setUpAll() throws Exception
    {
        context =
            new AnnotationConfigApplicationContext(TestConfiguration.class);
    }

    @BeforeEach
    public void
    setUp() throws Exception
    {
        executor = Executors.newFixedThreadPool(3);
    }

    @Test
    public void
    testOperationScope() throws Exception
    {
        OperationNameSupplier supplier0 =
            context.getBean(OperationNameSupplier.class);
        assertNotNull(supplier0);
        System.out.println("Global: " + supplier0.get());

        try (Operation operation1 = context.getBean(Operation.class))
        {
            assertNotNull(operation1);

            OperationNameSupplier supplier1 =
                operation1.getInstance(OperationNameSupplier.class);

            assertNotNull(supplier1);
            System.out.println("Operation 1: " + supplier1.get());

            try (Operation operation2 = context.getBean(Operation.class))
            {
                assertNotNull(operation2);

                OperationNameSupplier supplier2 =
                    operation2.getInstance(OperationNameSupplier.class);

                assertNotNull(supplier2);
                System.out.println("Operation 2: " + supplier2.get());

                try (Operation operation3 = context.getBean(Operation.class))
                {
                    assertNotNull(operation3);

                    OperationNameSupplier supplier3 =
                        operation3.getInstance(OperationNameSupplier.class);

                    assertNotNull(supplier3);
                    System.out.println("Operation 3: " + supplier3.get());
                }
            }
        }
    }

}

//////////////////////////////////////////////////////////////////////////////
