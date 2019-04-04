package org.cempaka.cyclone.storage.data;

import org.cempaka.cyclone.beans.ParcelMetadata;
import org.jdbi.v3.json.Json;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Set;
import java.util.stream.Stream;

public interface ParcelMetadataDataAccess
{
    @SqlUpdate("INSERT INTO parcels_metadata (id, value) VALUES (:id, :value) ON CONFLICT (id) DO UPDATE SET  value = :value")
    void upsert(@Bind("id") String id, @Bind("value") @Json ParcelMetadata parcelMetadata);

    @SqlUpdate("DELETE FROM parcels_metadata WHERE id = ?")
    void delete(String id);

    @SqlQuery("SELECT id FROM parcels_metadata")
    Stream<String> getKeys();

    @Json
    @SqlQuery("SELECT value FROM parcels_metadata WHERE id = ?")
    ParcelMetadata findById(String id);

    @Json
    @SqlQuery("SELECT value FROM parcels_metadata")
    Set<ParcelMetadata> getAll();
}