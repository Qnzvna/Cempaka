package org.cempaka.cyclone.storage.jdbi;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import org.cempaka.cyclone.storage.mappers.TestExecutionRowMapper;
import org.cempaka.cyclone.tests.TestExecution;
import org.cempaka.cyclone.tests.TestExecutionProperties;
import org.jdbi.v3.json.Json;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindList;
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

    @SqlUpdate("UPDATE test_executions SET update_timestamp = :update_timestamp WHERE id = :id AND node = :node")
    void updateTimestamp(@Bind("id") String id,
                         @Bind("node") String node,
                         @Bind("update_timestamp") Timestamp timestamp);

    @RegisterRowMapper(TestExecutionRowMapper.class)
    @SqlQuery("SELECT id, node, state, properties FROM test_executions WHERE state IN (<states>) AND properties->>'testName' ~* :name ORDER BY id LIMIT :limit OFFSET :offset")
    List<TestExecution> search(@BindList("states") Set<String> states,
                               @Bind("name") String nameLike,
                               @Bind("limit") int limit,
                               @Bind("offset") int offset);

    @RegisterRowMapper(TestExecutionRowMapper.class)
    @SqlQuery("SELECT id, node, state, properties FROM test_executions ORDER BY id LIMIT :limit OFFSET :offset")
    List<TestExecution> getAll(@Bind("limit") int limit, @Bind("offset") int offset);

    @RegisterRowMapper(TestExecutionRowMapper.class)
    @SqlQuery("SELECT id, node, state, properties FROM test_executions WHERE node = ? AND state = ?")
    Set<TestExecution> get(String node, String state);

    @RegisterRowMapper(TestExecutionRowMapper.class)
    @SqlQuery("SELECT id, node, state, properties FROM test_executions WHERE id = ?")
    Set<TestExecution> get(String id);

    @RegisterRowMapper(TestExecutionRowMapper.class)
    @SqlQuery("SELECT id, node, state, properties FROM test_executions WHERE update_timestamp < :update_timestamp")
    Set<TestExecution> getUpdatedLaterThan(@Bind("update_timestamp") Timestamp updatedTimestamp);

    @SqlUpdate("DELETE FROM test_executions WHERE id = ?")
    void delete(String id);

    @SqlQuery("SELECT id FROM test_executions")
    Set<String> keys();
}
