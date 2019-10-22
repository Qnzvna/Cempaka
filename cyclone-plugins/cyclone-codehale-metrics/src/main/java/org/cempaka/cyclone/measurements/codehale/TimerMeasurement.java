package org.cempaka.cyclone.measurements.codehale;

import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.cempaka.cyclone.measurements.Measurement;

public class TimerMeasurement extends Measurement
{
    private final Timer timer;

    public TimerMeasurement(final String name)
    {
        super(name);
        this.timer = new Timer();
    }

    @Override
    public Map<String, Double> getSnapshot()
    {
        final Snapshot snapshot = timer.getSnapshot();
        final HashMap<String, Double> metrics = new HashMap<>();
        metrics.put("median", snapshot.getMedian());
        metrics.put("mean", snapshot.getMean());
        metrics.put("max", (double) snapshot.getMax());
        metrics.put("min", (double) snapshot.getMin());
        metrics.put("p75", snapshot.get75thPercentile());
        metrics.put("p95", snapshot.get95thPercentile());
        metrics.put("p99", snapshot.get99thPercentile());
        metrics.put("p999", snapshot.get999thPercentile());
        return Collections.unmodifiableMap(metrics);
    }
}
