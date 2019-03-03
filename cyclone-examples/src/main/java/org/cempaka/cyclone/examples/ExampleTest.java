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
    private String name = "defaultName";

    private String value;

    @BeforeStorm
    public void before()
    {
        System.out.println("I'm gonna sleep for " + sleep);
        value = "before";
    }

    @Thunderbolt
    @Measurements(measurements = {RateMeasurement.class, TimeMeasurement.class})
    public void test1() throws InterruptedException
    {
        System.out.println("Before value: " + value);
        Thread.sleep(sleep);
        System.out.println("run " + name + " on: " + Thread.currentThread().getName());
        if (name == null) {
            throw new IllegalArgumentException();
        }
    }

    @AfterStorm
    public void after()
    {
        System.out.println("after");
    }
}
