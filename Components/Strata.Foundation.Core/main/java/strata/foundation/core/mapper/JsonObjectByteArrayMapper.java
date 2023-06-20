// ##########################################################################
// # File Name:	JsonObjectMapper.java
// ##########################################################################

package strata.foundation.core.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public
class JsonObjectByteArrayMapper<T>
    implements IObjectMapper<T,byte[]>
{

    private final ObjectMapper       itsMapper;
    private final Map<String,String> itsTypeMappings;

    public
    JsonObjectByteArrayMapper()
    {

        itsMapper =
            JsonMapper
                .builder()
                .enable( MapperFeature.REQUIRE_SETTERS_FOR_GETTERS )
                .enable( MapperFeature.ALLOW_EXPLICIT_PROPERTY_RENAMING )
                .enable( DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES )
                .enable( SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS )
                .build();

        itsMapper
            .setPropertyNamingStrategy(
               PropertyNamingStrategies.UPPER_CAMEL_CASE )
            .activateDefaultTypingAsProperty(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE,
                "@class")
            .registerModule(new SimpleModule())
            .registerModule(new JavaTimeModule())
            .registerModule( new Jdk8Module())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        itsTypeMappings = new HashMap<>();
    }

    public
    JsonObjectByteArrayMapper(ObjectMapper mapper)
    {
        itsMapper = mapper;
        itsTypeMappings = new HashMap<>();
    }

    @Override
    public <S extends T> byte[]
    toPayload(S object)
    {
        try
        {
            return itsMapper.writeValueAsBytes( object );
        }
        catch(JsonProcessingException e)
        {
            throw new IllegalStateException( e );
        }
    }

    @Override
    public <S extends T> S 
    toObject(Class<S> type,byte[] payload)
    {
        try
        {
            return itsMapper.readValue( preprocess(payload),type );
        }
        catch(IOException e)
        {
            throw new IllegalStateException( e );
        }
    }

    public JsonObjectByteArrayMapper<T>
    insertMapping(String sourceType,Class<?> destType)
    {
        itsTypeMappings.put( sourceType,destType.getName() );
        return this;
    }
    
    public JsonObjectByteArrayMapper<T>
    clearMappings()
    {
        itsTypeMappings.clear();
        return this;
    }

    public ObjectMapper
    getImplementation()
    {
        return itsMapper;
    }

    private byte[]
    preprocess(byte[] payload)
    {
        String output = null;
        
        output = new String(payload).replace( "__type","@class" );
        output = output.replace( "$type","@class" );
        
        for (Entry<String,String> mapping:itsTypeMappings.entrySet())
            output = output.replace( mapping.getKey(),mapping.getValue() );
        
        return output.getBytes();
    }
}

// ##########################################################################
