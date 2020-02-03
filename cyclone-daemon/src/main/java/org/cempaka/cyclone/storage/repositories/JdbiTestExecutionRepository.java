package org.cempaka.cyclone.storage.repositories;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.storage.jdbi.TestExecutionDataAccess;
import org.cempaka.cyclone.tests.TestExecution;

@Singleton
public class JdbiTestExecutionRepository implements TestExecutionRepository
{
    private final TestExecutionDataAccess testExecutionDataAccess;

    @Inject
    public JdbiTestExecutionRepository(final TestExecutionDataAccess testExecutionDataAccess)
    {
        this.testExecutionDataAccess = checkNotNull(testExecutionDataAccess);
    }

    @Override
    public void setState(final UUID id, final String node, final String state)
    {
        checkNotNull(node);
        checkNotNull(state);
        testExecutionDataAccess.setState(id.toString(), node, state);
    }

    @Override
    public void setStates(final UUID id, final String state)
    {
        checkNotNull(state);
        testExecutionDataAccess.setStates(id.toString(), state);
    }

    @Override
    public void updateTimestamp(final UUID id, final String node, final Instant timestamp)
    {
        checkNotNull(id);
        checkNotNull(node);
        checkNotNull(timestamp);
        testExecutionDataAccess.updateTimestamp(id.toString(), node, Timestamp.from(timestamp));
    }

    @Override
    public void put(final TestExecution testExecution)
    {
        testExecutionDataAccess.insert(testExecution.getId().toString(),
            testExecution.getNode(),
            testExecution.getState(),
            testExecution.getProperties());
    }

    @Override
    public List<TestExecution> getAll(final int limit, final int offset)
    {
        checkArgument(limit > 0);
        checkArgument(offset >= 0);
        return testExecutionDataAccess.getAll(limit, offset);
    }

    @Override
    public Set<TestExecution> get(final String node, final String state)
    {
        checkNotNull(node);
        checkNotNull(state);
        return testExecutionDataAccess.get(node, state);
    }

    @Override
    public Set<TestExecution> get(final UUID id)
    {
        return testExecutionDataAccess.get(id.toString());
    }

    @Override
    public Set<TestExecution> getUpdatedLaterThan(final Instant timestamp)
    {
        checkNotNull(timestamp);
        return testExecutionDataAccess.getUpdatedLaterThan(Timestamp.from(timestamp));

    }

    @Override
    public void delete(final UUID id)
    {
        testExecutionDataAccess.delete(id.toString());
    }

    @Override
    public Set<UUID> keys()
    {
        return testExecutionDataAccess.keys().stream().map(UUID::fromString).collect(toSet());
    }
}
