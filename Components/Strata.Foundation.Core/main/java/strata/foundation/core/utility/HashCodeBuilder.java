// ##########################################################################
// # File Name:	HashCodeBuilder.java
// ##########################################################################

package strata.foundation.core.utility;

/****************************************************************************
 * Builds hash codes by appending the properties of a class that contribute
 * to its hash code value.
 */
public 
class HashCodeBuilder
{
    private static final int PRIME_ENCODER = 127;
    
    private int              itsHashCode;
   
    /************************************************************************
     * Creates a new HashCodeBuilder with a default seed value of 37.
     *
     */
    public 
    HashCodeBuilder()
    {
        this( 37 );
    }
    
    /************************************************************************
     * Creates a new HashCodeBuilder. 
     *
     * @param seed value that seeds the hash code (should be prime)
     */
    public 
    HashCodeBuilder(int seed)
    {
        itsHashCode = seed;
    }
    
    /************************************************************************
     * Appends a property to a class's hash code. 
     *
     * @param  property a property that contributes to a class's hash code
     * @return this {@code HashCodeBuilder} to enable method chaining
     */
    public HashCodeBuilder
    append(int property)
    {
        itsHashCode = PRIME_ENCODER * itsHashCode + property;
        return this;
    }
        
    /************************************************************************
     * Appends a property to a class's hash code. 
     *
     * @param  property a property that contributes to a class's hash code
     * @return this {@code HashCodeBuilder} to enable method chaining
     */
    public HashCodeBuilder
    append(boolean property)
    {
        return append( property ? 1 : 0 );
    }

    /************************************************************************
     * Appends a property to a class's hash code. 
     *
     * @param  property a property that contributes to a class's hash code
     * @return this {@code HashCodeBuilder} to enable method chaining
     */
    public HashCodeBuilder
    append(float property)
    {
        return append( Float.floatToIntBits( property ) );
    }
    
    /************************************************************************
     * Appends a property to a class's hash code. 
     *
     * @param  property a property that contributes to a class's hash code
     * @return this {@code HashCodeBuilder} to enable method chaining
     */
    public HashCodeBuilder
    append(double property)
    {
        return append( (int)Double.doubleToLongBits( property ) );
    }
    
    /************************************************************************
     * Appends a property to a class's hash code. 
     *
     * @param  property a property that contributes to a class's hash code
     * @return this {@code HashCodeBuilder} to enable method chaining
     */
    public HashCodeBuilder
    append(String property)
    {
        return 
            append(property != null ? property.hashCode() : "".hashCode());
    }
    
    /************************************************************************
     * Gets the currently built hash code value. 
     *
     * @return currently built hash code value
     */
    public int
    getHashCode()
    {
        return itsHashCode;
    }
}

// ##########################################################################
