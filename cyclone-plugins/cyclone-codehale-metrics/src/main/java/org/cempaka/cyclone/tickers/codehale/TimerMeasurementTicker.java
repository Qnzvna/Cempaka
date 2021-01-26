package org.cempaka.cyclone.tickers.codehale;

import java.util.concurrent.TimeUnit;
import org.cempaka.cyclone.core.measurements.Measurement;
import org.cempaka.cyclone.core.measurements.Ticker;
import org.cempaka.cyclone.core.runners.ExecutionContext;
import org.cempaka.cyclone.measurements.codehale.TimerMeasurement;

public class TimerMeasurementTicker implements Ticker
{
    @Override
    public void tick(final Measurement measurement, final ExecutionContext executionContext)
    {
        if (measurement instanceof TimerMeasurement) {
            ((TimerMeasurement) measurement).update(executionContext.getMillisExecutionTime(), TimeUnit.MILLISECONDS);
        }
    }
}
