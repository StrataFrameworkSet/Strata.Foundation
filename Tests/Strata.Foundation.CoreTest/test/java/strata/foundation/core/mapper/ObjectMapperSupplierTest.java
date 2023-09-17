//////////////////////////////////////////////////////////////////////////////
// ObjectMapperTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("CommitStage")
public
class ObjectMapperSupplierTest
{
    private IObjectMapperSupplier target;

    @BeforeEach
    public void
    setUp()
    {
        target = new ObjectMapperSupplier();
    }

    @AfterEach
    public void
    tearDown()
    {
        target = null;
    }

    @Test
    public void
    testSerializeHierarchy()
    {
        ObjectMapper mapper = target.get();


    }
}

//////////////////////////////////////////////////////////////////////////////
