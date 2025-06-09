//////////////////////////////////////////////////////////////////////////////
// PhoneNumberTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import strata.foundation.core.mapper.ObjectMapperSupplier;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("CommitStage")
public
class GeoLocationTest
{
    @Test
    public void
    testMapping() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();
        GeoLocation  expected = GeoLocation.of(37.7749, -122.4194);
        GeoLocation  actual =
            mapper.readValue(
                mapper.writeValueAsString(expected),GeoLocation.class);

        assertEquals(expected,actual);
        System.out.println(mapper.writeValueAsString(expected));
    }

}

//////////////////////////////////////////////////////////////////////////////
