//////////////////////////////////////////////////////////////////////////////
// PrototypeScoped.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.BeanDefinition;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that indicates a bean should use the prototype scope.
 * <p>
 * This is a convenience annotation that is equivalent to:
 * <pre class="code">
 * {@code
 * @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
 * }
 * </pre>
 * <p>
 * With this annotation, a new instance of the bean will be created
 * each time it is requested from the application context.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public @interface PrototypeScoped {}

//////////////////////////////////////////////////////////////////////////////
