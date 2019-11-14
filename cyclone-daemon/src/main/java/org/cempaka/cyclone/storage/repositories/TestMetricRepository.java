package org.cempaka.cyclone.storage.repositories;

import com.google.common.collect.Range;
import java.util.List;
import java.util.UUID;
import org.cempaka.cyclone.beans.MetricDataPoint;

public interface TestMetricRepository
{
    List<MetricDataPoint> get(UUID executionId, Range<Long> timeRange);

    List<MetricDataPoint> get(UUID executionId, String name, Range<Long> timeRange);

    void put(UUID executionId, MetricDataPoint metricDataPoint);
}
