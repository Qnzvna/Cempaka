package org.cempaka.cyclone.storage.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.cempaka.cyclone.beans.Parcel;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class ParcelRowMapper implements RowMapper<Parcel>
{
    @Override
    public Parcel map(final ResultSet rs, final StatementContext ctx) throws SQLException
    {
        return Parcel.of(UUID.fromString(rs.getString("id")), rs.getBytes("parcel"));
    }
}
