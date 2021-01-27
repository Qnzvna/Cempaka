package org.cempaka.cyclone.core.measurements;

import org.cempaka.cyclone.core.runners.ExecutionContext;

/**
 * {@code Ticker} that increments {@code CounterMeasurement} when test method returns successfully
 */
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
