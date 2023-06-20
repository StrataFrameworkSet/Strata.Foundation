//////////////////////////////////////////////////////////////////////////////
// FooEventListener.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public
class FooEventListener
    implements IFooEventListener
{
    private final IFooEventReceiver itsReceiver;
    private Queue<FooEvent>         itsExpected;
    private Queue<FooEvent>         itsActual;

    public
    FooEventListener(IFooEventReceiver receiver)
    {
        itsReceiver = receiver;
        itsExpected = new ConcurrentLinkedQueue<>();
        itsActual   = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void
    onEvent(FooEvent actual)
    {
        System.out.println("onEvent");
        itsActual.add(actual);
        itsReceiver.stopListening();
    }

    @Override
    public void
    onException(Exception exception)
    {
        exception.printStackTrace();
    }

    public FooEventListener
    insertExpected(FooEvent expected)
    {
        itsExpected.add(expected);
        return this;
    }

    public Queue<FooEvent>
    getExpected()
    {
        return itsExpected;
    }

    public boolean
    receivedActual()
    {
        return !itsActual.isEmpty();
    }

    public void
    checkAssertions()
    {
        assertFalse(itsExpected.isEmpty(),"Expected is empty");
        assertFalse(itsActual.isEmpty(),"Actual is empty");
        assertEquals(itsExpected.size(),itsActual.size(),"Expected.size != Actual.size");

        while (!itsExpected.isEmpty())
        {
            FooEvent expected = itsExpected.remove();
            FooEvent actual = itsActual.remove();

            EventIdentifiersData expectedIds = expected.getIdentifiers();
            FooData              expectedSource = expected.getSource();
            EventIdentifiersData actualIds = actual.getIdentifiers();
            FooData              actualSource = actual.getSource();

            assertEquals("EventIds !=",expectedIds.getEventId(),actualIds.getEventId());
            assertEquals("CorrelationIds !=",expectedIds.getCorrelationId(),actualIds.getCorrelationId());
            assertEquals(expectedIds.getTimestamp(),actualIds.getTimestamp(),"Timestamps !=");
            assertEquals(expectedSource.getId(),actualSource.getId(),"Ids !=");
            assertEquals(expectedSource.getX(),actualSource.getX(),"Xs !=");
            assertEquals(expectedSource.getY(),actualSource.getY(),"Ys !=");
        }
    }
}

//////////////////////////////////////////////////////////////////////////////
