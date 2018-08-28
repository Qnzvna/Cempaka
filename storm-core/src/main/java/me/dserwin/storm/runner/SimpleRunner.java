package me.dserwin.storm.runner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import me.dserwin.storm.invoker.Invoker;
import me.dserwin.storm.invoker.ReflectiveInvoker;

public class SimpleRunner implements Runner
{
    private final List<Class> testClasses;
    private final Map<String, String> parameters;

    public SimpleRunner(final List<Class> testClasses,
                        final Map<String, String> parameters)
    {
        this.testClasses = ImmutableList.copyOf(testClasses);
        this.parameters = ImmutableMap.copyOf(parameters);
    }

    @Override
    public Stream<Invoker> getInvokers()
    {
        return getTestClasses().stream().map(testClass -> ReflectiveInvoker.forTestClass(testClass, parameters));
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
