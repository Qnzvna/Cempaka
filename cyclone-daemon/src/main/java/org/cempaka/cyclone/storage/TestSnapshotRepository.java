package org.cempaka.cyclone.storage;

import static org.cempaka.cyclone.utils.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.TestSnapshot;

@Singleton
public class TestSnapshotRepository
{
    private final ConcurrentMap<String, List<TestSnapshot>> snapshots;

    @Inject
    public TestSnapshotRepository()
    {
        this.snapshots = new ConcurrentHashMap<>();
    }

    public Set<String> getSnapshotsKeys()
    {
        return snapshots.keySet();
    }

    public void put(final String testUuid, final TestSnapshot testSnapshot)
    {
        checkNotNull(testSnapshot);
        snapshots.compute(testUuid, (key, value) -> {
            final List<TestSnapshot> list;
            if (value == null) {
                list = createQueueStore();
            } else {
                list = value;
            }
            list.add(testSnapshot);
            return list;
        });
    }

    private List<TestSnapshot> createQueueStore()
    {
        return new ArrayList<>();
    }

    public List<TestSnapshot> get(final String testUuid)
    {
        final List<TestSnapshot> queue = snapshots.get(testUuid);
        if (queue == null) {
            return ImmutableList.of();
        } else {
            final List<TestSnapshot> sorted = new ArrayList<>(queue);
            sorted.sort(Comparator.comparingLong(TestSnapshot::getTimestamp).reversed());
            return ImmutableList.copyOf(sorted);
        }
    }
}

