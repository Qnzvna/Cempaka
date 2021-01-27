package org.cempaka.cyclone.core.measurements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to count successes and failures of the test method.
 *
 * {@link CounterMeasurement}
 */
@Measured(value = CounterMeasurement.class, name = "success", ticker = SuccessCounterTicker.class)
@Measured(value = CounterMeasurement.class, name = "failure", ticker = FailureCounterTicker.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Counted
{
}
