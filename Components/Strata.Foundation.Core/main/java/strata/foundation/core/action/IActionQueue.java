//////////////////////////////////////////////////////////////////////////////
// IActionQueue.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.action;

/**
 * <p>
 * Represents an ordered queue of {@code IAction} instances that
 * supports setup and teardown registration and sequential execution.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * IActionQueue queue = new StandardActionQueue();
 *
 * // Register setup and teardown
 * queue.register(() -> initialize(),() -> cleanup());
 *
 * // Insert and execute actions
 * queue
 *     .insert(() -> doFirst())
 *     .insert(() -> doSecond())
 *     .execute();
 * </pre>
 */
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
