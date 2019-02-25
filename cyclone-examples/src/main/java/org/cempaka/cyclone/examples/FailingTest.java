package org.cempaka.cyclone.examples;

import org.cempaka.cyclone.annotation.Thunderbolt;

public class FailingTest
{
    private int i = 0;

    @SuppressWarnings({"ConstantConditions", "DuplicateCondition"})
    @Thunderbolt(suppressedThrowables = {IllegalArgumentException.class})
    public void test1()
    {
        if (i == 0) {
            i++;
            throw new IllegalArgumentException();
        }
        System.out.println("Passed IAE.");
        if (true) {
            throw new IllegalStateException();
        }
    }
}
