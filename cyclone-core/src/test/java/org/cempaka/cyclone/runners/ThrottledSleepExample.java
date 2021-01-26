package org.cempaka.cyclone.runners;

import org.cempaka.cyclone.annotations.Throttle;
import org.cempaka.cyclone.annotations.Thunderbolt;

public class ThrottledSleepExample
{
    @Throttle(1)
    @Thunderbolt
    public void thunderbolt() throws InterruptedException
    {
        Thread.sleep(1100);
    }
}
