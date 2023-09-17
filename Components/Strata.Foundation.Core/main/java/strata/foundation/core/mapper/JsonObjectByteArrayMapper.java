// ##########################################################################
// # File Name:	JsonObjectMapper.java
// ##########################################################################

package strata.foundation.core.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

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
            new ObjectMapperSupplier()
                .get()
                .setPropertyNamingStrategy(
                   PropertyNamingStrategies.UPPER_CAMEL_CASE )
                .activateDefaultTypingAsProperty(
                    LaissezFaireSubTypeValidator.instance,
                    ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE,
                    "@class");

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
