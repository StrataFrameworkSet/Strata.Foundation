/// ///////////////////////////////////////////////////////////////////////////
// ThreadLocalExcerptAppender.java
/// ///////////////////////////////////////////////////////////////////////////

package strata.foundation.chronicle.event;

import net.openhft.chronicle.bytes.BytesStore;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.wire.DocumentContext;
import net.openhft.chronicle.wire.UnrecoverableTimeoutException;
import net.openhft.chronicle.wire.Wire;

import java.util.function.Supplier;

public
class ThreadLocalExcerptAppender
    implements ExcerptAppender
{
    private final ThreadLocal<ExcerptAppender> appender;

    public ThreadLocalExcerptAppender(Supplier<ExcerptAppender> supplier)
    {
        appender = ThreadLocal.withInitial(supplier);
    }

    @Override
    public void
    writeBytes(BytesStore bytes)
    {
        appender
            .get()
            .writeBytes(bytes);
    }

    @Override
    public long
    lastIndexAppended()
    {
        return
            appender
                .get()
                .lastIndexAppended();
    }

    @Override
    public int
    cycle()
    {
        return
            appender
                .get()
                .cycle();
    }

    @Override
    public Wire
    wire()
    {
        return
            appender
                .get()
                .wire();
    }

    @Override
    public int
    sourceId()
    {
        return 0;
    }

    @Override
    public ChronicleQueue
    queue()
    {
        return
            appender
                .get()
                .queue();
    }

    @Override
    public void
    close()
    {
        appender
            .get()
            .close();
    }

    @Override
    public boolean
    isClosed()
    {
        return
            appender
                .get()
                .isClosed();
    }

    @Override
    public DocumentContext
    writingDocument(boolean metaData) throws UnrecoverableTimeoutException
    {
        return
            appender
                .get()
                .writingDocument(metaData);
    }

    @Override
    public DocumentContext
    acquireWritingDocument(boolean metaData) throws UnrecoverableTimeoutException
    {
        return
            appender
                .get()
                .acquireWritingDocument(metaData);
    }

    @Override
    public void
    singleThreadedCheckReset()
    {
        appender
            .get()
            .singleThreadedCheckReset();
    }

    @Override
    public void
    singleThreadedCheckDisabled(boolean singleThreadedCheckDisabled)
    {
        appender
            .get()
            .singleThreadedCheckDisabled(singleThreadedCheckDisabled);
    }
}

//////////////////////////////////////////////////////////////////////////////
