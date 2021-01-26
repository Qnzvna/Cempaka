package org.cempaka.cyclone.core.measurements;

import org.cempaka.cyclone.core.runners.ExecutionContext;

/**
 * Invokes {@code Measurement} action based on state defined in {@code ExecutionContext}.
 */
public interface Ticker
{
    void tick(Measurement measurement, ExecutionContext executionContext);
}
