package org.cempaka.cyclone.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.cempaka.cyclone.metrics.Measurement;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Measurements
{
    Class<? extends Measurement>[] measurements();
}