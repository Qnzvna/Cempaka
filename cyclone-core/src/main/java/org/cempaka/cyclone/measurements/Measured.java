package org.cempaka.cyclone.measurements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.cempaka.cyclone.measurements.tickers.Ticker;

@Repeatable(MultiMeasured.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
public @interface Measured
{
    Class<? extends Measurement> value();

    String name();

    Class<? extends Ticker> ticker();
}
