//////////////////////////////////////////////////////////////////////////////
// RetryExecutorTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import strata.foundation.core.time.Stopwatch;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Tag("CommitStage")
public
class RetryExecutorTest
{
    private IRetryExecutor retryAgent;

    @BeforeEach
    public void
    setUp()
    {
        retryAgent =
            new BasicRetryExecutor()
                .setMaxAttempts(10)
                .setRetryDelay(50L)
                .setBackoffStrategy(BackoffStrategy.EXPONENTIAL)
                .setBackoffFactor(2.0);
    }

    @Test
    public void
    testRunRetryable()
    {
        AtomicInteger counter = new AtomicInteger(1);
        Stopwatch     stopwatch = new Stopwatch();

        stopwatch.start();
        retryAgent.executeRun(
            () ->
            {
                Duration duration =
                    stopwatch
                        .stop()
                        .getDuration();

                System.out.println("Attempt " + counter.get() + " paused for " + duration.toMillis() + " ms");
                stopwatch.restart();
                if (counter.getAndIncrement() < 10)
                    throw new RuntimeException("Test exception " + counter.get());
                System.out.println("Hello, World!");
            });
    }

    @Test
    public void
    testCallRetryable()
    {
        AtomicInteger counter = new AtomicInteger(1);

        String result =
            retryAgent.executeCall(
                () ->
                {
                    if (counter.getAndIncrement() < 3)
                        throw new RuntimeException("Test exception " + counter.get());
                    return "Hello, World!";
                });

        System.out.println(result);
    }

    @Test
    public void
    testAcceptRetryable()
    {
        AtomicInteger counter = new AtomicInteger(1);

        retryAgent.executeAccept(
            value ->
            {
                if (counter.getAndIncrement() < 3)
                    throw new RuntimeException("Test exception " + counter.get());
                System.out.println("Hello, " + value + "!");
            },"World");
    }

    @Test
    public void
    testGetRetryable()
    {
        AtomicInteger counter = new AtomicInteger(1);

        String result =
            retryAgent.executeGet(
                () ->
                    {
                    if (counter.getAndIncrement() < 3)
                        throw new RuntimeException("Test exception " + counter.get());
                    return "Hello, World!";
                    });

        System.out.println(result);
    }

    @Test
    public void
    testApplyRetryable()
    {
        AtomicInteger counter = new AtomicInteger(1);

        String result =
            retryAgent.executeApply(
                value ->
                    {
                    if (counter.getAndIncrement() < 3)
                        throw new RuntimeException("Test exception " + counter.get());
                    return "Hello, " + value + "!";
                    },"World");

        System.out.println(result);
    }
}

//////////////////////////////////////////////////////////////////////////////
