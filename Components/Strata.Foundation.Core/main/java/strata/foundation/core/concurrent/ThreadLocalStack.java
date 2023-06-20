//////////////////////////////////////////////////////////////////////////////
// ThreadLocalStack.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.Stack;

public
class ThreadLocalStack<T>
{
    private ThreadLocal<Stack<T>> itsImplementation;

    public
    ThreadLocalStack()
    {
        itsImplementation = new ThreadLocal<>();
    }

    public void
    push(T item) { getStack().push(item); }

    public T
    pop() { return getStack().pop(); }

    public T
    peek() { return getStack().peek(); }

    public boolean
    isEmpty() { return getStack().isEmpty(); }

    private Stack<T>
    getStack()
    {
        Stack<T> stack = itsImplementation.get();

        if (stack == null)
        {
            stack = new Stack<>();
            itsImplementation.set(stack);
        }

        return stack;
    }
}

//////////////////////////////////////////////////////////////////////////////
