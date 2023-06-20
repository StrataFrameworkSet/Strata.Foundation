// ##########################################################################
// # File Name:	JsonObjectMapper.java
// ##########################################################################

package strata.foundation.kafka.mapper;

import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import strata.foundation.core.mapper.IObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/****************************************************************************
 * 
 */
@SuppressWarnings("unchecked")
public
class AvroObjectMapper<T>
    implements IObjectMapper<T,String>
{
    private final Map<Class<?>,SpecificDatumWriter<?>> itsWriters;
    private final Map<Class<?>,SpecificDatumReader<?>> itsReaders;

    /************************************************************************
     * Creates a new JsonObjectMapper.
     *
     */
    public
    AvroObjectMapper()
    {
        itsWriters = new ConcurrentHashMap<>();
        itsReaders = new ConcurrentHashMap<>();
    }

    /************************************************************************
     * {@inheritDoc} 
     */
    @Override
    public <S extends T> String
    toPayload(S object)
    {
        ByteArrayOutputStream  output = new ByteArrayOutputStream();

        try
        {
            SpecificDatumWriter<S> writer =
                getWriter((Class<S>)object.getClass());
            Encoder encoder =
                EncoderFactory
                    .get()
                    .binaryEncoder(output,null);

            writer.write(object,encoder);
            encoder.flush();

            return
                Base64
                    .getEncoder()
                    .encodeToString(output.toByteArray());
        }
        catch (IOException e)
        {
            throw new IllegalStateException(e);
        }
        finally
        {
            try
            {
                output.close();
            }
            catch (IOException e)
            {
                throw new IllegalStateException(e);
            }
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
            SpecificDatumReader<S> reader = getReader(type);
            ByteArrayInputStream   input =
                new ByteArrayInputStream(
                    Base64
                        .getDecoder()
                        .decode(payload));

            return
                reader.read(
                    null,
                    DecoderFactory
                        .get()
                        .binaryDecoder(input,null));
        }
        catch (IOException e)
        {
            throw new IllegalStateException(e);
        }

    }

    private <S> SpecificDatumWriter<S>
    getWriter(Class<S> type)
    {
        if (itsWriters.containsKey(type))
            return (SpecificDatumWriter<S>)itsWriters.get(type);
        else
        {
            SpecificDatumWriter<S> writer = new SpecificDatumWriter<>(type);

            itsWriters.put(type,writer);
            return writer;
        }
    }

    private <S> SpecificDatumReader<S>
    getReader(Class<S> type)
    {
        if (itsReaders.containsKey(type))
            return (SpecificDatumReader<S>)itsReaders.get(type);
        else
        {
            SpecificDatumReader<S> reader = new SpecificDatumReader<>(type);

            itsReaders.put(type,reader);
            return reader;
        }
    }
}

// ##########################################################################
