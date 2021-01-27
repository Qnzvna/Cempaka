package org.cempaka.cyclone.examples;

import org.cempaka.cyclone.core.annotations.Thunderbolt;

public class FailingTest
{
    @SuppressWarnings({"ConstantConditions"})
    @Thunderbolt
    public void test1()
    {
        if (true) {
            throw new IllegalStateException();
        }
    }
}
