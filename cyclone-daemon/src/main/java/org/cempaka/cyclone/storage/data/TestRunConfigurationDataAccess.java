package org.cempaka.cyclone.storage.data;


import org.cempaka.cyclone.beans.TestRunConfiguration;
import org.jdbi.v3.json.Json;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface TestRunConfigurationDataAccess
{
    @SqlUpdate("INSERT INTO test_run_configuration (test_run_id, value) VALUES (?, ?)")
    void insertTestRunConfiguration(String testRunId, @Json TestRunConfiguration testRunConfiguration);

    @Json
    @SqlQuery("SELECT value FROM test_run_configuration WHERE test_run_id = ?")
    TestRunConfiguration getConfiguration(String testRunId);
}
