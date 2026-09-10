//////////////////////////////////////////////////////////////////////////////
// HashCodeBuilder.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Optional;

/**
 * <p>
 * Incrementally builds a hash code by combining property values using a
 * consistent prime-multiplier algorithm, avoiding the need to hand-write
 * {@link Object#hashCode()} implementations.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * int hashCode =
 *     new HashCodeBuilder()
 *         .append(name)
 *         .append(age)
 *         .getHashCode();
 * </pre>
 */
public
class HashCodeBuilder
{
    private static final int PRIME_ENCODER = 127;
    
    private int              itsHashCode;
   
    public
    HashCodeBuilder()
    {
        this( 37 );
    }
    
    public
    HashCodeBuilder(int seed)
    {
        itsHashCode = seed;
    }
    
    public HashCodeBuilder
    append(int property)
    {
        itsHashCode = PRIME_ENCODER * itsHashCode + property;
        return this;
    }
        
    public HashCodeBuilder
    append(boolean property)
    {
        return append( property ? 1 : 0 );
    }

    public HashCodeBuilder
    append(float property)
    {
        return append( Float.floatToIntBits( property ) );
    }
    
    public HashCodeBuilder
    append(double property)
    {
        return append( (int)Double.doubleToLongBits( property ) );
    }
    
    public HashCodeBuilder
    append(String property)
    {
        return 
            append(property != null ? property.hashCode() : "".hashCode());
    }

    public <T> HashCodeBuilder
    append(Optional<T> property)
    {
        return
            OptionalExtension
                .ifPresentOrElse(
                    property,
                    value -> append(value.hashCode()),
                    () -> append(0));
    }
    
    public int
    getHashCode()
    {
        return itsHashCode;
    }
}

//////////////////////////////////////////////////////////////////////////////
