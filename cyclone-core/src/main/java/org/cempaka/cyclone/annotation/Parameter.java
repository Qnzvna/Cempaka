package org.cempaka.cyclone.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate primitive or {@code String} variable to inject external test parameters.
 * <p>
 * Example parameter:
 * <pre>
 * public class ExampleTest {
 *     &#064;Parameter(name = "port")
 *     private int port;
 *
 *     &#064;Thunderbolt
 *     public void someLoadOnApplicationOnPort() {
 *         ...
 *     }
 * }
 * </pre>
 * <p>
 * Cyclone will automatically inject values at the start of the test so thunderbolts methods can use
 * them.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Parameter
{
    String name();
}
