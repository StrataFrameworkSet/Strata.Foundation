// ##########################################################################
// # File Name:	Triple.java
// ##########################################################################

package strata.foundation.core.container;

import strata.foundation.core.utility.HashCodeBuilder;

import java.util.Objects;

public 
class Quadruple<T1,T2,T3,T4>
{
    private final T1 itsFirst;
    private final T2 itsSecond;
    private final T3 itsThird;
    private final T4 itsFourth;

    public
    Quadruple(final T1 first,final T2 second,final T3 third,final T4 fourth)
    {
        itsFirst  = first;
        itsSecond = second;
        itsThird  = third;
        itsFourth = fourth;
    }

    public boolean
    equals(Quadruple<T1,T2,T3,T4> other)
    {
        return
            itsFirst.equals(other.getFirst()) &&
            itsSecond.equals(other.getSecond()) &&
            itsThird.equals(other.getThird()) &&
            itsFourth.equals(other.getFourth());
    }

    @Override
    public boolean 
    equals(Object other)
    {
        if ( other instanceof Quadruple<?,?,?,?> )
        {
            Quadruple<?,?,?,?> quadruple = (Quadruple<?,?,?,?>)other;
            
            return
                itsFirst.equals(quadruple.getFirst()) &&
                itsSecond.equals(quadruple.getSecond()) &&
                itsThird.equals(quadruple.getThird()) &&
                itsFourth.equals(quadruple.getFourth());
        }
        
        return false;
    }

    @Override
    public int 
    hashCode()
    {
        return
            new HashCodeBuilder(43)
                .append(itsFirst != null ? itsFirst.toString() : "")
                .append(itsSecond != null ? itsSecond.toString() : "")
                .append(itsThird != null ? itsThird.toString() : "" )
                .append(itsFourth != null ? itsFourth.toString() : "")
                .getHashCode();
    }

    @Override
    public String
    toString()
    {
        return
            new StringBuilder()
                .append(itsFirst)
                .append(',')
                .append(itsSecond)
                .append(',')
                .append(itsThird)
                .append(',')
                .append(itsFourth)
                .toString();
    }

    public T1
    getFirst()
    {
        return itsFirst;
    }
    
    public T2
    getSecond()
    {
        return itsSecond;
    }

    public T3
    getThird()
    {
        return itsThird;
    }

    public T4
    getFourth() { return itsFourth; }

    public static <T1,T2,T3,T4> Quadruple<T1,T2,T3,T4>
    create(final T1 first,final T2 second,final T3 third,final T4 fourth)
    {
        return new Quadruple<>(first,second,third,fourth);
    }
}

// ##########################################################################
