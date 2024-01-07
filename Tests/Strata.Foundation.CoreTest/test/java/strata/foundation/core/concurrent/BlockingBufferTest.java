//////////////////////////////////////////////////////////////////////////////
// BlockingBufferTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("CommitStage")
public
class BlockingBufferTest
{
    private IBlockingBuffer<String>  subject;
    private ScheduledExecutorService writer;

    @BeforeEach
    public void
    setUp() throws Exception
    {
        subject = new BlockingBuffer<>();
        writer = Executors.newSingleThreadScheduledExecutor();
    }

    @Test
    public void
    testAcceptAndGet() throws Exception
    {
        writer.schedule(
            () -> subject.accept("XXXXXX"),
            5,
            TimeUnit.SECONDS);

        assertEquals("XXXXXX",subject.get());
    }
}

//////////////////////////////////////////////////////////////////////////////
