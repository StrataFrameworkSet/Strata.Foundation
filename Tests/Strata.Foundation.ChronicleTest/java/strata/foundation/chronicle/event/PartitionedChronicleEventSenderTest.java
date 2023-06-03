//////////////////////////////////////////////////////////////////////////////
// ChronicleEventSenderTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.chronicle.event;

import org.junit.jupiter.api.Tag;
import strata.foundation.core.event.SendResult;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.RollCycles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static strata.foundation.core.concurrent.Awaiter.await;

@Tag("IntegrationStage")
public
class PartitionedChronicleEventSenderTest
{
    private ChronicleQueue    sendingQueue1;
    private ChronicleQueue    sendingQueue2;
    private ChronicleQueue    sendingQueue3;
    private ChronicleQueue    receivingQueue1;
    private ChronicleQueue    receivingQueue2;
    private ChronicleQueue    receivingQueue3;
    private IFooEventSender   sender1;
    private IFooEventSender   sender2;
    private IFooEventSender   sender3;
    private IFooEventReceiver receiver1;
    private IFooEventReceiver receiver2;
    private IFooEventReceiver receiver3;
    private IFooEventSender partSender;
    private Path              path1;
    private Path              path2;
    private Path              path3;
    private List<FooEvent>    received1;
    private List<FooEvent>    received2;
    private List<FooEvent>    received3;

    @BeforeEach
    public void
    setUp() throws Exception
    {
        path1  = Path.of("C:/temp","test","foo-4");
        path2  = Path.of("C:/temp","test","foo-5");
        path3  = Path.of("C:/temp","test","foo-6");

        sendingQueue1 =
            ChronicleQueue
                .singleBuilder(path1)
                .rollCycle(RollCycles.FAST_HOURLY)
                .build();
        sendingQueue2 =
            ChronicleQueue
                .singleBuilder(path2)
                .rollCycle(RollCycles.FAST_HOURLY)
                .build();
        sendingQueue3 =
            ChronicleQueue
                .singleBuilder(path3)
                .rollCycle(RollCycles.FAST_HOURLY)
                .build();

        receivingQueue1 =
            ChronicleQueue
                .singleBuilder(path1)
                .rollCycle(RollCycles.FAST_HOURLY)
                .build();
        receivingQueue2 =
            ChronicleQueue
                .singleBuilder(path2)
                .rollCycle(RollCycles.FAST_HOURLY)
                .build();
        receivingQueue3 =
            ChronicleQueue
                .singleBuilder(path3)
                .rollCycle(RollCycles.FAST_HOURLY)
                .build();

        sender1   = new FooEventSender(sendingQueue1);
        sender2   = new FooEventSender(sendingQueue2);
        sender3   = new FooEventSender(sendingQueue3);

        partSender = new PartitionedFooEventSender(Set.of(sender1,sender2,sender3));

        receiver1 = new FooEventReceiver("foo-4.1",receivingQueue1);
        receiver2 = new FooEventReceiver("foo-5.1",receivingQueue2);
        receiver3 = new FooEventReceiver("foo-6.1",receivingQueue3);

        received1 = new ArrayList<>();
        received2 = new ArrayList<>();
        received3 = new ArrayList<>();

        partSender.open();

        receiver1.startListening(new FooEventListener(received1));
        receiver2.startListening(new FooEventListener(received2));
        receiver3.startListening(new FooEventListener(received3));
    }

    @AfterEach
    public void
    tearDown() throws Exception
    {
        partSender.close();
        receiver1.stopListening();
        receiver2.stopListening();
        receiver3.stopListening();
        received1.clear();
        received2.clear();
        received3.clear();
        sendingQueue1.close();
        sendingQueue2.close();
        sendingQueue3.close();
        receivingQueue1.close();
        receivingQueue2.close();
        receivingQueue3.close();
    }

    @Test
    public void
    testSend() throws Exception
    {
        String key1 = "1111111111111111111111";
        String key2 = "2222222222222222222222";
        String key3 = "3333333333333333333333";

        List<FooEvent> sent =
            List.of(
                new FooEvent(key1,1),
                new FooEvent(key2,2),
                new FooEvent(key3,3));
        List<CompletionStage<SendResult<FooEvent>>> results =
            sent
                .stream()
                .map(event -> partSender.send(event))
                .collect(Collectors.toList());

        assertTrue(
            results
                .stream()
                .map(result -> await(result))
                .filter(result -> !result.isSuccess())
                .collect(Collectors.toList()).isEmpty());

        assertReceived(sent);
    }

    protected void
    assertReceived(List<FooEvent> sent)
    {
        assertEquals(1,received1.size());
        assertEquals(1,received2.size());
        assertEquals(1,received3.size());

        assertNotEquals(received1.get(0),received2.get(0));
        assertNotEquals(received1.get(0),received3.get(0));
        assertNotEquals(received2.get(0),received3.get(0));
    }
}

//////////////////////////////////////////////////////////////////////////////
