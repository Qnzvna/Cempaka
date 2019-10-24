package org.cempaka.cyclone.storage.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.cempaka.cyclone.beans.MetricDataPoint;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class MetricDataPointRowMapper implements RowMapper<MetricDataPoint>
{
    @Override
    public MetricDataPoint map(final ResultSet resultSet, final StatementContext context)
        throws SQLException
    {
        return new MetricDataPoint(TimestampConverter.getSeconds(resultSet.getTimestamp("timestamp")),
            resultSet.getString("name"),
            resultSet.getDouble("value"));
    }
}
