package org.cempaka.cyclone.runners;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.cempaka.cyclone.measurements.MeasurementRegistry;

class SimpleRunner implements Runner
{
    private final List<Class<?>> testClasses;
    private final List<Invoker> invokers;

    public SimpleRunner(final List<Class<?>> testClasses,
                        final Map<String, String> parameters,
                        final Map<String, String> metadata,
                        final MeasurementRegistry measurementRegistry)
    {
        this.testClasses = new ArrayList<>(testClasses);
        this.invokers = getTestClasses().stream()
            .map(testClass ->
                ReflectiveInvoker.forTestClass(testClass, parameters, metadata, measurementRegistry))
            .collect(Collectors.toList());
    }

    @Override
    public Stream<Invoker> getInvokers()
    {
        return invokers.stream();
    }

    @Override
    public void beforeInvocation(final Invoker invoker)
    {
        invoker.invokeBefore();
    }

    @Override
    public void invokeTest(final Invoker invoker)
    {
        invoker.invoke();
    }

    @Override
    public void afterInvocation(final Invoker invoker)
    {
        invoker.invokeAfter();
    }

    @Override
    public List<Class<?>> getTestClasses()
    {
        return testClasses;
    }

    @Override
    public void awaitTermination(final Duration duration)
    {
    }
}
