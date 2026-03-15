//////////////////////////////////////////////////////////////////////////////
// KafkaAvroEventSenderTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import com.google.inject.Guice;
import com.google.inject.Module;
import org.junit.jupiter.api.Tag;
import strata.foundation.core.event.EventSenderTest;
import strata.foundation.core.inject.IInjector;
import strata.foundation.guice.inject.GuiceInjector;

@Tag("CommitStage")
public
class KafkaAvroEventSenderTest
    extends EventSenderTest
{
    @Override
    protected IInjector
    getInjector()
    {
        return
            new GuiceInjector(
                Guice.createInjector(
                    new AvroTestModule()));
    }
}

//////////////////////////////////////////////////////////////////////////////
