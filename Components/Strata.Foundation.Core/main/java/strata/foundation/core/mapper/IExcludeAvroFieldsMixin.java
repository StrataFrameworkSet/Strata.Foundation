//////////////////////////////////////////////////////////////////////////////
// IExcludeAvroFieldsMixin.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificData;

/**
 * <p>
 * Jackson mix-in that suppresses serialization of the
 * {@code schema} and {@code specificData} properties generated on
 * Avro {@code SpecificRecordBase} classes. Avro's generated accessors
 * expose the record's {@link Schema} and {@link SpecificData} as bean
 * properties, which Jackson would otherwise attempt to serialize
 * along with the record's actual data. Registering this mix-in against
 * {@code SpecificRecordBase} (for example, via
 * {@link com.fasterxml.jackson.databind.ObjectMapper#addMixIn}) hides
 * both properties from JSON output.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * ObjectMapper mapper = new ObjectMapper();
 * mapper.addMixIn(SpecificRecordBase.class,IExcludeAvroFieldsMixin.class);
 * </pre>
 * </p>
 */
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