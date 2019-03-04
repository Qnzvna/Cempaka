package org.cempaka.cyclone.examples;

import org.cempaka.cyclone.annotation.Thunderbolt;

public class SupressedFailingTest
{
    @SuppressWarnings({"ConstantConditions"})
    @Thunderbolt(suppressedThrowables = {IllegalArgumentException.class})
    public void test1()
    {
        if (true) {
            throw new IllegalArgumentException();
        }
    }
}
