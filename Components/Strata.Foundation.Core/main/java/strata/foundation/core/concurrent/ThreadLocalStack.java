//////////////////////////////////////////////////////////////////////////////
// ThreadLocalStack.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.Stack;

/**
 * <p>
 * A thread-local {@link java.util.Stack} that maintains a separate
 * stack per thread using {@link ThreadLocal}.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Per-thread stack operations
 * ThreadLocalStack&lt;String&gt; stack = new ThreadLocalStack&lt;&gt;();
 * stack.push("first");
 * stack.push("second");
 *
 * String top = stack.peek();  // "second"
 * String pop = stack.pop();   // "second"
 * </pre>
 *
 * @param <T> - element type
 */
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
