package org.cempaka.cyclone.measurements.tickers;

import org.cempaka.cyclone.invoker.ExecutionContext;
import org.cempaka.cyclone.measurements.Measurement;

public interface Ticker
{
    void tick(Measurement measurement, ExecutionContext executionContext);
}
