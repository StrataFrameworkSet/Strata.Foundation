//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

/**
 * <p>
 * Generates numeric and string-based identifiers. Implementations may
 * generate identifiers sequentially, randomly, or by any other scheme,
 * optionally bounding numeric identifiers to a given interval or
 * constraining string identifiers to a given length.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * IIdentifierGenerator generator = ...;
 *
 * Integer id     = generator.getNextIntegerId();
 * Integer capped = generator.getNextIntegerId(100);
 * String  token  = generator.getNextStringId(16);
 * </pre>
 */
public
interface IIdentifierGenerator
{
    /************************************************************************
     * Generates an integer-based identifier. 
     *
     * @return integer-based identifier
     */
    Integer
    getNextIntegerId();
    
    /************************************************************************
     * Generates an integer-based identifier in the 
     * interval [0..upperBound]. 
     *
     * @param  upperBound upper bound of identifier values
     * @return integer-based identifier
     */
    Integer
    getNextIntegerId(int upperBound);
    
    /************************************************************************
     * Generates an integer-based identifier in the 
     * interval [lowerBound..upperBound]. 
     *
     * @param  lowerBound lowerBound bound of identifier values
     * @param  upperBound upper bound of identifier values
     * @return integer-based identifier
     */
    Integer
    getNextIntegerId(int lowerBound,int upperBound);
    
    /************************************************************************
     * Generates an long-based identifier. 
     *
     * @return long-based identifier
     */
    Long
    getNextLongId();
    
    /************************************************************************
     * Generates an long-based identifier in the 
     * interval [0..upperBound]. 
     *
     * @param  upperBound upper bound of identifier values
     * @return long-based identifier
     */
    Long
    getNextLongId(long upperBound);
    
    /************************************************************************
     * Generates an long-based identifier in the 
     * interval [lowerBound..upperBound]. 
     *
     * @param  lowerBound lowerBound bound of identifier values
     * @param  upperBound upper bound of identifier values
     * @return long-based identifier
     */
    Long
    getNextLongId(long lowerBound,long upperBound);
    
    /************************************************************************
     * Generates a string-based identifier. 
     *
     * @return string-based identifier
     */
    String
    getNextStringId();

    /************************************************************************
     * Generates a string-based identifier of the specified length.
     *
     * @param  length length of the identifier
     * @return string-based identifier
     */
    String
    getNextStringId(int length);
}

//////////////////////////////////////////////////////////////////////////////
