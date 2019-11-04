package org.cempaka.cyclone.storage.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.cempaka.cyclone.tests.ImmutableTestExecution;
import org.cempaka.cyclone.tests.TestExecution;
import org.cempaka.cyclone.tests.TestExecutionProperties;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.json.JsonConfig;

public class TestExecutionRowMapper implements RowMapper<TestExecution>
{
    @Override
    public TestExecution map(final ResultSet resultSet, final StatementContext context) throws SQLException
    {
        return ImmutableTestExecution.builder()
            .id(UUID.fromString(resultSet.getString("id")))
            .node(resultSet.getString("node"))
            .state(resultSet.getString("state"))
            .properties(getProperties(resultSet, context))
            .build();
    }

    private TestExecutionProperties getProperties(final ResultSet resultSet, final StatementContext context)
        throws SQLException
    {
        return (TestExecutionProperties) context.getConfig(JsonConfig.class)
            .getJsonMapper()
            .fromJson(TestExecutionProperties.class, resultSet.getString("properties"), context.getConfig());
    }
}
