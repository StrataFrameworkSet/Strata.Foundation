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
class JsonObjectMapper<T>
    implements IObjectMapper<T,String>
{
    
    private final ObjectMapper       itsMapper;
    private final Map<String,String> itsTypeMappings;
    
    public
    JsonObjectMapper()
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
    JsonObjectMapper(ObjectMapper mapper)
    {
        itsMapper = mapper;
        itsTypeMappings = new HashMap<>();
    }

    @Override
    public <S extends T> String
    toPayload(S object)
    {
        try
        {
            return itsMapper.writeValueAsString( object );
        }
        catch(JsonProcessingException e)
        {
            throw new IllegalStateException( e );
        }
    }

    @Override
    public <S extends T> S 
    toObject(Class<S> type,String payload)
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

    public JsonObjectMapper<T>
    insertMapping(String sourceType,Class<?> destType)
    {
        itsTypeMappings.put( sourceType,destType.getName() );
        return this;
    }
    
    public JsonObjectMapper<T>
    clearMappings()
    {
        itsTypeMappings.clear();
        return this;
    }

    /*************************************************************************
     *
     * @return
     */
    public ObjectMapper
    getImplementation()
    {
        return itsMapper;
    }

    private String
    preprocess(String payload)
    {
        String output = null;
        
        output = payload.replace( "__type","@class" );
        output = output.replace( "$type","@class" );
        
        for (Entry<String,String> mapping:itsTypeMappings.entrySet())
            output = output.replace( mapping.getKey(),mapping.getValue() );
        
        return output;
    }
}

// ##########################################################################
