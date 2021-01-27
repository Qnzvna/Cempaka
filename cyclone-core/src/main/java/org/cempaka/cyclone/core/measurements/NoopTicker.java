package org.cempaka.cyclone.core.measurements;

import org.cempaka.cyclone.core.runners.ExecutionContext;

class NoopTicker implements Ticker
{
    @Override
    public void tick(final Measurement measurement, final ExecutionContext executionContext)
    {
    }
}
