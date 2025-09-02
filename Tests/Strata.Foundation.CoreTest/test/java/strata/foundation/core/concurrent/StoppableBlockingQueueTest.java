/// ///////////////////////////////////////////////////////////////////////////
// StoppableBlockingQueueTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Tag("CommitStage")
public
class StoppableBlockingQueueTest
{
    private IBlockingQueue<String> queue;
    private ExecutorService        executor;

    @BeforeEach
    public void
    setUp()
    {
        queue = new StoppableBlockingQueue<>();
        executor = Executors.newFixedThreadPool(2);
    }

    @Test
    public void
    testTake() throws InterruptedException
    {
        executor.execute(this::produce);
        executor.execute(this::consume);
        executor.shutdown();

        while(!executor.awaitTermination(1,TimeUnit.SECONDS))
            System.out.print(".");

        System.out.println();
    }

    @ParameterizedTest
    @MethodSource("getSeconds")
    public void
    testDrainTo(int seconds) throws InterruptedException
    {
        executor.execute(this::produce);
        pause(Duration.ofSeconds(seconds));
        executor.execute(this::drain);
        executor.shutdown();

        while (!executor.awaitTermination(1,TimeUnit.SECONDS))
            System.out.print(".");

        System.out.println();

    }

    @Test
    public void
    testStream() throws InterruptedException
    {
        executor.execute(this::produce);
        executor.execute(this::stream);
        executor.shutdown();

        while(!executor.awaitTermination(1,TimeUnit.SECONDS))
            System.out.print(".");

        System.out.println();
    }

    private void
    produce()
    {

        try
        {
            queue.offer("one");
            pause(Duration.ofMillis(500));
            queue.put("two");
            pause(Duration.ofMillis(500));
            queue.add("three");
            pause(Duration.ofMillis(500));
            queue.offer("four");
            pause(Duration.ofMillis(500));
            queue.put("five");
            pause(Duration.ofSeconds(5));
            queue.add("six");
            pause(Duration.ofMillis(500));
            queue.offer("seven");
            pause(Duration.ofSeconds(5));
            queue.stop();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void
    consume()
    {
        while (queue.isStarted())
        {
            try
            {
                String element = queue.take();
                System.out.println("Consumed: " + element);
                pause(Duration.ofMillis(25));
            }
            catch (StoppedException e)
            {
                System.out.println("Queue stopped.");
                return;
            }
            catch (InterruptedException e)
            {
                System.out.println("Interrupted during consuming.");
            }
        }
    }

    private void
    drain()
    {
        while (queue.isStarted())
        {
            try
            {
                List<String> elements = new ArrayList<>();

                queue.drainTo(elements);

                if (!elements.isEmpty())
                    System.out.println(
                        "Consumed: " +
                            elements
                                .stream()
                                .collect(Collectors.joining(",")));
                pause(Duration.ofSeconds(1));
            }
            catch (PartiallyDrainedException e)
            {
                System.out.println(e.getMessage());

                if (e.hasDrained(String.class))
                    System.out.println(
                        "Drained elements: " +
                            e.getDrained(String.class)
                                .stream()
                                .collect(Collectors.joining(",")));

                return;
            }
        }
    }

    private void
    stream()
    {
        queue
            .stream()
            .forEach(element -> System.out.println("Consumed: " + element));

    }

    private void
    pause(Duration duration)
    {
        try
        {
            Thread.sleep(duration.toMillis());
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted", e);
        }
    }

    private static List<Integer>
    getSeconds()
    {
        return List.of(2, 3, 5, 7, 11, 13);
    }
}

//////////////////////////////////////////////////////////////////////////////
