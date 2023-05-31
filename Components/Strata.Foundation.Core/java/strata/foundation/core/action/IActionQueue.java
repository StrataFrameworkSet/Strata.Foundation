//////////////////////////////////////////////////////////////////////////////
// IActionQueue.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.action;

public
interface IActionQueue
{
    IActionQueue
    register(IAction setup,IAction teardown);

    IActionQueue
    setUp() throws Exception;

    IActionQueue
    tearDown() throws Exception;

    IActionQueue
    insert(IAction action);

    IAction
    remove();

    IActionQueue
    clear();

    IActionQueue
    execute() throws Exception;

    boolean
    isEmpty();

}

//////////////////////////////////////////////////////////////////////////////