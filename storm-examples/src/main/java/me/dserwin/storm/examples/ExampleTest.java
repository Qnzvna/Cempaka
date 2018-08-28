package me.dserwin.storm.examples;

import com.google.common.util.concurrent.Uninterruptibles;
import java.util.concurrent.TimeUnit;
import me.dserwin.storm.annotation.AfterStorm;
import me.dserwin.storm.annotation.BeforeStorm;
import me.dserwin.storm.annotation.Parameter;
import me.dserwin.storm.annotation.Thunderbolt;

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
    public void test1()
    {
        Uninterruptibles.sleepUninterruptibly(sleep, TimeUnit.SECONDS);
        System.out.println("run " + name + " on: " + Thread.currentThread().getName());
    }

    @AfterStorm
    public void after()
    {
        System.out.println("after");
    }
}
