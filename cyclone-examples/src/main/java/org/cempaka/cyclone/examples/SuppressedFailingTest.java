package org.cempaka.cyclone.examples;

import org.cempaka.cyclone.annotations.Thunderbolt;
import org.cempaka.cyclone.measurements.Counted;

public class SuppressedFailingTest
{
    @Counted
    @SuppressWarnings("ConstantConditions")
    @Thunderbolt(suppressedThrowables = IllegalArgumentException.class)
    public void test1()
    {
        if (true) {
            throw new IllegalArgumentException();
        }
    }
}
