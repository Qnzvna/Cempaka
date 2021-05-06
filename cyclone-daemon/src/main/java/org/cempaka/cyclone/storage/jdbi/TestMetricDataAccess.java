package org.cempaka.cyclone.storage.jdbi;

import java.sql.Timestamp;
import java.util.List;
import org.cempaka.cyclone.beans.MetricDataPoint;
import org.cempaka.cyclone.storage.mappers.MetricDataPointRowMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface TestMetricDataAccess
{
    @SqlUpdate("INSERT INTO test_metrics (test_run_id, timestamp, name, value) VALUES (?, ?, ?, ?)")
    void insert(String testRunId, Timestamp timestamp, String name, double value);

    @RegisterRowMapper(MetricDataPointRowMapper.class)
    @SqlQuery("SELECT timestamp, name, value FROM test_metrics WHERE test_run_id = ? AND timestamp >= ? AND timestamp <= ? ORDER BY timestamp ASC")
    List<MetricDataPoint> get(String testRunId, Timestamp from, Timestamp to);

    @RegisterRowMapper(MetricDataPointRowMapper.class)
    @SqlQuery("SELECT timestamp, name, value FROM test_metrics WHERE test_run_id = ? AND name = ? AND timestamp >= ? AND timestamp <= ? ORDER BY timestamp ASC")
    List<MetricDataPoint> get(String testRunId, String name, Timestamp from, Timestamp to);
}
