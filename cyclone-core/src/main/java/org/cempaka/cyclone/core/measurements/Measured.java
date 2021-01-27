package org.cempaka.cyclone.core.measurements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to aggregate measurements on different annotations.
 *
 * {@link Counted}
 */
@Repeatable(MultiMeasured.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
public @interface Measured
{
    /**
     * @return class of {@link Measurement} to use
     */
    Class<? extends Measurement> value();

    /**
     * @return name of the measurement
     */
    String name();

    /**
     * @return class of {@link Ticker} to use
     */
    Class<? extends Ticker> ticker();
}
