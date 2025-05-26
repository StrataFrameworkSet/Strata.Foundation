/// ///////////////////////////////////////////////////////////////////////////
// OpenExtent.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.resource;

import java.util.Objects;

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
