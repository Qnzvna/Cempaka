package org.cempaka.cyclone.tickers.codehale;

import org.cempaka.cyclone.invoker.ExecutionContext;
import org.cempaka.cyclone.measurements.Measurement;
import org.cempaka.cyclone.measurements.codehale.MeterMeasurement;
import org.cempaka.cyclone.measurements.tickers.Ticker;

public class MeterMeasurementTicker implements Ticker
{
    @Override
    public void tick(final Measurement measurement, final ExecutionContext executionContext)
    {
        if (measurement instanceof MeterMeasurement) {
            ((MeterMeasurement) measurement).mark();
        }
    }
}
