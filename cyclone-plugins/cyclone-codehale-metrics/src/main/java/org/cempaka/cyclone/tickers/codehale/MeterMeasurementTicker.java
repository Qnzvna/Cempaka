package org.cempaka.cyclone.tickers.codehale;

import org.cempaka.cyclone.core.measurements.Measurement;
import org.cempaka.cyclone.core.measurements.Ticker;
import org.cempaka.cyclone.core.runners.ExecutionContext;
import org.cempaka.cyclone.measurements.codehale.MeterMeasurement;

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
