// ##########################################################################
// # File Name:	CollectionHelper.java
// ##########################################################################

package strata.foundation.core.collection;

import strata.foundation.core.utility.IMatcher;

import java.util.Collection;

/**
 * <p>
 * Utility class providing static helper methods for working
 * with {@link java.util.Collection} instances.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Find first matching element
 * String result = CollectionHelper.find(items,item -&gt; item.startsWith("foo"));
 * </pre>
 */
public
class CollectionHelper
{

    /************************************************************************
     *  
     *
     * @param collection
     * @param predicate
     * @return
     */
    public static <T> T
    find(Collection<T> collection,IMatcher<T> predicate)
    {
        for (T element:collection)
            if ( predicate.match( element ) )
                return element;
        
        return null;
    }
    
    /************************************************************************
     * Private no-op constructor because this is a utility class. 
     *
     */
    private 
    CollectionHelper() {}
}

// ##########################################################################
