package org.cempaka.cyclone.storage.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.cempaka.cyclone.beans.NodeState;
import org.cempaka.cyclone.beans.NodeStatus;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class NodeStateRowMapper implements RowMapper<NodeState>
{
    @Override
    public NodeState map(final ResultSet resultSet, final StatementContext context) throws SQLException
    {
        return new NodeState(resultSet.getString("identifier"),
            NodeStatus.valueOf(resultSet.getString("status")),
            TimestampConverter.getSeconds(resultSet.getTimestamp("timestamp")));
    }
}
