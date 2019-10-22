package org.cempaka.cyclone.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate method to run it once before all test executions.
 * <p>
 * For example:
 * <pre>
 * public class ExampleTest {
 *     &#064;BeforeStorm
 *     public void runOnceBefore() {
 *         ...
 *     }
 *
 *     &#064;Thunderbolt
 *     public void performLoad() {
 *         ...
 *     }
 * }
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface BeforeStorm
{
}
