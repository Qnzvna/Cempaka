package org.cempaka.cyclone.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Thunderbolt
{
    boolean suppressAllThrowables() default false;

    Class<? extends Throwable>[] suppressedThrowables() default {};
}
