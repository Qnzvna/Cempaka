package org.cempaka.cyclone.configurations;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class TestRunnerConfiguration
{
    @Min(5)
    @Max(60)
    private int periodInterval = 10;
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
