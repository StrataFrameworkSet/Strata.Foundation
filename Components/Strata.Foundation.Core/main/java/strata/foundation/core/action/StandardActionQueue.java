//////////////////////////////////////////////////////////////////////////////
// StandardMessageQueue.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.action;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <p>
 * Standard implementation of the {@code IActionQueue} interface that
 * provides an ordered queue of {@code IAction} instances with
 * setup and teardown registration and sequential execution
 * using {@code ConcurrentLinkedQueue} for thread-safe action management.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Default construction
 * IActionQueue queue = new StandardActionQueue();
 *
 * // Construction with custom queue
 * IActionQueue queue = new StandardActionQueue(new LinkedList&lt;&gt;());
 *
 * // Register setup/teardown and execute
 * queue
 *     .register(() -> openConnection(),() -> closeConnection())
 *     .insert(() -> processData())
 *     .execute();
 * </pre>
 */
public
class StandardActionQueue
    implements IActionQueue
{
    private final Queue<IAction> setups;
    private final Queue<IAction> teardowns;
    private final Queue<IAction> actions;

    public
    StandardActionQueue()
    {
        this(new ConcurrentLinkedQueue<>());
    }

    public
    StandardActionQueue(Queue<IAction> imp)
    {
        setups = new ConcurrentLinkedQueue<>();
        teardowns = new ConcurrentLinkedQueue<>();
        actions = imp;
    }

    @Override
    public IActionQueue
    register(IAction setup,IAction teardown)
    {
        setups.add(setup);
        teardowns.add(teardown);
        return this;
    }

    @Override
    public IActionQueue
    setUp()
        throws Exception
    {
        for (IAction setup:setups)
            setup.execute();

        return this;
    }

    @Override
    public IActionQueue
    tearDown()
        throws Exception
    {
        for (IAction teardown:teardowns)
            teardown.execute();

        return this;
    }

    @Override
    public IActionQueue
    insert(IAction action)
    {
        actions.add(action);
        return this;
    }

    @Override
    public IAction
    remove()
    {
        return actions.remove();
    }

    @Override
    public IActionQueue
    clear()
    {
        actions.clear();
        return this;
    }

    @Override
    public IActionQueue
    execute() throws Exception
    {
        try
        {
            setUp();

            while (!actions.isEmpty())
                actions
                    .remove()
                    .execute();

            return this;
        }
        finally
        {
            tearDown();
        }
    }

    @Override
    public boolean
    isEmpty()
    {
        return actions.isEmpty();
    }
}

//////////////////////////////////////////////////////////////////////////////
