package org.cempaka.cyclone.storage.repositories;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import javax.inject.Singleton;
import org.cempaka.cyclone.tests.ImmutableTestExecution;
import org.cempaka.cyclone.tests.TestExecution;

@Singleton
public class MemoryTestExecutionRepository implements TestExecutionRepository
{
    private final Map<TestExecutionKey, TestExecution> storage = Maps.newConcurrentMap();

    @Override
    public void setState(final UUID id, final String node, final String state)
    {
        setState(new TestExecutionKey(id, node), state);
    }

    private void setState(final TestExecutionKey testExecutionKey, final String state)
    {
        storage.computeIfPresent(testExecutionKey, (key, testExecution) -> ImmutableTestExecution.builder()
            .from(testExecution)
            .state(state)
            .build());
    }

    @Override
    public void setStates(final UUID id, final String state)
    {
        ImmutableSet.copyOf(storage.keySet()).stream()
            .filter(key -> key.getId().equals(id))
            .forEach(key -> setState(key, state));
    }

    @Override
    public void put(final TestExecution testExecution)
    {
        storage.put(new TestExecutionKey(testExecution.getId(), testExecution.getNode()), testExecution);
    }

    @Override
    public Set<TestExecution> getAll()
    {
        return ImmutableSet.copyOf(storage.values());
    }

    @Override
    public Set<TestExecution> get(final String node, final String state)
    {
        return get(key -> key.getNode().equals(node));
    }

    @Override
    public Set<TestExecution> get(final UUID id)
    {
        return get(key -> key.getId().equals(id));
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

    private class TestExecutionKey
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
