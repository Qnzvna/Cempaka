package org.cempaka.cyclone.measurements;

import org.cempaka.cyclone.runners.ExecutionContext;

public class NoopTicker implements Ticker
{
    @Override
    public void tick(final Measurement measurement, final ExecutionContext executionContext)
    {
    }
}
