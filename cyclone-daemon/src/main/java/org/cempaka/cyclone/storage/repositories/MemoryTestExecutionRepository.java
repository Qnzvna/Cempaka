package org.cempaka.cyclone.storage.repositories;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.of;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import java.time.Clock;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.resources.ImmutableTestExecutionsPage;
import org.cempaka.cyclone.resources.TestExecutionsPage;
import org.cempaka.cyclone.tests.ImmutableTestExecution;
import org.cempaka.cyclone.tests.TestExecution;

@Singleton
public class MemoryTestExecutionRepository implements TestExecutionRepository
{
    private final Map<TestExecutionKey, TestExecution> storage = Maps.newConcurrentMap();
    private final Map<TestExecutionKey, Instant> updateTimestampStorage = Maps.newConcurrentMap();
    private final Clock clock;

    @Inject
    public MemoryTestExecutionRepository(final Clock clock)
    {
        this.clock = checkNotNull(clock);
    }

    @Override
    public void setState(final UUID id, final String node, final String state)
    {
        setState(new TestExecutionKey(id, node), state);
    }

    private synchronized void setState(final TestExecutionKey testExecutionKey, final String state)
    {
        storage.computeIfPresent(testExecutionKey, (key, testExecution) -> ImmutableTestExecution.builder()
            .from(testExecution)
            .state(state)
            .build());
        updateTimestampStorage.put(testExecutionKey, Instant.now(clock));
    }

    @Override
    public void setStates(final UUID id, final String state)
    {
        ImmutableSet.copyOf(storage.keySet()).stream()
            .filter(key -> key.getId().equals(id))
            .forEach(key -> setState(key, state));
    }

    @Override
    public void updateTimestamp(final UUID id, final String node, final Instant timestamp)
    {
        checkNotNull(id);
        checkNotNull(node);
        checkNotNull(timestamp);
        updateTimestampStorage.put(new TestExecutionKey(id, node), timestamp);
    }

    @Override
    public void put(final TestExecution testExecution)
    {
        storage.put(new TestExecutionKey(testExecution.getId(), testExecution.getNode()), testExecution);
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
        final List<TestExecution> executions = storage.values().stream()
            .filter(testExecution -> states.contains(testExecution.getState()))
            .filter(testExecution -> like(names, testExecution))
            .skip(offset)
            .collect(Collectors.toList());
        return getTestExecutionsPage(limit, executions);
    }

    private boolean like(final Set<String> names, final TestExecution testExecution)
    {
        return names.stream().anyMatch(name -> testExecution.getProperties().getTestName().contains(name));
    }

    @Override
    public TestExecutionsPage getPage(final int limit, final int offset)
    {
        checkArgument(limit > 0);
        checkArgument(offset >= 0);
        final List<TestExecution> executions = storage.values().stream()
            .sorted(Comparator.comparing(testExecution -> testExecution.getId().toString()))
            .skip(offset)
            .collect(toImmutableList());
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
        return get(key -> key.getNode().equals(node)).stream()
            .filter(testExecution -> testExecution.getState().equals(state))
            .collect(toImmutableSet());
    }

    @Override
    public Set<TestExecution> get(final UUID id)
    {
        return get(key -> key.getId().equals(id));
    }

    @Override
    public synchronized Set<TestExecution> getUpdatedLaterThan(final Instant timestamp)
    {
        return updateTimestampStorage.entrySet().stream()
            .filter(entry -> entry.getValue().isBefore(timestamp))
            .map(Map.Entry::getKey)
            .map(storage::get)
            .collect(toImmutableSet());
    }

    @Override
    public void delete(final UUID id)
    {
        get(id).stream()
            .map(testExecution -> new TestExecutionKey(testExecution.getId(), testExecution.getNode()))
            .forEach(storage::remove);
    }

    private Set<TestExecution> get(final Predicate<TestExecutionKey> predicate)
    {
        return ImmutableSet.copyOf(storage.keySet()).stream()
            .filter(predicate)
            .map(storage::get)
            .filter(Objects::nonNull)
            .collect(toImmutableSet());
    }

    @Override
    public Set<UUID> keys()
    {
        return ImmutableSet.copyOf(storage.keySet()).stream().map(TestExecutionKey::getId).collect(toImmutableSet());
    }

    private static class TestExecutionKey
    {
        private final UUID id;
        private final String node;

        private TestExecutionKey(final UUID id, final String node)
        {
            this.id = checkNotNull(id);
            this.node = checkNotNull(node);
        }

        public UUID getId()
        {
            return id;
        }

        public String getNode()
        {
            return node;
        }

        @Override
        public boolean equals(final Object o)
        {
            if (this == o) { return true; }
            if (o == null || getClass() != o.getClass()) { return false; }
            final TestExecutionKey that = (TestExecutionKey) o;
            return Objects.equals(id, that.id) &&
                Objects.equals(node, that.node);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(id, node);
        }
    }
}
