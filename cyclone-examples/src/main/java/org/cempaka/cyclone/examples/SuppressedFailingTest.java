package org.cempaka.cyclone.examples;

import org.cempaka.cyclone.core.annotations.SuppressThrowables;
import org.cempaka.cyclone.core.measurements.Counted;

public class SuppressedFailingTest
{
    @Counted
    @SuppressWarnings("ConstantConditions")
    @SuppressThrowables(IllegalArgumentException.class)
    public void test1()
    {
        if (true) {
            throw new IllegalArgumentException();
        }
    }
}
