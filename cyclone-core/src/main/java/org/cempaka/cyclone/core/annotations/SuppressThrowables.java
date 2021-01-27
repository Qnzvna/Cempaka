package org.cempaka.cyclone.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotations for suppressing throwables from tests.
 *
 * <p>
 * By default all exceptions thrown during executing load tests will immediately stop entire run. To prevent such
 * situation use {@code SuppressThrowables} annotation. You can define what kind of throwables you want to suppress or
 * leave the value empty and all exceptions will be ignored.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SuppressThrowables
{
    Class<? extends Throwable>[] value() default {};
}
