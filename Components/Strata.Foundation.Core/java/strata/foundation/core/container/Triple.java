// ##########################################################################
// # File Name:	Triple.java
// ##########################################################################

package strata.foundation.core.container;

import strata.foundation.core.utility.HashCodeBuilder;

public 
class Triple<F,S,T>
{
    private final F itsFirst;
    private final S itsSecond;
    private final T itsThird;
    
    /************************************************************************
     * Creates a new {@code Triple}.
     *
     * @param first
     * @param second
     * @param third
     */
    public 
    Triple(final F first,final S second,final T third)
    {
        itsFirst  = first;
        itsSecond = second;
        itsThird  = third;
    }
    
    /************************************************************************
     * {@inheritDoc} 
     */
    @Override
    public boolean 
    equals(Object other)
    {
        if ( other instanceof Triple<?,?,?> )
        {
            Triple<?,?,?> triple = (Triple<?,?,?>)other;
            
            return
                itsFirst.equals( triple.getFirst() ) &&
                itsSecond.equals( triple.getSecond() ) &&
                itsThird.equals( triple.getThird() );
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
                .append( itsThird.toString() )
                .getHashCode();
    }

    /************************************************************************
     * {@inheritDoc} 
     */
    @Override
    public String
    toString()
    {
        return 
            itsFirst.toString() + "," + 
            itsSecond.toString() + "," +
            itsThird.toString();
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
     * @return
     */
    public T
    getThird()
    {
        return itsThird;
    }
    
    /************************************************************************
     *  
     *
     * @param first
     * @param second
     * @return
     */
    public static <F,S,T> Triple<F,S,T>
    create(final F first,final S second,final T third)
    {
        return new Triple<F,S,T>( first,second,third );
    }
}

// ##########################################################################
