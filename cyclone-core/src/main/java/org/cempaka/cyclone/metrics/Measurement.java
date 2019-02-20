package org.cempaka.cyclone.metrics;

public interface Measurement
{
    String name();

    void start();

    void stop();

    double getSnapshot();
}