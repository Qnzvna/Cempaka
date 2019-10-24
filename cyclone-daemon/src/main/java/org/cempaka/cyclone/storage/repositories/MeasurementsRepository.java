package org.cempaka.cyclone.storage.repositories;

import com.google.common.collect.Range;
import java.util.List;
import org.cempaka.cyclone.beans.MetricDataPoint;

public interface MeasurementsRepository
{
    List<MetricDataPoint> getMetrics(String testRunId, Range<Long> timeRange);

    List<MetricDataPoint> getMetrics(String testRunId, String name, Range<Long> timeRange);

    void put(String testRunId, MetricDataPoint metricDataPoint);
}
