//////////////////////////////////////////////////////////////////////////////
// AvroObjectMapperTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.mapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strata.foundation.core.event.FooData;
import strata.foundation.core.mapper.IObjectMapper;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;


public
class AvroObjectMapperTest
{
    private IObjectMapper<Object,String> itsTarget;

    @BeforeEach
    public void
    setUp()
    {
        itsTarget = new AvroObjectMapper<>();
    }

    @AfterEach
    public void
    tearDown()
    {
        itsTarget = null;
    }

    @Test
    public void
    testToPayloadToObject()
    {
        FooData expected =
            FooData
                .newBuilder()
                .setId("1234567")
                .setX("XXXXXXXXXXXXXXXXXXX")
                .setY(new Random().nextInt())
                .build();
        FooData actual =
            itsTarget.toObject(
                FooData.class,
                itsTarget.toPayload(expected));

        assertEquals(expected,actual);
    }
}

//////////////////////////////////////////////////////////////////////////////
