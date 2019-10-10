package org.cempaka.cyclone.invoker;

import java.util.concurrent.atomic.LongAdder;
import org.cempaka.cyclone.annotation.AfterStorm;
import org.cempaka.cyclone.annotation.BeforeStorm;
import org.cempaka.cyclone.annotation.Parameter;
import org.cempaka.cyclone.annotation.Thunderbolt;

public class TestExample
{
    private static LongAdder BEFORE_COUNTER = new LongAdder();
    private static LongAdder THUNDERBOLT_COUNTER = new LongAdder();
    private static LongAdder AFTER_COUNTER = new LongAdder();
    private static RuntimeException TO_THROW;
    private static String PARAMETER_HOLDER;

    @Parameter(name = "parameter")
    private String parameter;

    @BeforeStorm
    public void beforeStorm()
    {
        BEFORE_COUNTER.increment();
        throwIfWanted();
    }

    @Thunderbolt
    public void thunderbolt()
    {
        THUNDERBOLT_COUNTER.increment();
        PARAMETER_HOLDER = parameter;
        throwIfWanted();
    }

    private void throwIfWanted()
    {
        if (TO_THROW != null) {
            throw TO_THROW;
        }
    }

    @AfterStorm
    public void afterStorm()
    {
        AFTER_COUNTER.increment();
    }

    static void reset()
    {
        BEFORE_COUNTER.reset();
        THUNDERBOLT_COUNTER.reset();
        AFTER_COUNTER.reset();
        TO_THROW = null;
        PARAMETER_HOLDER = null;
    }

    static int getBeforeCounter()
    {
        return BEFORE_COUNTER.intValue();
    }

    static int getThunderboltCounter()
    {
        return THUNDERBOLT_COUNTER.intValue();
    }

    static int getAfterCounter()
    {
        return AFTER_COUNTER.intValue();
    }

    static void setThrow(final RuntimeException throwable)
    {
        TO_THROW = throwable;
    }

    static String getParameter()
    {
        return PARAMETER_HOLDER;
    }
}
