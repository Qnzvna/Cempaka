package org.cempaka.cyclone.measurements.codehale;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.cempaka.cyclone.measurements.Measured;
import org.cempaka.cyclone.tickers.codehale.MeterMeasurementTicker;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Measured(value = MeterMeasurement.class, name = "meter", ticker = MeterMeasurementTicker.class)
public @interface Metered
{
}
