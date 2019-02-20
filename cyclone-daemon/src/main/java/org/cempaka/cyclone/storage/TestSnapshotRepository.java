package org.cempaka.cyclone.storage;

import com.google.common.collect.ImmutableList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.TestSnapshot;

@Singleton
public class TestSnapshotRepository
{
    private final ConcurrentMap<String, PriorityQueue<TestSnapshot>> snapshots;

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
        snapshots.compute(testUuid, (key, value) -> {
            final PriorityQueue<TestSnapshot> queue;
            if (value == null) {
                queue = createQueueStore();
            } else {
                queue = value;
            }
            queue.add(testSnapshot);
            return queue;
        });
    }

    private PriorityQueue<TestSnapshot> createQueueStore()
    {
        return new PriorityQueue<>(Comparator.comparingLong(TestSnapshot::getTimestamp));
    }

    public List<TestSnapshot> get(final String testUuid)
    {
        final PriorityQueue<TestSnapshot> queue = snapshots.get(testUuid);
        if (queue == null) {
            return ImmutableList.of();
        } else {
            return ImmutableList.copyOf(queue);
        }
    }
}

