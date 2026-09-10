//////////////////////////////////////////////////////////////////////////////
// Strings.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

/**
 * <p>
 * A {@link String} utility class providing static helper methods for common
 * null/empty/blank checks and normalization, including testing whether a
 * string is null-or-empty or null-or-blank, converting empty/blank strings
 * to {@code null}, and trimming a string while returning {@code null} for
 * empty/blank input.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * Strings.isNullOrEmpty("");           // true
 * Strings.isNullOrBlank("   ");        // true
 * Strings.trimNonBlankOrNull("  hi "); // "hi"
 * Strings.toNonEmptyOrNull("");        // null
 * </pre>
 */
public
class Strings
{
    private Strings() {}

    public static boolean
    isNullOrEmpty(String value)
    {
        return value == null || value.isEmpty();
    }

    public static boolean
    isNullOrBlank(String value)
    {
        return value == null || value.isBlank();
    }

    public static String
    toNonEmptyOrNull(String value)
    {
        return
            isNullOrEmpty(value)
                ? null
                : value;
    }

    public static String
    toNonBlankOrNull(String value)
    {
        return
            isNullOrBlank(value)
                ? null
                : value;
    }

    public static String
    trimNonEmptyOrNull(String value)
    {
        return
            isNullOrEmpty(value)
                ? null
                : value.trim();
    }

    public static String
    trimNonBlankOrNull(String value)
    {
        return
            isNullOrBlank(value)
                ? null
                : value.trim();
    }

}

//////////////////////////////////////////////////////////////////////////////
