/// ///////////////////////////////////////////////////////////////////////////
// BearerToken.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.transfer;

import java.util.Objects;

public
class BearerToken
{
    private final String token;

    public
    BearerToken(String token)
    {
        Objects.requireNonNull(token,"Token must not be null");

        this.token =
            token.startsWith("Bearer ")
                ? token
                : "Bearer " + token;
    }

    public String
    toString()
    {
        return token;
    }

    public static BearerToken
    of(String token)
    {
        return new BearerToken(token);
    }
}

//////////////////////////////////////////////////////////////////////////////
