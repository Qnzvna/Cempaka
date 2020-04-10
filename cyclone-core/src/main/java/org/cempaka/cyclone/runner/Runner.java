package org.cempaka.cyclone.runner;

import java.util.List;
import java.util.stream.Stream;
import org.cempaka.cyclone.invoker.Invoker;

public interface Runner
{
    default void run()
    {
        getInvokers().forEach(invoker -> {
            try {
                beforeInvocation(invoker);
                invokeTest(invoker);
            } finally {
                afterInvocation(invoker);
            }
        });
    }

    Stream<Invoker> getInvokers();

    void beforeInvocation(Invoker invoker);

    void invokeTest(Invoker invoker);

    void afterInvocation(Invoker invoker);

    List<Class<?>> getTestClasses();
}
