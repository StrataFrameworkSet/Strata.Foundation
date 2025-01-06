//////////////////////////////////////////////////////////////////////////////
// ObjectMapperTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.transfer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import strata.foundation.core.mapper.ObjectMapperSupplier;
import strata.foundation.core.value.EmailAddress;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("CommitStage")
public
class ObjectMapperTest
{
    private ObjectMapper target;

    @BeforeEach
    public void
    setUp()
    {
        target = new ObjectMapperSupplier().get();
    }

    @AfterEach
    public void
    tearDown()
    {
        target = null;
    }

    @Test
    public void
    testSerializeDoFooRequest() throws Exception
    {
        DoFooRequest expected =
            new DoFooRequest()
                .setFoo("XXXXXXXXXXXX");
        DoFooRequest actual =
            target.readValue(
                target.writeValueAsString(expected),
                DoFooRequest.class);

        assertEquals(expected,actual);
    }

    @Test
    public void
    testDeserializeAbstractServiceRequest() throws Exception
    {
        DoFooRequest expected =
            new DoFooRequest()
                .setFoo("XXXXXXXXXXXX");
        AbstractServiceRequest actual =
            target.readValue(
                target.writeValueAsString(expected),
                DoFooRequest.class);

        assertEquals(expected,actual);
    }

    @Test
    public void
    testSerializeEmailAddress() throws Exception
    {
        EmailAddress expected = new EmailAddress("john.doe@xyz.com");
        String expectedOutput = target.writeValueAsString(expected);
        EmailAddress actual = target.readValue(expectedOutput,EmailAddress.class);

        assertEquals(expected,actual);
    }
}

//////////////////////////////////////////////////////////////////////////////
