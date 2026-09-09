//////////////////////////////////////////////////////////////////////////////
// Holder.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

/**
 * <p>
 * Holds a single mutable reference to a value of type {@code T}, useful for
 * capturing a value from within a lambda expression or anonymous inner
 * class where the enclosing variable must otherwise be effectively final.
 * </p>
 * <h4>Type Parameter</h4>
 * {@code <T>} - the type of the held value
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * Holder&lt;Integer&gt; total = new Holder&lt;Integer&gt;().setItem(0);
 *
 * items.forEach(i -&gt; total.setItem(total.getItem() + i));
 * </pre>
 * </p>
 */
public
class Holder<T>
{
    private T itsItem;

    public
    Holder() { itsItem = null; }

    public Holder<T>
    setItem(T item)
    {
        itsItem = item;
        return this;
    }

    public T
    getItem() { return itsItem; }
}

//////////////////////////////////////////////////////////////////////////////
