//////////////////////////////////////////////////////////////////////////////
// StandardMessageQueue.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.action;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/*****************************************************************************
 * Standard implementation of {@code IActionQueue} interface.
 */
public
class StandardActionQueue
    implements IActionQueue
{
    private final Queue<IAction> setups;
    private final Queue<IAction> teardowns;
    private final Queue<IAction> actions;

    /*************************************************************************
     * Creates a new instance of {@code StandardMessageQueue<T,M>}.
     */
    public
    StandardActionQueue()
    {
        this(new ConcurrentLinkedQueue<>());
    }

    /*************************************************************************
     * Creates a new instance of {@code StandardMessageQueue<T,M>}.
     */
    public
    StandardActionQueue(Queue<IAction> imp)
    {
        setups = new ConcurrentLinkedQueue<>();
        teardowns = new ConcurrentLinkedQueue<>();
        actions = imp;
    }

    /*************************************************************************
     * {@inheritDoc}
     * @param setup
     * @param teardown
     */
    @Override
    public IActionQueue
    register(IAction setup,IAction teardown)
    {
        setups.add(setup);
        teardowns.add(teardown);
        return this;
    }

    /*************************************************************************
     * {@inheritDoc}
     */
    @Override
    public IActionQueue
    setUp()
        throws Exception
    {
        for (IAction setup:setups)
            setup.execute();

        return this;
    }

    /*************************************************************************
     * {@inheritDoc}
     */
    @Override
    public IActionQueue
    tearDown()
        throws Exception
    {
        for (IAction teardown:teardowns)
            teardown.execute();

        return this;
    }

    /*************************************************************************
     * {@inheritDoc}
     */
    @Override
    public IActionQueue
    insert(IAction action)
    {
        actions.add(action);
        return this;
    }

    /*************************************************************************
     * {@inheritDoc}
     */
    @Override
    public IAction
    remove()
    {
        return actions.remove();
    }

    /*************************************************************************
     * {@inheritDoc}
     */
    @Override
    public IActionQueue
    clear()
    {
        actions.clear();
        return this;
    }

    /*************************************************************************
     * {@inheritDoc}
     */
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

    /*************************************************************************
     * {@inheritDoc}
     */
    @Override
    public boolean
    isEmpty()
    {
        return actions.isEmpty();
    }
}

//////////////////////////////////////////////////////////////////////////////
