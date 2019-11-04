package org.cempaka.cyclone.storage.repositories;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;
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
    public void delete(final String id)
    {
        checkNotNull(id);
        testDataAccess.delete(id);
    }

    @Override
    public Set<String> keys()
    {
        return testDataAccess.keys();
    }
}
