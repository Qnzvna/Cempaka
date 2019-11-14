package org.cempaka.cyclone.storage.repositories;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Range;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
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
    public List<MetricDataPoint> get(final UUID executionId, final Range<Long> timeRange)
    {
        checkNotNull(executionId);
        checkNotNull(timeRange);
        return testMetricDataAccess.get(executionId.toString(), getFromTimestamp(timeRange), getToTimestamp(timeRange));
    }

    @Override
    public List<MetricDataPoint> get(final UUID executionId, final String name, final Range<Long> timeRange)
    {
        checkNotNull(executionId);
        checkNotNull(name);
        checkNotNull(timeRange);
        return testMetricDataAccess.get(executionId.toString(),
            name,
            getFromTimestamp(timeRange),
            getToTimestamp(timeRange));
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
    public void put(final UUID executionId, final MetricDataPoint metricDataPoint)
    {
        checkNotNull(executionId);
        checkNotNull(metricDataPoint);
        testMetricDataAccess.insert(executionId.toString(),
            new Timestamp(metricDataPoint.getTimestamp()),
            metricDataPoint.getName(),
            metricDataPoint.getValue());
    }
}
