//////////////////////////////////////////////////////////////////////////////
// ChronicleEventSenderTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.chronicle.event;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import strata.foundation.core.event.SendResult;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.RollCycles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static strata.foundation.core.concurrent.Awaiter.await;

@Tag("IntegrationStage")
public
class ChronicleEventSenderTest
{
    private ChronicleQueue    sendingQueue;
    private ChronicleQueue    receivingQueue;
    private IFooEventSender   sender;
    private IFooEventReceiver receiver;
    private Path              path;
    private List<FooEvent>    received;
    private int               yGenerator;

    @BeforeEach
    public void
    setUp() throws Exception
    {
        path  = Path.of("C:/temp","test","foo");
        sendingQueue =
            ChronicleQueue
                .singleBuilder(path)
                .rollCycle(RollCycles.FAST_HOURLY)
                .build();
        receivingQueue =
            ChronicleQueue
                .singleBuilder(path)
                .rollCycle(RollCycles.FAST_HOURLY)
                .build();

        sender   = new FooEventSender(sendingQueue);
        receiver = new FooEventReceiver("foo-1",receivingQueue);
        received = new ArrayList<>();
        sender.open();
        receiver.startListening(new FooEventListener(received));
        yGenerator = 0;
    }

    @AfterEach
    public void
    tearDown() throws Exception
    {
        sender.close();
        receiver.stopListening();
        received.clear();
        sendingQueue.close();
        receivingQueue.close();

        if (sendingQueue.file().delete())
            System.out.println("sending queue - file not deleted");
        if (receivingQueue.file().delete())
            System.out.println("receiving queue - file not deleted");
    }

    @Test
    public void
    testSend() throws Exception
    {
        List<FooEvent> sent =
            List.of(
                new FooEvent("AAA",1),
                new FooEvent("BBB",2),
                new FooEvent("CCC",3));
        List<CompletionStage<SendResult<FooEvent>>> results =
            sent
                .stream()
                .map(event -> sender.send(event))
                .collect(Collectors.toList());

        assertTrue(
            results
                .stream()
                .map(result -> await(result))
                .filter(result -> !result.isSuccess())
                .collect(Collectors.toList()).isEmpty());

        assertReceived(sent);
    }

    //@Test
    public void
    testPerformance() throws Exception
    {
        FooEvent event = new FooEvent("XXX",7);

        receiver.stopListening();
        receiver.startListening(new NullFooEventListener());

        Instant start = Instant.now();
        Instant finish = null;

        List<CompletionStage<SendResult<FooEvent>>> results =
            new ArrayList<>();

        for (int i = 0;i < 1000000;i++)
            results.add(sender.send(event));

        finish = Instant.now();

        results
            .stream()
            .map(result -> await(result));

        assertTrue(Duration.between(start,finish).getSeconds() < 15);
    }

    protected void
    assertReceived(List<FooEvent> sent)
    {
        assertIterableEquals(sent,received);
    }

    private FooEvent
    createRandom()
    {
        return new FooEvent(UUID.randomUUID().toString(),++yGenerator);
    }
}

//////////////////////////////////////////////////////////////////////////////
