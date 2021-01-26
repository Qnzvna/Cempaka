package org.cempaka.cyclone.measurements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Measured(value = CounterMeasurement.class, name = "success", ticker = SuccessCounterTicker.class)
@Measured(value = CounterMeasurement.class, name = "failure", ticker = FailureCounterTicker.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Counted
{
}
