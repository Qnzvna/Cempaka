package org.cempaka.cyclone.core.runners;

interface Invoker
{
    void setThrottleMultiplier(long multiplier);

    void invokeBefore();

    void invoke();

    void invokeAfter();
}
