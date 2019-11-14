package org.cempaka.cyclone.storage.repositories;

import java.util.Set;
import java.util.UUID;
import org.cempaka.cyclone.tests.Test;

public interface TestRepository
{
    Set<Test> getAll();

    Set<Test> getByName(String name);

    void putAll(Set<Test> tests);

    void delete(UUID id);

    Set<UUID> keys();
}
