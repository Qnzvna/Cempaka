package org.cempaka.cyclone.storage.jdbi;

import java.sql.Timestamp;
import java.util.List;
import org.cempaka.cyclone.beans.MetricDataPoint;
import org.cempaka.cyclone.storage.mappers.MetricDataPointRowMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface TestMetricsDataAccess
{
    @SqlUpdate("INSERT INTO test_metrics (test_run_id, timestamp, name, value) VALUES (?, ?, ?, ?)")
    void insert(String testRunId, Timestamp timestamp, String name, double value);

    @RegisterRowMapper(MetricDataPointRowMapper.class)
    @SqlQuery("SELECT timestamp, name, value FROM test_metrics WHERE test_run_id = ? AND from >= ? AND to <= ORDER BY timestamp DESC")
    List<MetricDataPoint> get(String testRunId, long from, long to);

    @RegisterRowMapper(MetricDataPointRowMapper.class)
    @SqlQuery("SELECT timestamp, name, value FROM test_metrics WHERE test_run_id = ? AND name = ? AND timestamp >= ? AND timestamp <= to ORDER BY timestamp DESC")
    List<MetricDataPoint> get(String testRunId, String name, long from, long to);
}
