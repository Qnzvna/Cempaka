package org.cempaka.cyclone.storage;

import java.sql.Timestamp;
import java.util.List;
import org.cempaka.cyclone.TestRunMetric;
import org.cempaka.cyclone.storage.mappers.TestRunMetricRowMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface TestRunMetricDataAcess
{
    @SqlUpdate("INSERT INTO test_run_metrics (test_run_id, timestamp, type, name, value) VALUES (?, ?, ?, ?, ?)")
    void insertMetric(String testRunId, Timestamp timestamp, String type, String name, double value);

    @SqlQuery("SELECT timestamp, name, value FROM test_run_metrics WHERE test_run_id = ? ORDER BY timestamp DESC")
    @RegisterRowMapper(TestRunMetricRowMapper.class)
    List<TestRunMetric> getMetricsById(String testRunId);
}
