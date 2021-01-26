package org.cempaka.cyclone.measurements;

import org.cempaka.cyclone.runners.ExecutionContext;

public class SuccessCounterTicker implements Ticker
{
    @Override
    public void tick(final Measurement measurement, final ExecutionContext executionContext)
    {
        if (measurement instanceof CounterMeasurement) {
            if (executionContext.getThrowable() == null) {
                ((CounterMeasurement) measurement).increment();
            }
        }
    }
}
