package org.cempaka.cyclone.examples;

import org.cempaka.cyclone.core.annotations.AfterStorm;
import org.cempaka.cyclone.core.annotations.BeforeStorm;
import org.cempaka.cyclone.core.annotations.Parameter;
import org.cempaka.cyclone.core.annotations.Thunderbolt;
import org.cempaka.cyclone.core.measurements.Counted;

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

    @Counted
    @Thunderbolt
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
