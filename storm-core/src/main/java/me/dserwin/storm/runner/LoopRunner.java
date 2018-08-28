package me.dserwin.storm.runner;

import static com.google.common.base.Preconditions.checkArgument;

import me.dserwin.storm.invoker.Invoker;

public class LoopRunner extends ForwardingRunner
{
    private final Runner delegateRunner;
    private final long loopCount;

    public LoopRunner(final Runner delegateRunner,
                      final long loopCount)
    {
        this.delegateRunner = delegateRunner;
        checkArgument(loopCount > 0, "loopCount must be greater than 0");
        this.loopCount = loopCount;
    }

    @Override
    Runner getDelegate()
    {
        return delegateRunner;
    }

    @Override
    public void invokeTest(final Invoker invoker)
    {
        for (int i = 0; i < loopCount; i++) {
            delegateRunner.invokeTest(invoker);
        }
    }
}
