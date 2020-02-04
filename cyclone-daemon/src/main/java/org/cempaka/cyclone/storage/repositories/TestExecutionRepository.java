package org.cempaka.cyclone.storage.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.cempaka.cyclone.resources.TestExecutionsPage;
import org.cempaka.cyclone.tests.TestExecution;

public interface TestExecutionRepository
{
    void setState(UUID id, String node, String state);

    void setStates(UUID id, String state);

    void updateTimestamp(UUID id, String node, Instant timestamp);

    void put(TestExecution testExecution);

    TestExecutionsPage search(Set<String> states, Set<String> names, int limit, int offset);

    TestExecutionsPage getPage(int limit, int offset);

    Set<TestExecution> get(String node, String state);

    Set<TestExecution> get(UUID id);

    Set<TestExecution> getUpdatedLaterThan(Instant timestamp);

    void delete(UUID id);

    Set<UUID> keys();
}
