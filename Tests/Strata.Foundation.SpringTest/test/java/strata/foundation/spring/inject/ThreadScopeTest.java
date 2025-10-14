//////////////////////////////////////////////////////////////////////////////
// ThreadScopeTest.java
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
class ThreadScopeTest
{
    private static ApplicationContext context;
    private ExecutorService           executor;

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
    testThreadScope() throws Exception
    {
        ThreadNameSupplier supplier =
            context.getBean(ThreadNameSupplier.class);

        assertNotNull(supplier);
        System.out.println("Main Thread: " + supplier.get());

        executor.submit(
            () ->
                {
                    ThreadNameSupplier bean =
                        context.getBean(ThreadNameSupplier.class);

                    assertNotNull(bean);
                    System.out.println("Instance 1: " + bean.get());
                });
        executor.submit(
            () ->
                {
                    ThreadNameSupplier bean =
                        context.getBean(ThreadNameSupplier.class);

                    assertNotNull(bean);
                    System.out.println("Instance 2: " + bean.get());
                });
        executor.submit(
            () ->
                {
                    ThreadNameSupplier bean =
                        context.getBean(ThreadNameSupplier.class);

                    assertNotNull(bean);
                    System.out.println("Instance 3: " + bean.get());
                });
        executor.shutdown();
        executor.awaitTermination(
            5,
            java.util.concurrent.TimeUnit.SECONDS);
    }
}

//////////////////////////////////////////////////////////////////////////////
