package org.cempaka.cyclone.storage.jdbi;

import java.sql.Timestamp;
import java.util.Set;
import org.cempaka.cyclone.beans.NodeState;
import org.cempaka.cyclone.storage.mappers.NodeStateRowMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface NodeStateDataAccess
{
    @SqlUpdate("INSERT INTO nodes (identifier, status, timestamp) VALUES (:identifier, :status, :timestamp) ON CONFLICT (identifier) DO UPDATE SET status = :status, timestamp = :timestamp")
    void upsert(@Bind("identifier") String identifier,
                @Bind("status") String status,
                @Bind("timestamp") Timestamp timestamp);

    @RegisterRowMapper(NodeStateRowMapper.class)
    @SqlQuery("SELECT identifier, status, timestamp FROM nodes")
    Set<NodeState> getNodes();
}
