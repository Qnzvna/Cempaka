package org.cempaka.cyclone.storage.jdbi;

import java.util.Set;
import org.cempaka.cyclone.beans.Parcel;
import org.cempaka.cyclone.storage.mappers.ParcelRowMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface ParcelDataAccess
{
    @SqlUpdate("INSERT INTO parcels (id, parcel) VALUES (:id, :parcel) ON CONFLICT (id) DO UPDATE SET parcel = :parcel")
    void upsert(@Bind("id") String id, @Bind("parcel") byte[] data);

    @RegisterRowMapper(ParcelRowMapper.class)
    @SqlQuery("SELECT id, parcel FROM parcels WHERE id = ?")
    Parcel get(String id);

    @SqlUpdate("DELETE FROM parcels WHERE id = ?")
    void delete(String id);

    @SqlQuery("SELECT id FROM parcels")
    Set<String> keys();
}
