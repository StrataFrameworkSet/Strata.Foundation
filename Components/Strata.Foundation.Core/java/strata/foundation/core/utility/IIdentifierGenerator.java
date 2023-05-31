// ##########################################################################
// # File Name: IIdentifierGenerator.java
// # Copyright: 2016, SomethingPay, LLC. All Rights Reserved.
// ##########################################################################

package strata.foundation.core.utility;

/****************************************************************************
 * Generates numeric and string-based identifiers.
 * 
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
}

// ##########################################################################
