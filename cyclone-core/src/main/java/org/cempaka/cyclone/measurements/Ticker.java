package org.cempaka.cyclone.measurements;

import org.cempaka.cyclone.runners.ExecutionContext;
import org.cempaka.cyclone.measurements.Measurement;

public interface Ticker
{
    void tick(Measurement measurement, ExecutionContext executionContext);
}
