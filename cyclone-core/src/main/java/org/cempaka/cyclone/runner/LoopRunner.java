package org.cempaka.cyclone.runner;


import static org.cempaka.cyclone.utils.Preconditions.checkArgument;

import org.cempaka.cyclone.invoker.Invoker;

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
