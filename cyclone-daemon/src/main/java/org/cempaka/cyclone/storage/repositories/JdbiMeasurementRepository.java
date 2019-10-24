package org.cempaka.cyclone.storage.repositories;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Range;
import java.sql.Timestamp;
import java.time.Instant;
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
        return testMetricsDataAccess.get(testRunId, getFromTimestamp(timeRange), getToTimestamp(timeRange));
    }

    @Override
    public List<MetricDataPoint> getMetrics(final String testRunId, final String name, final Range<Long> timeRange)
    {
        checkNotNull(testRunId);
        checkNotNull(name);
        checkNotNull(timeRange);
        return testMetricsDataAccess.get(testRunId, name, getFromTimestamp(timeRange), getToTimestamp(timeRange));
    }

    private Timestamp getFromTimestamp(final Range<Long> timeRange)
    {
        return timeRange.hasLowerBound() ? new Timestamp(timeRange.lowerEndpoint()) : new Timestamp(0);
    }

    private Timestamp getToTimestamp(final Range<Long> timeRange)
    {
        return timeRange.hasUpperBound() ? new Timestamp(timeRange.upperEndpoint()) : Timestamp.from(Instant.now());
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
