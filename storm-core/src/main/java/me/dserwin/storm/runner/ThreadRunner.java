package me.dserwin.storm.runner;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.time.Duration;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import me.dserwin.storm.invoker.Invoker;

public class ThreadRunner extends ForwardingRunner
{
    private final Runner delegated;
    private final ExecutorService executorService;
    private final Queue<Future> futures = Queues.newLinkedBlockingQueue();

    public ThreadRunner(final Runner delegated, final int threadNum)
    {
        this.delegated = checkNotNull(delegated);
        checkArgument(threadNum > 0, "Number of threads has to be greater than 0.");
        this.executorService = Executors.newFixedThreadPool(threadNum, new ThreadFactoryBuilder()
            .setNameFormat("ThreadRunner-%s")
            .build());
    }

    @Override
    Runner getDelegate()
    {
        return delegated;
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
