package org.cempaka.cyclone.storage.repositories;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.inject.Singleton;
import org.cempaka.cyclone.tests.Test;

@Singleton
public class MemoryTestRepository implements TestRepository
{
    private final Map<UUID, Test> storage = Maps.newConcurrentMap();

    @Override
    public Set<Test> getAll()
    {
        return ImmutableSet.copyOf(storage.values());
    }

    @Override
    public Set<Test> getByName(final String name)
    {
        return getAll().stream().filter(test -> test.getName().equals(name))
            .collect(toImmutableSet());
    }

    @Override
    public synchronized void putAll(final Set<Test> tests)
    {
        tests.forEach(test -> storage.put(test.getParcelId(), test));
    }

    @Override
    public void delete(final UUID id)
    {
        storage.remove(id);
    }

    @Override
    public Set<UUID> keys()
    {
        return ImmutableSet.copyOf(storage.keySet());
    }
}
