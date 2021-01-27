package org.cempaka.cyclone.core.runners;

import java.util.concurrent.atomic.LongAdder;

public abstract class AbstractTestExample
{
    private static LongAdder BEFORE_COUNTER = new LongAdder();
    private static LongAdder THUNDERBOLT_COUNTER = new LongAdder();
    private static LongAdder AFTER_COUNTER = new LongAdder();
    private static RuntimeException TO_THROW;
    private static Object PARAMETER_HOLDER;

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

    static Object getParameter()
    {
        return PARAMETER_HOLDER;
    }

    void before()
    {
        BEFORE_COUNTER.increment();
        throwIfWanted();
    }

    void thunder()
    {
        THUNDERBOLT_COUNTER.increment();
        setParameter();
        throwIfWanted();
    }

    void after()
    {
        AFTER_COUNTER.increment();
    }

    void setParameter()
    {
    }

    void setParameterHolder(final Object parameter)
    {
        PARAMETER_HOLDER = parameter;
    }

    private void throwIfWanted()
    {
        if (TO_THROW != null) {
            throw TO_THROW;
        }
    }
}
