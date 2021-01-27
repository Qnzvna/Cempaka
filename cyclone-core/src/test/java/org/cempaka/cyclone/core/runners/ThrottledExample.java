package org.cempaka.cyclone.core.runners;

import org.cempaka.cyclone.core.annotations.Throttle;
import org.cempaka.cyclone.core.annotations.Thunderbolt;

public class ThrottledExample
{
    @Throttle(10)
    @Thunderbolt
    public void thunderbolt()
    {
    }
}
