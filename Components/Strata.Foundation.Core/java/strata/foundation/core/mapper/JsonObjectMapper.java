// ##########################################################################
// # File Name:	JsonObjectMapper.java
// ##########################################################################

package strata.foundation.core.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.avro.specific.SpecificRecordBase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


/****************************************************************************
 * 
 */
public 
class JsonObjectMapper<T>
    implements IObjectMapper<T,String>
{
    
    private final ObjectMapper       itsMapper;
    private final Map<String,String> itsTypeMappings;
    
    /************************************************************************
     * Creates a new JsonObjectMapper. 
     *
     */
    public
    JsonObjectMapper()
    {

        itsMapper = new ObjectMapper();
        itsMapper
            .setPropertyNamingStrategy( 
                new PropertyNamingStrategy.UpperCamelCaseStrategy() )
            .activateDefaultTypingAsProperty(
                itsMapper.getPolymorphicTypeValidator(),
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
            .addMixIn(SpecificRecordBase.class,IExcludeAvroFieldsMixin.class)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        itsTypeMappings = new HashMap<>();
    }

    /************************************************************************
     * Creates a new JsonObjectMapper.
     *
     */
    public
    JsonObjectMapper(ObjectMapper mapper)
    {
        itsMapper = mapper;
        itsTypeMappings = new HashMap<>();
    }

    /************************************************************************
     * {@inheritDoc} 
     */
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

    /************************************************************************
     * {@inheritDoc} 
     */
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

    /************************************************************************
     *  
     *
     * @param sourceType
     * @param destType
     * @return
     */
    public JsonObjectMapper<T>
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

    /************************************************************************
     *  
     *
     * @param payload
     * @return
     */
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
