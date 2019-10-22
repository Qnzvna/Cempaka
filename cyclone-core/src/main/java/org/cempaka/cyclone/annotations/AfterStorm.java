package org.cempaka.cyclone.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate method to run it once after all test executions.
 * <p>
 * For example:
 * <pre>
 * public class ExampleTest {
 *      &#064;Thunderbolt
 *      public void performLoad() {
 *          ...
 *      }
 *
 *     &#064;AfterStorm
 *     public void runOnceAfter() {
 *         ...
 *     }
 * }
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AfterStorm
{
}
