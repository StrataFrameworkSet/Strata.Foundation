// ##########################################################################
// # File Name:	JsonObjectMapper.java
// ##########################################################################

package strata.foundation.core.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


/****************************************************************************
 * 
 */
public 
class JsonObjectByteArrayMapper<T>
    implements IObjectMapper<T,byte[]>
{

    private final ObjectMapper       itsMapper;
    private final Map<String,String> itsTypeMappings;

    /************************************************************************
     * Creates a new JsonObjectMapper.
     *
     */
    public
    JsonObjectByteArrayMapper()
    {

        itsMapper = new ObjectMapper();
        itsMapper
            .setPropertyNamingStrategy(
                new PropertyNamingStrategy.UpperCamelCaseStrategy() )
            .enableDefaultTypingAsProperty(
                ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE,
                    "@class")
            .enable( MapperFeature.REQUIRE_SETTERS_FOR_GETTERS )
            .enable( MapperFeature.ALLOW_EXPLICIT_PROPERTY_RENAMING )
            .enable( MapperFeature.SORT_PROPERTIES_ALPHABETICALLY )
            .enable( DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES )
            .enable( SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS )
            .registerModule(new SimpleModule())
            .registerModule(new JavaTimeModule())
            .registerModule( new Jdk8Module())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        itsTypeMappings = new HashMap<>();
    }

    /************************************************************************
     * Creates a new JsonObjectMapper.
     *
     */
    public
    JsonObjectByteArrayMapper(ObjectMapper mapper)
    {
        itsMapper = mapper;
        itsTypeMappings = new HashMap<>();
    }

    /************************************************************************
     * {@inheritDoc} 
     */
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

    /************************************************************************
     * {@inheritDoc} 
     */
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

    /************************************************************************
     *  
     *
     * @param sourceType
     * @param destType
     * @return
     */
    public JsonObjectByteArrayMapper<T>
    insertMapping(String sourceType,Class<?> destType)
    {
        itsTypeMappings.put( sourceType,destType.getName() );
        return this;
    }
    
    /************************************************************************
     *  
     *
     * @return
     */
    public JsonObjectByteArrayMapper<T>
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

    /************************************************************************
     *  
     *
     * @param payload
     * @return
     */
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
