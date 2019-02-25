package org.cempaka.cyclone.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.cempaka.cyclone.invoker.Invoker;
import org.cempaka.cyclone.invoker.ReflectiveInvoker;
import org.cempaka.cyclone.metrics.MeasurementRegistry;

public class SimpleRunner implements Runner
{
    private final List<Class> testClasses;
    private final List<Invoker> invokers;

    public SimpleRunner(final List<Class> testClasses,
                        final Map<String, String> parameters,
                        final MeasurementRegistry measurementRegistry)
    {
        this.testClasses = new ArrayList<>(testClasses);
        this.invokers = getTestClasses().stream()
            .map(testClass ->
                ReflectiveInvoker.forTestClass(testClass, parameters, measurementRegistry))
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
    public List<Class> getTestClasses()
    {
        return testClasses;
    }
}
