//////////////////////////////////////////////////////////////////////////////
// SingletonScoped.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.beans.factory.config.BeanDefinition;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that indicates a bean should use the singleton scope.
 * <p>
 * This is a convenience annotation that is equivalent to:
 * <pre class="code">
 * {@code
 * @Scope(value = BeanDefinition.SCOPE_SINGLETON)
 * }
 * </pre>
 * <p>
 * With this annotation, a single instance of the bean will be created
 * and shared across the entire application context.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public @interface SingletonScoped {}

//////////////////////////////////////////////////////////////////////////////
