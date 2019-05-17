package org.cempaka.cyclone.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.cempaka.cyclone.metrics.Measurement;

/**
 * Annotate thunderbolt method to enable gathering {@link Measurement}.
 * <p>
 * Example measurements:
 * <pre>
 * public class ExampleTest {
 *     &#064;Thunderbolt
 *     &#064;Measurements(measurements = {
 *         RateMeasurement.class,
 *         TimeMeasurement.class,
 *         ExecutionsMeasurement.class
 *     })
 *     public void measuredLoadTest() {
 *         ...
 *     }
 * }
 * </pre>
 * <p>
 * Cyclone will automatically start to measure and report metrics for such thunderbolt.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Measurements
{
    Class<? extends Measurement>[] measurements();
}