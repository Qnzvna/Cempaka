package org.cempaka.cyclone.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotations for throttling executions of test method.
 *
 * <p>
 * By default test methods will be executed with maximum throughput. By adding {@code @Throttle} annotations the test
 * method will be slowed downed to specified throughput.
 *
 * Throughput is not synchronized between different cyclone instances.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Throttle
{
    /**
     * @return number of method executions per second
     */
    long value();
}
