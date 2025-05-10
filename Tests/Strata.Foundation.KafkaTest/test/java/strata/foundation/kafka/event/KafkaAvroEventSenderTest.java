//////////////////////////////////////////////////////////////////////////////
// KafkaAvroEventSenderTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import com.google.inject.Module;
import org.junit.jupiter.api.Tag;
import strata.foundation.core.event.EventSenderTest;

@Tag("CommitStage")
public
class KafkaAvroEventSenderTest
    extends EventSenderTest
{
    @Override
    protected Module
    getModule()
    {
        return new AvroTestModule();
    }
}

//////////////////////////////////////////////////////////////////////////////
