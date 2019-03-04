package org.cempaka.cyclone.storage;


import java.util.List;
import java.util.Map;
import org.cempaka.cyclone.beans.TestRunMetadata;
import org.cempaka.cyclone.storage.mappers.TestRunMetadataRowMapper;
import org.jdbi.v3.json.Json;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface TestRunMetadataDataAcess
{
    @SqlUpdate("INSERT INTO test_run_metadata (test_run_id, test_running, parameters) VALUES (?, ?, ?)")
    void insertInitializationMetadata(String testRunId, String testRunning, @Json Map<String, String> parameters);

    @SqlQuery("SELECT test_run_events.test_run_id, timestamp, test_running, parameters FROM test_run_events JOIN test_run_metadata ON test_run_events.test_run_id = test_run_metadata.test_run_id WHERE event_type = 'INITIALIZED' ORDER BY timestamp DESC")
    @RegisterRowMapper(TestRunMetadataRowMapper.class)
    List<TestRunMetadata> getInitializationMetadata();
}
