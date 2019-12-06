package org.cempaka.cyclone.invoker;

import org.cempaka.cyclone.annotations.Throttle;
import org.cempaka.cyclone.annotations.Thunderbolt;

public class ThrottledExample
{
    @Throttle(10)
    @Thunderbolt
    public void thunderbolt()
    {
    }
}
