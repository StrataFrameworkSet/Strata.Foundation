// ##########################################################################
// # File Name:	Pair.java
// ##########################################################################

package strata.foundation.core.collection;

import strata.foundation.core.utility.HashCodeBuilder;

import java.util.Objects;

/**
 * <p>
 * Container for a pair of data elements.
 * </p>
 * <p>
 * <h4>Type Parameters</h4>
 * <ul>
 * <li>{@code <F>} - first element of Pair</li>
 * <li>{@code <S>} - second element of Pair</li>
 * </ul>
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Creation
 * Pair&lt;Integer,String&gt; foo = Pair.of(1,"foo");
 *
 * // Selectors
 * Integer first = foo.getFirst();
 * String second = foo.getSecond();
 * </pre>
 * </p>
 */
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
        if (other instanceof Pair<?,?> pair)
            return
                Objects.equals(itsFirst,pair.getFirst()) &&
                Objects.equals(itsSecond,pair.getSecond());
        
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
                .getHashCode();
    }

    /************************************************************************
     * {@inheritDoc} 
     */
    @Override
    public String
    toString()
    {
        return itsFirst + "," + itsSecond;
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
    @Deprecated
    public static <F,S> Pair<F,S>
    create(final F first,final S second)
    {
        return new Pair<F,S>( first,second );
    }

    public static <F,S> Pair<F,S>
    of(final F first,final S second)
    {
        return new Pair<>(first,second);
    }
}

// ##########################################################################
