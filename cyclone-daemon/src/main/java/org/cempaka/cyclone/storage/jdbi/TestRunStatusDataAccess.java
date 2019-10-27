package org.cempaka.cyclone.storage.jdbi;

import java.util.Set;
import org.cempaka.cyclone.beans.TestRunConfiguration;
import org.jdbi.v3.json.Json;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface TestRunStatusDataAccess
{
    @SqlUpdate("INSERT INTO test_run_status (test_run_id, node_identifier, state, configuration) VALUES (?, ?, ?, ?)")
    void insert(String testRunId, String nodeIdentifier, String state, @Json TestRunConfiguration testRunConfiguration);

    @SqlUpdate("UPDATE test_run_status SET state = ? WHERE test_run_id = ? AND node_identifier = ?")
    void updateState(String state, String testRunId, String nodeIdentifier);

    @SqlUpdate("UPDATE test_run_status SET state = ? WHERE test_run_id = ?")
    void updateAllStates(String state, String testRunId);

    @Json
    @SqlQuery("SELECT configuration FROM test_run_status WHERE test_run_id = ?")
    TestRunConfiguration getConfiguration(String testRunId);

    @SqlQuery("SELECT test_run_id FROM test_run_status WHERE node_identifier = ? AND state = ?")
    Set<String> getTests(String nodeIdentifier, String state);

    @SqlQuery("SELECT state FROM test_run_status WHERE test_run_id = ? AND node_identifier = ?")
    String getState(String testRunId, String nodeIdentifier);

    @SqlQuery("SELECT test_run_id FROM test_run_status WHERE state = ?")
    Set<String> getTestsWithState(String state);
}