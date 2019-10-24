package org.cempaka.cyclone.storage.repositories;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Range;
import java.sql.Timestamp;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.MetricDataPoint;
import org.cempaka.cyclone.storage.jdbi.TestMetricsDataAccess;

@Singleton
public class JdbiMeasurementRepository implements MeasurementsRepository
{
    private final TestMetricsDataAccess testMetricsDataAccess;

    @Inject
    public JdbiMeasurementRepository(final TestMetricsDataAccess testMetricsDataAccess)
    {
        this.testMetricsDataAccess = checkNotNull(testMetricsDataAccess);
    }

    @Override
    public List<MetricDataPoint> getMetrics(final String testRunId, final Range<Long> timeRange)
    {
        checkNotNull(testRunId);
        checkNotNull(timeRange);
        final long from = timeRange.hasLowerBound() ? timeRange.lowerEndpoint() : 0;
        final long to = timeRange.hasUpperBound() ? timeRange.upperEndpoint() : Long.MAX_VALUE;
        return testMetricsDataAccess.get(testRunId, from, to);
    }

    @Override
    public List<MetricDataPoint> getMetrics(final String testRunId, final String name, final Range<Long> timeRange)
    {
        checkNotNull(testRunId);
        checkNotNull(name);
        checkNotNull(timeRange);
        final long from = timeRange.hasLowerBound() ? timeRange.lowerEndpoint() : 0;
        final long to = timeRange.hasUpperBound() ? timeRange.upperEndpoint() : Long.MAX_VALUE;
        return testMetricsDataAccess.get(testRunId, name, from, to);
    }

    @Override
    public void put(final String testRunId, final MetricDataPoint metricDataPoint)
    {
        checkNotNull(testRunId);
        checkNotNull(metricDataPoint);
        testMetricsDataAccess.insert(testRunId,
            new Timestamp(metricDataPoint.getTimestamp()),
            metricDataPoint.getName(),
            metricDataPoint.getValue());
    }
}
