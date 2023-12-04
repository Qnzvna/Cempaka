package org.cempaka.cyclone.core.runners;


import static org.cempaka.cyclone.core.utils.Preconditions.checkArgument;
import static org.cempaka.cyclone.core.utils.Preconditions.checkNotNull;

import java.time.Duration;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class ThreadRunner extends ForwardingRunner
{
    private final Runner delegated;
    private final int poolSize;
    private final int warmups;
    private final ExecutorService executorService;
    private final Queue<Future<?>> futures = new LinkedBlockingQueue<>();

    public ThreadRunner(final Runner delegated, final int poolSize, int warmups)
    {
        this.delegated = checkNotNull(delegated);
        checkArgument(poolSize > 0, "Number of threads has to be greater than 0.");
        this.poolSize = poolSize;
        this.warmups = warmups;
        this.executorService = Executors.newFixedThreadPool(poolSize);
    }

    @Override
    Runner getDelegate()
    {
        return delegated;
    }

    @Override
    public void beforeInvocation(final Invoker invoker)
    {
        invoker.setThrottleMultiplier(poolSize);
        invoker.invokeBefore();
        for(int i = 0; i < warmups; i++) {
            invoker.invoke();
        }
    }

    @Override
    public void invokeTest(final Invoker invoker)
    {
        final Future<?> invocationFuture = executorService.submit(invoker::invoke);
        futures.add(invocationFuture);
    }

    @Override
    public void afterInvocation(final Invoker invoker)
    {
        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException ignore) {
            } catch (ExecutionException e) {
                throw new RuntimeException(e.getCause());
            }
        });
        invoker.invokeAfter();
    }

    public void awaitTermination(final Duration awaitTime) throws InterruptedException
    {
        executorService.shutdown();
        executorService.awaitTermination(awaitTime.toMillis(), TimeUnit.MILLISECONDS);
    }
}
