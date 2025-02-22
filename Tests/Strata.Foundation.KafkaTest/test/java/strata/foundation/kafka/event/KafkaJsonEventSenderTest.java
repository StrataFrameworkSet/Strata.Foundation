//////////////////////////////////////////////////////////////////////////////
// KafkaJsonEventSenderTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import com.google.inject.Module;
import strata.foundation.core.event.EventSenderTest;

public
class KafkaJsonEventSenderTest
    extends EventSenderTest
{
    @Override
    protected Module
    getModule()
    {
        return new JsonTestModule();
    }
}

//////////////////////////////////////////////////////////////////////////////
