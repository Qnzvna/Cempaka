package me.dserwin.storm.invoker;

public interface Invoker
{
    void invokeBefore();

    void invoke();

    void invokeAfter();
}
