package org.cempaka.cyclone.examples;

import org.cempaka.cyclone.core.annotations.Throttle;
import org.cempaka.cyclone.core.annotations.Thunderbolt;

public class ThrottledTest
{
    @Thunderbolt
    @Throttle(10)
    public void throttled()
    {
    }
}
