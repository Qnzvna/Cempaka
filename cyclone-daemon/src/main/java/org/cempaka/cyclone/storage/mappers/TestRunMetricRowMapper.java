package org.cempaka.cyclone.storage.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.cempaka.cyclone.TestRunMetric;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class TestRunMetricRowMapper implements RowMapper<TestRunMetric>
{
    @Override
    public TestRunMetric map(final ResultSet resultSet, final StatementContext context)
        throws SQLException
    {
        return new TestRunMetric(TimestampConverter.getSeconds(resultSet.getTimestamp("timestamp")),
            resultSet.getString("name"),
            resultSet.getDouble("value"));
    }
}
