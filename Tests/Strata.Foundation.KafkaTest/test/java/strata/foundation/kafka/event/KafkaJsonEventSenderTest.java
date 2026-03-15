//////////////////////////////////////////////////////////////////////////////
// KafkaJsonEventSenderTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import com.google.inject.Guice;
import com.google.inject.Module;
import strata.foundation.core.event.EventSenderTest;
import strata.foundation.core.inject.IInjector;
import strata.foundation.guice.inject.GuiceInjector;

public
class KafkaJsonEventSenderTest
    extends EventSenderTest
{
    @Override
    protected IInjector
    getInjector()
    {
        return
            new GuiceInjector(
                Guice.createInjector(
                    new JsonTestModule()));
    }
}

//////////////////////////////////////////////////////////////////////////////
