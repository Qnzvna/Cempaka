package org.cempaka.cyclone.storage.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.cempaka.cyclone.core.log.ImmutableLogMessage;
import org.cempaka.cyclone.core.log.LogMessage;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class LogMessageRowMapper implements RowMapper<LogMessage>
{
    @Override
    public LogMessage map(final ResultSet resultSet, final StatementContext ctx) throws SQLException
    {
        return ImmutableLogMessage.builder()
            .testExecutionId(UUID.fromString(resultSet.getString("test_id")))
            .logLine(resultSet.getString("log_line"))
            .build();
    }
}
