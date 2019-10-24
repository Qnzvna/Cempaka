package org.cempaka.cyclone.measurements.codehale;

import com.codahale.metrics.Counter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.cempaka.cyclone.measurements.Measurement;

public class CounterMeasurement extends Measurement
{
    private final Counter counter;

    public CounterMeasurement(final String name)
    {
        super(name);
        counter = new Counter();
    }

    public void increment()
    {
        counter.inc();
    }

    public void increment(final long n)
    {
        counter.inc(n);
    }

    public void decrement()
    {
        counter.dec();
    }

    public void decrement(final long n)
    {
        counter.dec(n);
    }

    @Override
    public Map<String, Double> getSnapshot()
    {
        final HashMap<String, Double> snapshot = new HashMap<>();
        snapshot.put("count", (double) counter.getCount());
        return Collections.unmodifiableMap(snapshot);
    }
}
