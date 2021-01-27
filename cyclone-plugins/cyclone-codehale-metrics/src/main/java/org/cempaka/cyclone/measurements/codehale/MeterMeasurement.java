package org.cempaka.cyclone.measurements.codehale;

import com.codahale.metrics.Meter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.cempaka.cyclone.core.measurements.Measurement;

public class MeterMeasurement extends Measurement
{
    private final Meter meter;

    public MeterMeasurement(final String name)
    {
        super(name);
        this.meter = new Meter();
    }

    public void mark()
    {
        meter.mark();
    }

    public void mark(final long n)
    {
        meter.mark(n);
    }

    @Override
    public Map<String, Double> getSnapshot()
    {
        final HashMap<String, Double> snapshot = new HashMap<>();
        snapshot.put("m_rate", meter.getMeanRate());
        snapshot.put("1m_rate", meter.getOneMinuteRate());
        snapshot.put("5m_rate", meter.getFiveMinuteRate());
        snapshot.put("15m_rate", meter.getFifteenMinuteRate());
        return Collections.unmodifiableMap(snapshot);
    }
}
