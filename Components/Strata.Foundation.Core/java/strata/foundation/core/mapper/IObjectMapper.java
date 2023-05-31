// ##########################################################################
// # File Name:	IObjectMapper.java
// ##########################################################################

package strata.foundation.core.mapper;

public 
interface IObjectMapper<T,P>
{
    <S extends T> P
    toPayload(S object);
    
    <S extends T> S
    toObject(Class<S> type,P payload);
}

// ##########################################################################
