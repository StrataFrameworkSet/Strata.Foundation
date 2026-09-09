//////////////////////////////////////////////////////////////////////////////
// BearerToken.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.transfer;

import java.util.Objects;

/**
 * <p>
 * Wraps an HTTP <a href="https://en.wikipedia.org/wiki/Basic_access_authentication#Bearer_token">bearer token</a>
 * value, normalizing it so that it always carries the {@code "Bearer "}
 * scheme prefix required by the {@code Authorization} header. If the value
 * supplied at construction already starts with {@code "Bearer "} it is used
 * as-is; otherwise the prefix is added automatically.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * BearerToken token = BearerToken.of("eyJhbGciOiJIUzI1NiJ9...");
 * request.addHeader("Authorization",token.toString());
 * // Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
 * </pre>
 * </p>
 */
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
