package org.cempaka.cyclone.storage.repositories;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.storage.jdbi.TestDataAccess;
import org.cempaka.cyclone.tests.Test;

@Singleton
public class JdbiTestRepository implements TestRepository
{
    private final TestDataAccess testDataAccess;

    @Inject
    public JdbiTestRepository(final TestDataAccess testDataAccess)
    {
        this.testDataAccess = checkNotNull(testDataAccess);
    }

    @Override
    public Set<Test> getAll()
    {
        return testDataAccess.getAll();
    }

    @Override
    public Set<Test> getByName(final String name)
    {
        checkNotNull(name);
        return testDataAccess.get(name);
    }

    @Override
    public void putAll(final Set<Test> tests)
    {
        testDataAccess.putAll(tests);
    }

    @Override
    public void delete(final UUID id)
    {
        checkNotNull(id);
        testDataAccess.delete(id.toString());
    }

    @Override
    public Set<UUID> keys()
    {
        return testDataAccess.keys().stream().map(UUID::fromString).collect(toImmutableSet());
    }
}
