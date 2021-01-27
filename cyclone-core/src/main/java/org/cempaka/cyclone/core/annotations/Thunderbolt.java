package org.cempaka.cyclone.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code Thunderbolt} annotation tells cyclone to execute a method as a single load test.
 * <p>
 * Example test class:
 * <pre>
 * public class ExampleTest {
 *     &#064;Thunderbolt
 *     public void someLoad() {
 *         ...
 *     }
 *
 *     &#064;Thunderbolt
 *     public void evenMoreLoad() {
 *         ...
 *     }
 * }
 * </pre>
 * <p>
 * Be aware that test classes that has methods annotated with {@code Thunderbolt} annotation should not have any state
 * or that state should be synchronized. In performance testing to achieve the best results code will be most likely run
 * simultaneously on different threads or even machines. Keep that in mind when implementing thunderbolts body.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Thunderbolt
{
}
