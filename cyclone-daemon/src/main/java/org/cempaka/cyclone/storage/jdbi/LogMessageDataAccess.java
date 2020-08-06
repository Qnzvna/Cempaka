package org.cempaka.cyclone.storage.jdbi;

import java.sql.Timestamp;
import java.util.List;
import org.cempaka.cyclone.log.LogMessage;
import org.cempaka.cyclone.storage.mappers.LogMessageRowMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface LogMessageDataAccess
{
    @SqlUpdate("INSERT INTO log_messages (test_id, update_timestamp, log_line) VALUES (?, ?, ?)")
    void insert(String testId, Timestamp timestamp, String logLine);

    @RegisterRowMapper(LogMessageRowMapper.class)
    @SqlQuery("SELECT test_id, log_line FROM log_messages WHERE test_id = ? update_timestamp > ?")
    List<LogMessage> getNewerThan(String testId, Timestamp timestamp);
}
