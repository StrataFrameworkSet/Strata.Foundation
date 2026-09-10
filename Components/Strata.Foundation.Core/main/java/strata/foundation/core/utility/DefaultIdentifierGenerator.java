//////////////////////////////////////////////////////////////////////////////
// DefaultIdentifierGenerator.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

/**
 * <p>
 * Default {@link IIdentifierGenerator} implementation that generates random
 * numeric-based identifiers using a {@link SecureRandom} source and random
 * string-based identifiers derived from a {@link UUID}.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * IIdentifierGenerator generator = new DefaultIdentifierGenerator();
 *
 * Integer id   = generator.getNextIntegerId(1000);
 * String  uuid = generator.getNextStringId();
 * </pre>
 */
public
class DefaultIdentifierGenerator
    implements IIdentifierGenerator
{
    private final Random itsSource;
    
    /************************************************************************
     * Creates a new DefaultIdentifierGenerator. 
     *
     */
    public 
    DefaultIdentifierGenerator()
    {
        itsSource = new SecureRandom();
    }

    /************************************************************************
     * {@inheritDoc} 
     */
    @Override
    public Integer
    getNextIntegerId()
    {
        int id = itsSource.nextInt();
        
        return id >= 0 ? id : -id;
    }

    /************************************************************************
     * {@inheritDoc} 
     */
    @Override
    public Integer
    getNextIntegerId(int upperBound)
    {
        return itsSource.nextInt(upperBound);
    }

    /************************************************************************
     * {@inheritDoc} 
     */
    @Override
    public Integer
    getNextIntegerId(int lowerBound,int upperBound)
    {
        if ( lowerBound > upperBound )
            throw 
                new IllegalArgumentException(
                    "lowerBound must be < upperBound" );
        
        return lowerBound + itsSource.nextInt(upperBound - lowerBound);
    }

    /************************************************************************
     * {@inheritDoc} 
     */
    @Override
    public Long
    getNextLongId()
    {
        long id = itsSource.nextLong();
        
        return id >= 0 ? id : -id; 
    }

    /************************************************************************
     * {@inheritDoc} 
     */
    @Override
    public Long
    getNextLongId(long upperBound)
    {
        long id = itsSource.nextLong();
        
        id = id >= 0 ? id : -id;
        return id % upperBound;
    }

    /************************************************************************
     * {@inheritDoc} 
     */
    @Override
    public Long
    getNextLongId(long lowerBound,long upperBound)
    {
        long offset;
        
        if ( lowerBound > upperBound )
            throw 
                new IllegalArgumentException(
                    "lowerBound must be < upperBound" );
        
        offset = itsSource.nextLong();
        offset = offset >= 0 ? offset : -offset;
        return lowerBound + (offset % (upperBound-lowerBound));
    }

    /************************************************************************
     * {@inheritDoc} 
     */
    @Override
    public String
    getNextStringId()
    {
        return UUID.randomUUID().toString();
    }

    @Override
    public String
    getNextStringId(int length)
    {
        return
            UUID
                .randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, Math.min(length, 32));
    }
}

//////////////////////////////////////////////////////////////////////////////
