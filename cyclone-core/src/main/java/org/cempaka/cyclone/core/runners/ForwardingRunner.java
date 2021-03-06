package org.cempaka.cyclone.core.runners;

import java.util.List;
import java.util.stream.Stream;

abstract class ForwardingRunner implements Runner
{
    abstract Runner getDelegate();

    @Override
    public void beforeInvocation(final Invoker invoker)
    {
        getDelegate().beforeInvocation(invoker);
    }

    @Override
    public void invokeTest(final Invoker invoker)
    {
        getDelegate().invokeTest(invoker);
    }

    @Override
    public void afterInvocation(final Invoker invoker)
    {
        getDelegate().afterInvocation(invoker);
    }

    @Override
    public List<Class<?>> getTestClasses()
    {
        return getDelegate().getTestClasses();
    }

    @Override
    public Stream<Invoker> getInvokers()
    {
        return getDelegate().getInvokers();
    }
}
