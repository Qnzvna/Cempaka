package org.cempaka.cyclone.storage.repositories;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Range;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.MetricDataPoint;
import org.cempaka.cyclone.storage.jdbi.TestMetricDataAccess;

@Singleton
public class JdbiTestMetricRepository implements TestMetricRepository
{
    private final TestMetricDataAccess testMetricDataAccess;

    @Inject
    public JdbiTestMetricRepository(final TestMetricDataAccess testMetricDataAccess)
    {
        this.testMetricDataAccess = checkNotNull(testMetricDataAccess);
    }

    @Override
    public List<MetricDataPoint> get(final String testRunId, final Range<Long> timeRange)
    {
        checkNotNull(testRunId);
        checkNotNull(timeRange);
        return testMetricDataAccess.get(testRunId, getFromTimestamp(timeRange), getToTimestamp(timeRange));
    }

    @Override
    public List<MetricDataPoint> get(final String testRunId, final String name, final Range<Long> timeRange)
    {
        checkNotNull(testRunId);
        checkNotNull(name);
        checkNotNull(timeRange);
        return testMetricDataAccess.get(testRunId, name, getFromTimestamp(timeRange), getToTimestamp(timeRange));
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
        testMetricDataAccess.insert(testRunId,
            new Timestamp(metricDataPoint.getTimestamp()),
            metricDataPoint.getName(),
            metricDataPoint.getValue());
    }
}
