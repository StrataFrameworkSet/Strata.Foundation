// ##########################################################################
// # File Name:	HashCodeBuilder.java
// ##########################################################################

package strata.foundation.core.utility;

import java.util.Optional;

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

// ##########################################################################
