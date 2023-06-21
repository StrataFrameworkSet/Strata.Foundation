//////////////////////////////////////////////////////////////////////////////
// AvroTestModule.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import strata.foundation.core.action.IActionQueue;
import strata.foundation.core.action.StandardActionQueue;
import strata.foundation.core.event.IFooEventReceiver;
import strata.foundation.core.event.IFooEventSender;
import strata.foundation.core.inject.AbstractModule;
import strata.foundation.core.inject.ThreadScope;

public
class AvroTestModule
    extends AbstractModule
{
    public AvroTestModule()
    {
        setDefaultScope(new ThreadScope());
    }

    @Override
    protected void
    configure()
    {
        bind(IActionQueue.class)
            .to(StandardActionQueue.class)
            .in(getDefaultScope());

        bind(IFooEventSender.class)
            .to(KafkaAvroFooEventSender.class)
            .in(getDefaultScope());

        bind(IFooEventReceiver.class)
            .to(KafkaAvroFooEventReceiver.class)
            .in(getDefaultScope());

    }
}

//////////////////////////////////////////////////////////////////////////////
