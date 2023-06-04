//////////////////////////////////////////////////////////////////////////////
// AvroSerializerModifier.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.mapper;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.apache.avro.Schema;

import java.util.List;
import java.util.stream.Collectors;

public
class AvroSerializerModifier
    extends BeanSerializerModifier
{
    @Override
    public List<BeanPropertyWriter>
    changeProperties(
        SerializationConfig      config,
        BeanDescription          beanDesc,
        List<BeanPropertyWriter> beanProperties)
    {
        System.out.println("changeProperties called");
        return
            beanProperties
                .stream()
                .filter( p -> !p.getType().isTypeOrSubTypeOf(Schema.class))
                .collect(Collectors.toList());
    }
}

//////////////////////////////////////////////////////////////////////////////
