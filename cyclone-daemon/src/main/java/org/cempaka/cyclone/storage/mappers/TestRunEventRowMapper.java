package org.cempaka.cyclone.storage.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.cempaka.cyclone.beans.EventType;
import org.cempaka.cyclone.beans.TestRunEvent;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class TestRunEventRowMapper implements RowMapper<TestRunEvent>
{
    @Override
    public TestRunEvent map(final ResultSet resultSet, final StatementContext context)
        throws SQLException
    {
        return new TestRunEvent(resultSet.getString("test_run_id"),
            resultSet.getTimestamp("timestamp").getTime() / 1000,
            EventType.valueOf(resultSet.getString("event_type")));
    }
}
