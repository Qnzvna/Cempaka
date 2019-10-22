package org.cempaka.cyclone.measurements.tickers;

import org.cempaka.cyclone.invoker.ExecutionContext;
import org.cempaka.cyclone.measurements.Measurement;

public class NoopTicker implements Ticker
{
    @Override
    public void tick(final Measurement measurement, final ExecutionContext executionContext)
    {
    }
}
