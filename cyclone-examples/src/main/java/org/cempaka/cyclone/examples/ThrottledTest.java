package org.cempaka.cyclone.examples;

import org.cempaka.cyclone.annotations.Throttle;
import org.cempaka.cyclone.annotations.Thunderbolt;

public class ThrottledTest
{
    @Thunderbolt
    @Throttle(10)
    public void throttled()
    {
    }
}
