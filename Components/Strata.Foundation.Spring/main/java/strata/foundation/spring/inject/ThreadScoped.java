//////////////////////////////////////////////////////////////////////////////
// ThreadScoped.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that indicates a bean should be scoped to the current thread.
 * <p>
 * This is a convenience annotation that is equivalent to:
 * <pre class="code">
 * {@code
 * @Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
 * }
 * </pre>
 * <p>
 * With this annotation, beans will be created once per thread and shared
 * within the same thread.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
public @interface ThreadScoped {}

//////////////////////////////////////////////////////////////////////////////