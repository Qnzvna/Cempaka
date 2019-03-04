package org.cempaka.cyclone.storage.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.cempaka.cyclone.beans.TestRunMetadata;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.jackson2.Jackson2Config;

public class TestRunMetadataRowMapper implements RowMapper<TestRunMetadata>
{
    private static final TypeReference<Map<String, String>> TYPE_REFERENCE =
        new TypeReference<Map<String, String>>() {};

    @Override
    public TestRunMetadata map(final ResultSet resultSet, final StatementContext context)
        throws SQLException
    {
        final ObjectMapper mapper = context.getConfig(Jackson2Config.class).getMapper();
        try {
            return new TestRunMetadata(resultSet.getString("test_run_id"),
                resultSet.getTimestamp("timestamp").getTime() / 1000,
                resultSet.getString("test_running"),
                mapper.readValue(resultSet.getString("parameters"), TYPE_REFERENCE));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
