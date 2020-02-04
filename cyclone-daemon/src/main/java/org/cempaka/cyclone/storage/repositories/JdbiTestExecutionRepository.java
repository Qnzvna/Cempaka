package org.cempaka.cyclone.storage.repositories;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.resources.ImmutableTestExecutionsPage;
import org.cempaka.cyclone.resources.TestExecutionsPage;
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
    public TestExecutionsPage search(final Set<String> states,
                                     final Set<String> names,
                                     final int limit,
                                     final int offset)
    {
        checkNotNull(states);
        checkNotNull(names);
        checkArgument(limit > 0);
        checkArgument(offset >= 0);
        final List<TestExecution> executions = testExecutionDataAccess
            .search(states, String.join("|", names), limit, offset);
        return getTestExecutionsPage(limit, executions);
    }

    @Override
    public TestExecutionsPage getPage(final int limit, final int offset)
    {
        checkArgument(limit > 0);
        checkArgument(offset >= 0);
        final List<TestExecution> executions = testExecutionDataAccess.getAll(limit + 1, offset);
        return getTestExecutionsPage(limit, executions);
    }

    private TestExecutionsPage getTestExecutionsPage(final int limit, final List<TestExecution> executions)
    {
        return ImmutableTestExecutionsPage.builder()
            .hasNext(executions.size() > limit)
            .addAllTestExecutions(executions.stream().limit(limit).collect(Collectors.toList()))
            .build();
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
