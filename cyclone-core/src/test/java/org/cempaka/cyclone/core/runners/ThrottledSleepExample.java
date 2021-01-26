package org.cempaka.cyclone.core.runners;

import org.cempaka.cyclone.core.annotations.Throttle;
import org.cempaka.cyclone.core.annotations.Thunderbolt;

public class ThrottledSleepExample
{
    @Throttle(1)
    @Thunderbolt
    public void thunderbolt() throws InterruptedException
    {
        Thread.sleep(1100);
    }
}
