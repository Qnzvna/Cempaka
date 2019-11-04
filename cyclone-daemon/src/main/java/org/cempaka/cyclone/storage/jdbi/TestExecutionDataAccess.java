package org.cempaka.cyclone.storage.jdbi;

import java.util.Set;
import org.cempaka.cyclone.storage.mappers.TestExecutionRowMapper;
import org.cempaka.cyclone.tests.TestExecution;
import org.cempaka.cyclone.tests.TestExecutionProperties;
import org.jdbi.v3.json.Json;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface TestExecutionDataAccess
{
    @SqlUpdate("INSERT INTO test_executions (id, node, state, properties) VALUES (?, ?, ?, ?)")
    void insert(String id, String node, String state, @Json TestExecutionProperties testExecutionProperties);

    @SqlUpdate("UPDATE test_executions SET state = :state WHERE id = :id AND node = :node")
    void setState(@Bind("id") String id, @Bind("node") String node, @Bind("state") String state);

    @SqlUpdate("UPDATE test_executions SET state = :state WHERE id = :id")
    void setStates(@Bind("id") String id, @Bind("state") String state);

    @RegisterRowMapper(TestExecutionRowMapper.class)
    @SqlQuery("SELECT id, node, state, properties FROM test_executions")
    Set<TestExecution> getAll();

    @RegisterRowMapper(TestExecutionRowMapper.class)
    @SqlQuery("SELECT id, node, state, properties FROM test_executions WHERE node = ? AND state = ?")
    Set<TestExecution> get(String node, String state);

    @RegisterRowMapper(TestExecutionRowMapper.class)
    @SqlQuery("SELECT id, node, state, properties FROM test_executions WHERE id = ?")
    Set<TestExecution> get(String id);

    @SqlUpdate("DELETE FROM test_executions WHERE id = ?")
    void delete(String id);

    @SqlQuery("SELECT id FROM test_executions")
    Set<String> keys();
}
