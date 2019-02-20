package org.cempaka.cyclone.metrics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class MeasurementRegistry
{
    private final ConcurrentMap<String, Measurement> measurementStore;

    public MeasurementRegistry()
    {
        this.measurementStore = new ConcurrentHashMap<>();
    }

    public void register(final Measurement measurement)
    {
        measurementStore.put(measurement.name(), measurement);
    }

    public Map<String, Double> getSnapshots()
    {
        return measurementStore.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getSnapshot()));
    }
}
