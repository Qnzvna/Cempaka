package org.cempaka.cyclone.storage.repositories;

import java.util.Set;
import java.util.UUID;
import org.cempaka.cyclone.tests.TestExecution;

public interface TestExecutionRepository
{
    void setState(UUID id, String node, String state);

    void setStates(UUID id, String state);

    void put(TestExecution testExecution);

    Set<TestExecution> getAll();

    Set<TestExecution> get(String node, String state);

    Set<TestExecution> get(UUID id);

    void delete(UUID id);

    Set<UUID> keys();
}
