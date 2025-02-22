//////////////////////////////////////////////////////////////////////////////
// IExcludeAvroFieldsMixin.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificData;

public
interface IExcludeAvroFieldsMixin
{
    @JsonIgnore
    Schema
    getSchema();

    @JsonIgnore
    SpecificData
    getSpecificData();
}

//////////////////////////////////////////////////////////////////////////////