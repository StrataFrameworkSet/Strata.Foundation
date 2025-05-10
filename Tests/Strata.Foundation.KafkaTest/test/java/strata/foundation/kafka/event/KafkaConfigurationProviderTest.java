/// ///////////////////////////////////////////////////////////////////////////
// KafkaConfigurationProviderTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.kafka.clients.CommonClientConfigs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import strata.foundation.core.event.FooEvent;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("CommitStage")
public
class KafkaConfigurationProviderTest
{
    private IKafkaConfigurationProvider provider;

    @BeforeEach
    public void
    setUp()
    {
        Injector injector = Guice.createInjector(new AvroTestModule());

        provider = injector.getInstance(IKafkaConfigurationProvider.class);
    }

    @Test
    public void
    testGet()
    {
        Map<String,Object> config = provider.get();
        String groupKeyId =
            KafkaConfigurationProvider.GROUP_ID_PREFIX_KEY +
            FooEvent
                .class
                .getSimpleName();

        System.out.println(config);

        assertTrue(
            config.containsKey(
                CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG));

        assertTrue(config.containsKey(groupKeyId));
        assertEquals(
            "strata.fooevent.avro.consumer-group-1",
            new ClassBasedGroupIdSupplier<FooEvent>(FooEvent.class,config)
                .get()
                .get());
    }
}

//////////////////////////////////////////////////////////////////////////////
