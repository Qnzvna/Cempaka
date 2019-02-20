package org.cempaka.cyclone.invoker;

public interface Invoker
{
    void invokeBefore();

    void invoke();

    void invokeAfter();
}
