// ##########################################################################
// # File Name: DefaultIdentifierGenerator.java
// # Copyright: 2016, SomethingPay, LLC. All Rights Reserved.
// ##########################################################################

package strata.foundation.core.utility;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

/****************************************************************************
 * Default implementation of {@code IIdentifierGenerator} interface by
 * generating random numeric-based identifiers and random UUID string-based
 * identifiers.
 *  
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

}

// ##########################################################################
