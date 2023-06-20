//////////////////////////////////////////////////////////////////////////////
// EventSenderTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strata.foundation.core.action.IActionQueue;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract
class EventSenderTest
{
    private Module            itsModule;
    private IFooEventSender   itsTarget;
    private IFooEventReceiver itsReceiver;
    private IActionQueue      itsActionQueue;

    @BeforeEach
    public void
    setUp()
    {
        Injector injector = Guice.createInjector(getModule());

        itsTarget = injector.getInstance(IFooEventSender.class);
        itsReceiver = injector.getInstance(IFooEventReceiver.class);
        itsActionQueue = injector.getInstance(IActionQueue.class);
    }

    @AfterEach
    public void
    tearDown()
    {
        itsReceiver.stopListening();
    }

    @Test
    public void
    testSend()
        throws Exception
    {
        FooEvent expected =
            FooEvent.newBuilder()
                .setIdentifiers(
                    EventIdentifiersData.newBuilder()
                        .setEventId(UUID.randomUUID().toString())
                        .setCorrelationId("1111111111111")
                        .setTimestamp(Instant.now())
                        .build())
                .setEventType(StandardEventType.CREATED)
                .setSource(
                    FooData.newBuilder()
                        .setId("ABCDEFGHIJK")
                        .setX("!@#$%^&*")
                        .setY(23).build())
                .build();
        FooEventListener listener =
            new FooEventListener(itsReceiver)
                .insertExpected(expected);

        itsReceiver.startListening(listener);
        assertTrue(itsReceiver.isListening());

        itsTarget.send(expected);
        itsActionQueue.execute();
        sleep(7);
        assertFalse(itsReceiver.isListening(),"Should have stopped listening at this point");
        listener.checkAssertions();
    }

    @Test
    public void
    testSendMany()
        throws Exception
    {
        FooEvent expected1 =
            FooEvent.newBuilder()
                .setIdentifiers(
                    EventIdentifiersData.newBuilder()
                        .setEventId(UUID.randomUUID().toString())
                        .setCorrelationId("1")
                        .setTimestamp(Instant.now())
                        .build())
                .setEventType(StandardEventType.CREATED)
                .setSource(
                    FooData.newBuilder()
                        .setId("ABCDEFGHIJK")
                        .setX("!@#$%^&*")
                        .setY(23).build())
                .build();
        FooEvent expected2 =
            FooEvent.newBuilder()
                .setIdentifiers(
                    EventIdentifiersData.newBuilder()
                        .setEventId(UUID.randomUUID().toString())
                        .setCorrelationId("2")
                        .setTimestamp(Instant.now())
                        .build())
                .setEventType(StandardEventType.CREATED)
                .setSource(
                    FooData.newBuilder()
                        .setId("ABCDEFGHIJK")
                        .setX("!@#$%^&*")
                        .setY(23).build())
                .build();
        FooEventListener listener =
            new FooEventListener(itsReceiver)
                .insertExpected(expected1)
                .insertExpected(expected2);

        itsReceiver.startListening(listener);
        assertTrue(itsReceiver.isListening());

        itsTarget.send(expected1);
        itsTarget.send(expected2);
        itsActionQueue.execute();
        sleep(7);
        assertFalse(itsReceiver.isListening(),"Should have stopped listening at this point");
        listener.checkAssertions();
    }

    protected abstract Module
    getModule();

    protected void
    sleep(int seconds)
    {
        try
        {
            Thread.sleep(seconds*1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}

//////////////////////////////////////////////////////////////////////////////
