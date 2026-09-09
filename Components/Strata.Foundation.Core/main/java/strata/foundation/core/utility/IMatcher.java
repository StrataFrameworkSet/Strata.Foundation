//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

/**
 * <p>
 * Determines whether a given target satisfies some criteria, following the
 * <a href="https://en.wikipedia.org/wiki/Specification_pattern">Specification pattern (Wikipedia)</a>.
 * Implementations encapsulate the matching logic so that callers can test
 * candidates against it without needing to know the details of what
 * constitutes a match.
 * </p>
 * <h4>Type Parameter</h4>
 * {@code <T>} - the type of target being tested for a match
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * IMatcher&lt;String&gt; nonEmpty = target -&gt; target != null &amp;&amp; !target.isEmpty();
 *
 * boolean matches = nonEmpty.match("hello");
 * </pre>
 * </p>
 */
public
interface IMatcher<T>
{
    boolean
    match(T target);
}

//////////////////////////////////////////////////////////////////////////////
