package org.cempaka.cyclone.core.runners;

interface Invoker
{
    void invokeBefore();

    void invoke();

    void invokeAfter();
}
