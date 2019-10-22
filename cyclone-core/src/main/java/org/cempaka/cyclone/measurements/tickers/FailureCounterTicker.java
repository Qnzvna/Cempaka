package org.cempaka.cyclone.measurements.tickers;

import org.cempaka.cyclone.invoker.ExecutionContext;
import org.cempaka.cyclone.measurements.CounterMeasurement;
import org.cempaka.cyclone.measurements.Measurement;

public class FailureCounterTicker implements Ticker
{
    @Override
    public void tick(final Measurement measurement, final ExecutionContext executionContext)
    {
        if (measurement instanceof CounterMeasurement) {
            if (executionContext.getThrowable() != null) {
                ((CounterMeasurement) measurement).increment();
            }
        }
    }
}
