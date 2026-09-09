//////////////////////////////////////////////////////////////////////////////
// IAction.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.action;

/**
 * <p>
 * Represents a unit of work that can be executed.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Creation using lambda
 * IAction action = () -> System.out.println("executing");
 *
 * // Execution
 * action.execute();
 * </pre>
 * </p>
 */
public
interface IAction
{
    void
    execute() throws Exception;
}

//////////////////////////////////////////////////////////////////////////////