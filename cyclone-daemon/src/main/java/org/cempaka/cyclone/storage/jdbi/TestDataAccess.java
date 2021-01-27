package org.cempaka.cyclone.storage.jdbi;

import java.util.Optional;
import java.util.Set;
import org.cempaka.cyclone.tests.Test;
import org.jdbi.v3.json.Json;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

public interface TestDataAccess
{
    @Transaction
    default void putAll(Set<Test> tests)
    {
        tests.forEach(test -> upsert(test.getParcelId().toString(), test.getName(), test));
    }

    @SqlUpdate("INSERT INTO tests (id, name, value) VALUES (:id, :name, :value) ON CONFLICT (id, name) DO UPDATE SET  value = :value")
    void upsert(@Bind("id") String id, @Bind("name") String name, @Bind("value") @Json Test test);

    @Json
    @SqlQuery("SELECT value FROM tests")
    Set<Test> getAll();

    @Json
    @SqlQuery("SELECT value FROM tests WHERE name = ?")
    Set<Test> get(String name);

    @Json
    @SqlQuery("SELECT value FROM tests WHERE id = ? AND name = ?")
    Optional<Test> getByIdAndName(String id, String name);

    @SqlUpdate("DELETE FROM tests WHERE id = ?")
    void delete(String id);

    @SqlQuery("SELECT id FROM tests")
    Set<String> keys();
}