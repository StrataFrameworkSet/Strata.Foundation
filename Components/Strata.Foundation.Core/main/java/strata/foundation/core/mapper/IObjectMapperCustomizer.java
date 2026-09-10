//////////////////////////////////////////////////////////////////////////////
// IObjectMapperCustomizer.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import strata.foundation.core.utility.ICustomizer;

/**
 * <p>
 * Applies a set of configuration settings, features, and modules to a
 * Jackson {@link ObjectMapper}, returning the customized instance.
 * Implementations are typically consulted by an
 * {@link IObjectMapperSupplier} when constructing the shared
 * {@link ObjectMapper} used throughout the application.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * IObjectMapperCustomizer customizer = new ObjectMapperCustomizer();
 *
 * ObjectMapper mapper = customizer.customize(new ObjectMapper());
 * </pre>
 */
public
interface IObjectMapperCustomizer
    extends ICustomizer<ObjectMapper>
{}

//////////////////////////////////////////////////////////////////////////////
