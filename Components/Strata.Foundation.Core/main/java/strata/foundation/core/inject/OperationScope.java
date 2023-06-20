// ##########################################################################
// # File Name:	OperationScope.java
// #
// # Copyright:	2017, Sapientia Systems, LLC. All Rights Reserved.
// #
// # License:	This file is part of the StrataFoundation Framework.
// #
// #   			The StrataFoundation Framework is free software: you 
// #			can redistribute it and/or modify it under the terms of 
// #			the GNU Lesser General Public License as published by
// #    		the Free Software Foundation, either version 3 of the 
// #			License, or (at your option) any later version.
// #
// #    		The StrataFoundation Framework is distributed in the 
// #			hope that it will be useful, but WITHOUT ANY WARRANTY; 
// #			without even the implied warranty of MERCHANTABILITY or 
// #			FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser 
// #			General Public License for more details.
// #
// #    		You should have received a copy of the GNU Lesser 
// #			General Public License along with the StrataFoundation
// #			Framework. If not, see http://www.gnu.org/licenses/.
// ##########################################################################

package strata.foundation.core.inject;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/****************************************************************************
 *
 */
public 
class OperationScope
    implements Scope
{

    /************************************************************************
     * Creates a new OperationScope.
     *
     */
    public
    OperationScope() {}

    /************************************************************************
     * {@inheritDoc} 
     */
    @Override
    public <T> Provider<T> 
    scope(Key<T> key,Provider<T> source)
    {
        return
            new Provider<T>()
            {
                @Override
                public T get()
                {
                    Class<T> instanceClass = getInstanceType(key);
                    T instance = OperationContext.getInstance(instanceClass);

                    if (instance == null)
                    {
                        instance = source.get();
                        OperationContext.setInstance(instanceClass, instance);
                    }

                    return instance;
                }

                @Override
                public String
                toString()
                {
                    return String.format("%s[%s]", source, super.toString());
                }
            };
    }

    private static <T> Class<T>
    getInstanceType(Key<T> key)
    {
        Type type = key.getTypeLiteral().getType();

        if (type instanceof Class)
            return (Class<T>)type;

        if (type instanceof ParameterizedType)
            return (Class<T>)((ParameterizedType)type).getRawType();

        throw new ClassCastException("Unsupported type: " + type.getTypeName());
    }

}

// ##########################################################################
