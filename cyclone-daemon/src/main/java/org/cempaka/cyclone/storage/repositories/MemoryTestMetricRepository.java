package org.cempaka.cyclone.storage.repositories;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.MetricDataPoint;

@Singleton
public class MemoryTestMetricRepository implements TestMetricRepository
{
    private final Map<UUID, List<MetricDataPoint>> storage = Maps.newConcurrentMap();

    @Override
    public List<MetricDataPoint> get(final UUID executionId, final Range<Long> timeRange)
    {
        return storage.get(executionId).stream()
            .filter(metricDataPoint -> timeRange.contains(metricDataPoint.getTimestamp()))
            .collect(toImmutableList());
    }

    @Override
    public List<MetricDataPoint> get(final UUID executionId, final String name, final Range<Long> timeRange)
    {
        return get(executionId, timeRange).stream()
            .filter(metricDataPoint -> metricDataPoint.getName().equals(name))
            .collect(toImmutableList());
    }

    @Override
    public void put(final UUID executionId, final MetricDataPoint metricDataPoint)
    {
        storage.compute(executionId, (key, dataPoints) -> {
            if (dataPoints != null) {
                return ImmutableList.<MetricDataPoint>builder()
                    .addAll(dataPoints)
                    .add(metricDataPoint)
                    .build();
            } else {
                return ImmutableList.of(metricDataPoint);
            }
        });
    }
}
