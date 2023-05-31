// ##########################################################################
// # File Name:	Pair.java
// ##########################################################################

package strata.foundation.core.container;

import strata.foundation.core.utility.HashCodeBuilder;

public 
class Pair<F,S>
{
    private final F itsFirst;
    private final S itsSecond;
    
    /************************************************************************
     * Creates a new {@code Pair}. 
     *
     * @param first
     * @param second
     */
    public 
    Pair(final F first,final S second)
    {
        itsFirst = first;
        itsSecond = second;
    }
    
    /************************************************************************
     * {@inheritDoc} 
     */
    @Override
    public boolean 
    equals(Object other)
    {
        if ( other instanceof Pair<?,?> )
        {
            Pair<?,?> pair = (Pair<?,?>)other;
            
            return
                itsFirst.equals( pair.getFirst() ) &&
                itsSecond.equals( pair.getSecond() );
        }
        
        return false;
    }

    /************************************************************************
     * {@inheritDoc} 
     */
    @Override
    public int 
    hashCode()
    {
        return
            new HashCodeBuilder(43)
                .append( itsFirst.toString() )
                .append( itsSecond.toString() )
                .getHashCode();
    }

    /************************************************************************
     * {@inheritDoc} 
     */
    @Override
    public String
    toString()
    {
        return itsFirst.toString() + "," + itsSecond.toString();
    }

    /************************************************************************
     *  
     *
     * @return
     */
    public F
    getFirst()
    {
        return itsFirst;
    }
    
    /************************************************************************
     *  
     *
     * @return
     */
    public S
    getSecond()
    {
        return itsSecond;
    }

    /************************************************************************
     *  
     *
     * @param first
     * @param second
     * @return
     */
    public static <F,S> Pair<F,S>
    create(final F first,final S second)
    {
        return new Pair<F,S>( first,second );
    }
}

// ##########################################################################
