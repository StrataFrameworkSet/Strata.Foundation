//////////////////////////////////////////////////////////////////////////////
// ICustomizer.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

/**
 * <p>
 * Applies a transformation or configuration step to a subject and returns
 * the resulting (possibly the same, possibly a different) instance. This
 * allows callers to plug in custom behavior for finishing, decorating, or
 * adjusting objects without the caller needing to know the details of how
 * the customization is performed.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * ICustomizer&lt;StringBuilder&gt; upperCaser = subject -&gt;
 * {
 *     subject.replace(0, subject.length(), subject.toString().toUpperCase());
 *     return subject;
 * };
 *
 * StringBuilder result = upperCaser.customize(new StringBuilder("hello"));
 * </pre>
 *
 * @param <T> - the type of the subject being customized
 */
public
interface ICustomizer<T>
{
    T
    customize(T subject);
}

//////////////////////////////////////////////////////////////////////////////
