package org.cempaka.cyclone.storage;

import java.sql.Timestamp;
import java.util.List;
import org.cempaka.cyclone.beans.TestRunEvent;
import org.cempaka.cyclone.storage.mappers.TestRunEventRowMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface TestRunEventDataAccess
{
    @SqlUpdate("INSERT INTO test_run_events (test_run_id, timestamp, event_type) VALUES (?, ?, ?)")
    void insertEvent(String testRunId, Timestamp timestamp, String eventType);

    @SqlQuery("SELECT * FROM test_run_events WHERE test_run_id = ? ORDER BY timestamp")
    @RegisterRowMapper(TestRunEventRowMapper.class)
    List<TestRunEvent> getEventsById(String testRunId);
}
