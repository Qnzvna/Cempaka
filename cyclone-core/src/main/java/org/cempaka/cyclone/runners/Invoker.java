package org.cempaka.cyclone.runners;

interface Invoker
{
    void invokeBefore();

    void invoke();

    void invokeAfter();
}
