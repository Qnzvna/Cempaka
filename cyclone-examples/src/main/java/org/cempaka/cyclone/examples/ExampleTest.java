package org.cempaka.cyclone.examples;

import org.cempaka.cyclone.annotation.AfterStorm;
import org.cempaka.cyclone.annotation.BeforeStorm;
import org.cempaka.cyclone.annotation.Measurements;
import org.cempaka.cyclone.annotation.Parameter;
import org.cempaka.cyclone.annotation.Thunderbolt;
import org.cempaka.cyclone.metrics.codehale.RateMeasurement;
import org.cempaka.cyclone.metrics.codehale.TimeMeasurement;

public class ExampleTest
{
    @Parameter(name = "sleep")
    private int sleep;

    @Parameter(name = "testName")
    private String name;

    @BeforeStorm
    public void before()
    {
        System.out.println("I'm gonna sleep for " + sleep);
    }

    @Thunderbolt
    @Measurements(measurements = {RateMeasurement.class, TimeMeasurement.class})
    public void test1() throws InterruptedException
    {
        Thread.sleep(sleep);
        System.out.println("run " + name + " on: " + Thread.currentThread().getName());
    }

    @AfterStorm
    public void after()
    {
        System.out.println("after");
    }
}
