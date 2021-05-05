package org.cempaka.cyclone.core.runners;

import static org.cempaka.cyclone.core.utils.Preconditions.checkNotNull;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

class DurationRunner extends ForwardingRunner
{
    private final Runner delegateRunner;
    private final Duration duration;
    private final Clock clock;

    private Instant startTime;

    DurationRunner(final Runner delegateRunner, final Duration duration, final Clock clock)
    {
        this.delegateRunner = checkNotNull(delegateRunner);
        this.duration = checkNotNull(duration);
        this.clock = checkNotNull(clock);
    }

    @Override
    Runner getDelegate()
    {
        return delegateRunner;
    }

    @Override
    public void beforeInvocation(final Invoker invoker)
    {
        startTime = Instant.now(clock);
        invoker.invokeBefore();
    }

    @Override
    public void invokeTest(final Invoker invoker)
    {
        if (startTime.plus(duration).isAfter(Instant.now(clock))) {
            invoker.invoke();
            invokeTest(invoker);
        }
    }

    @Override
    public void awaitTermination(final Duration duration)
    {

    }
}
