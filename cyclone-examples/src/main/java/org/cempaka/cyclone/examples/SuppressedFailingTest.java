package org.cempaka.cyclone.examples;

import org.cempaka.cyclone.annotations.SuppressThrowables;
import org.cempaka.cyclone.measurements.Counted;

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
