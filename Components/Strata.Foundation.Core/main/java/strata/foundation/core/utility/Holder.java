//////////////////////////////////////////////////////////////////////////////
// Holder.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

/**
 * <p>
 * Holds a single mutable reference to a value of type {@code T}, useful for
 * capturing a value from within a lambda expression or anonymous inner
 * class where the enclosing variable must otherwise be effectively final.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * Holder&lt;Integer&gt; total = new Holder&lt;Integer&gt;().setItem(0);
 *
 * items.forEach(i -&gt; total.setItem(total.getItem() + i));
 * </pre>
 *
 * @param <T> the type of the held value
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
