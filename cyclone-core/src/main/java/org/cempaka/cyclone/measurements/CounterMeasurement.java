package org.cempaka.cyclone.measurements;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

public class CounterMeasurement extends Measurement
{
    private final LongAdder counter = new LongAdder();

    public CounterMeasurement(final String name)
    {
        super(name);
    }

    public void increment()
    {
        counter.increment();
    }

    @Override
    public Map<String, Double> getSnapshot()
    {
        final HashMap<String, Double> snapshot = new HashMap<>();
        snapshot.put("count", counter.doubleValue());
        return Collections.unmodifiableMap(snapshot);
    }
}
