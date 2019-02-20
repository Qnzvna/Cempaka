package org.cempaka.cyclone.metrics.codehale;


import com.codahale.metrics.Timer;
import org.cempaka.cyclone.metrics.Measurement;

abstract class TimerMeasurement implements Measurement
{
    protected final Timer timer = new Timer();

    protected Timer.Context context = null;

    @Override
    public void start()
    {
        context = timer.time();
    }

    @Override
    public void stop()
    {
        if (context != null) {
            context.stop();
        }
    }
}
