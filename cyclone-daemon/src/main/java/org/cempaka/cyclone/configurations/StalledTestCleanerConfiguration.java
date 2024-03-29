package org.cempaka.cyclone.configurations;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class StalledTestCleanerConfiguration
{
    @Min(30)
    @Max(600)
    private int periodInterval = 60;
    @Min(60)
    @Max(300)
    private int awaitInterval = 60;

    public int getPeriodInterval()
    {
        return periodInterval;
    }

    public int getAwaitInterval()
    {
        return awaitInterval;
    }
}
