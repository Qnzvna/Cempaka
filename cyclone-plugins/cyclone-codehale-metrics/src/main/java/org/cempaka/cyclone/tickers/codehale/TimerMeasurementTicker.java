package org.cempaka.cyclone.tickers.codehale;

import java.util.concurrent.TimeUnit;
import org.cempaka.cyclone.invoker.ExecutionContext;
import org.cempaka.cyclone.measurements.Measurement;
import org.cempaka.cyclone.measurements.codehale.TimerMeasurement;
import org.cempaka.cyclone.measurements.tickers.Ticker;

public class TimerMeasurementTicker implements Ticker
{
    @Override
    public void tick(final Measurement measurement, final ExecutionContext executionContext)
    {
        if (measurement instanceof TimerMeasurement) {
            ((TimerMeasurement) measurement).update(executionContext.getExecutionTime(), TimeUnit.MILLISECONDS);
        }
    }
}
