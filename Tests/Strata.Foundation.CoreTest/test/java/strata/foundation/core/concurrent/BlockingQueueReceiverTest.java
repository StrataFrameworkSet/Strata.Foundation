/// ///////////////////////////////////////////////////////////////////////////
// BlockingQueueReceiverTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class BlockingQueueReceiverTest
    implements Consumer<String>
{
    private IBlockingQueue<String>             queue;
    private ISender<String>                    sender1;
    private ISender<String>                    sender2;
    private IReceiver<String,Consumer<String>> receiver;
    private List<String>                       received;

    @BeforeEach
    public void
    setUp()
    {
        queue = new StoppableBlockingQueue<>();
        sender1 = new BlockingQueueSender<>(queue);
        sender2 = new BlockingQueueSender<>(queue);
        receiver = new BlockingQueueReceiver<>(queue);
        received = new ArrayList<>();

        receiver.startConsuming(this);
    }

    @AfterEach
    public void
    tearDown()
    {
        if (receiver.isConsuming())
            receiver.stopConsuming();
    }

    @Test
    public void
    testSendReceive() throws InterruptedException
    {
        assertTrue(receiver.isConsuming());

        sender1.send("1.A");
        sender2.send("2.A");
        sender1.send("1.B");
        sender2.send("2.B");
        sender1.send("1.C");
        sender1.send("1.D");
        sender2.send("2.C");
        sender2.send("2.D");

        Thread.sleep(500);

        assertReceived(
            List.of("1.A", "2.A", "1.B", "2.B", "1.C", "1.D", "2.C", "2.D"));
    }

    @Test
    public void
    testSendStopSendReceive() throws InterruptedException
    {
        assertTrue(receiver.isConsuming());

        sender1.send("1.A");
        sender2.send("2.A");
        sender1.send("1.B");
        sender2.send("2.B");

        queue.stop();
        Thread.sleep(500);
        assertTrue(queue.isStopped());
        assertFalse(receiver.isConsuming());
        receiver.startConsuming();
        assertTrue(receiver.isConsuming());

        sender1.send("1.C");
        sender1.send("1.D");
        sender2.send("2.C");
        sender2.send("2.D");

        Thread.sleep(500);

        assertReceived(
            List.of("1.A", "2.A", "1.B", "2.B", "1.C", "1.D", "2.C", "2.D"));
    }

    public void
    accept(String element)
    {
        received.add(element);
    }

    private void
    assertReceived(List<String> expected)
    {
        assertEquals(expected.size(), received.size());
        assertLinesMatch(expected,received);
    }
}

//////////////////////////////////////////////////////////////////////////////
