package org.cempaka.cyclone.annotations;

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
 * By default all exceptions thrown during executing load tests will immediately stop entire run. To
 * prevent such situation {@code Thunderbolt} annotation has two parameters. First {@code
 * suppressAllThrowables()} can be used to suppress all exceptions thrown inside test method. Second
 * {@code suppressThrowables} accept a list of throwable classes that will be suppressed.
 * <p>
 * Be aware that test classes that has methods annotated with {@code Thunderbolt} annotation should
 * not have any state or that state should be synchronized. In performance testing to achieve the
 * best results code will be most likely run simultaneously on different threads or even machines.
 * Keep that in mind when implementing thunderbolts body.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Thunderbolt
{
    boolean suppressAllThrowables() default false;

    Class<? extends Throwable>[] suppressedThrowables() default {};
}
