/// ///////////////////////////////////////////////////////////////////////////
// Strings.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

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

}

//////////////////////////////////////////////////////////////////////////////
