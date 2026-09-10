//////////////////////////////////////////////////////////////////////////////
// IAction.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.action;

/**
 * <p>
 * Represents a unit of work that can be executed.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation using lambda
 * IAction action = () -> System.out.println("executing");
 *
 * // Execution
 * action.execute();
 * </pre>
 */
public
interface IAction
{
    void
    execute() throws Exception;
}

//////////////////////////////////////////////////////////////////////////////
