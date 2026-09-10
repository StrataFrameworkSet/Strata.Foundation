//////////////////////////////////////////////////////////////////////////////
// IResource.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.resource;

/**
 * <p>
 * Represents a resource with an explicit, observable lifecycle, extending
 * {@link AutoCloseable} to support
 * <a href="https://en.wikipedia.org/wiki/Resource_management_(computing)">resource management</a>
 * with distinct open and closed states.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * try (IResource resource = new FileResource(path))
 * {
 *     resource.open();
 * }
 * </pre>
 */
public
interface IResource
    extends AutoCloseable
{
    void
    open() throws ResourceException;

    @Override
    void
    close() throws ResourceException;

    boolean
    isOpen();

    boolean
    isClosed();
}

//////////////////////////////////////////////////////////////////////////////
