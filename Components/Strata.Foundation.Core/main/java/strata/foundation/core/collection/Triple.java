// ##########################################################################
// # File Name:	Triple.java
// ##########################################################################

package strata.foundation.core.collection;

import strata.foundation.core.utility.HashCodeBuilder;

import java.util.Objects;

/**
 * <p>
 * Container for a triple of data elements.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * Triple&lt;Integer,String,Boolean&gt; bar = Triple.of(1,"foo",true);
 *
 * // Selectors
 * Integer first  = bar.getFirst();
 * String  second = bar.getSecond();
 * Boolean third  = bar.getThird();
 * </pre>
 *
 * @param <F> first element of Triple
 * @param <S> second element of Triple
 * @param <T> third element of Triple
 */
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
        if (other instanceof Triple<?,?,?> triple)
            return
                Objects.equals(itsFirst,triple.getFirst()) &&
                Objects.equals(itsSecond,triple.getSecond()) &&
                Objects.equals(itsThird,triple.getThird());
        
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
                .append(Objects.toString(itsFirst,""))
                .append(Objects.toString(itsSecond,""))
                .append(Objects.toString(itsThird,""))
                .getHashCode();
    }

    /************************************************************************
     * {@inheritDoc} 
     */
    @Override
    public String
    toString()
    {
        return itsFirst + "," + itsSecond + "," + itsThird;
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
    @Deprecated
    public static <F,S,T> Triple<F,S,T>
    create(final F first,final S second,final T third)
    {
        return new Triple<>(first,second,third);
    }

    public static <F,S,T> Triple<F,S,T>
    of(final F first,final S second,final T third)
    {
        return new Triple<>(first,second,third);
    }
}

// ##########################################################################
