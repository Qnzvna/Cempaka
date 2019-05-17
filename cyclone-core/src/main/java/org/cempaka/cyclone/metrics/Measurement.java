package org.cempaka.cyclone.metrics;

import org.cempaka.cyclone.annotation.Thunderbolt;

/**
 * Represents measurement used for gathering test run metrics.
 */
public interface Measurement
{
    /**
     * @return name of measurement
     */
    String name();

    /**
     * Called upon start of {@link Thunderbolt}.
     */
    void start();

    /**
     * Called after {@link Thunderbolt} method call.
     */
    void stop();

    /**
     * @return value that will be reported
     */
    double getSnapshot();
}