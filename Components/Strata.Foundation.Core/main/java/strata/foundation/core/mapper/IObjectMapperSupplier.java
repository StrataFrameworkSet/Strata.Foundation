//////////////////////////////////////////////////////////////////////////////
// IObjectMapperSupplier.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.function.Supplier;

/**
 * <p>
 * Supplies a fully configured Jackson {@link ObjectMapper} instance,
 * typically built with a {@link com.fasterxml.jackson.databind.json.JsonMapper} builder and customized via
 * an {@link IObjectMapperCustomizer}. Implementations may return a new
 * instance on each call or a shared, pre-built instance.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * IObjectMapperSupplier supplier = new ObjectMapperSupplier();
 *
 * ObjectMapper mapper = supplier.get();
 * </pre>
 */
public
interface IObjectMapperSupplier
    extends Supplier<ObjectMapper> {}

//////////////////////////////////////////////////////////////////////////////
