//////////////////////////////////////////////////////////////////////////////
// OpenExtent.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.resource;

import java.util.Objects;

/**
 * <p>
 * Opens an {@link IResource} if needed on construction and closes it if
 * needed when the extent itself is closed, so the resource is guaranteed to
 * be open for the lifetime of a try-with-resources block.
 * </p>
 * <br/>
 * <b>Type Parameter</b><br/>
 * {@code <R>} - the type of {@link IResource} being managed
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * try (OpenExtent&lt;FileResource&gt; extent = OpenExtent.of(resource))
 * {
 *     // resource is guaranteed to be open here
 * }
 * </pre>
 */
public
class OpenExtent<R extends IResource>
    implements AutoCloseable
{
    private final R resource;

    public
    OpenExtent(R resource)
        throws OpenFailedException
    {
        Objects.requireNonNull(resource, "resource must not be null");
        this.resource = resource;

        if (resource.isClosed())
        {
            try
            {
                resource.open();
            }
            catch (Exception e)
            {
                throw new OpenFailedException(e);
            }
        }
    }

    @Override
    public void
    close()
        throws CloseFailedException
    {
        try
        {
            if (resource.isOpen())
                resource.close();
        }
        catch (Throwable e)
        {
            throw new CloseFailedException(e);
        }
    }

    public static <R extends IResource> OpenExtent<R>
    of(R resource)
    {
        return new OpenExtent<>(resource);
    }
}

//////////////////////////////////////////////////////////////////////////////
