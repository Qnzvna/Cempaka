package org.cempaka.cyclone.worker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import org.cempaka.cyclone.beans.Parcel;
import org.cempaka.cyclone.beans.exceptions.ParcelNotFoundException;
import org.cempaka.cyclone.beans.exceptions.WorkerNotAvailableException;
import org.cempaka.cyclone.storage.repository.ParcelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.google.common.base.Preconditions.checkNotNull;

@Singleton
public class WorkerManager
{
    private static final Logger LOG = LoggerFactory.getLogger(WorkerManager.class);

    private final Queue<Worker> workerPool = Queues.newConcurrentLinkedQueue();
    private final Map<UUID, Worker> runningTests = Maps.newConcurrentMap();

    private final ParcelRepository parcelRepository;
    private final int udpServerPort;
    private final String logsDirectory;

    @Inject
    public WorkerManager(final ParcelRepository parcelRepository,
                         @Named("udp.server.port") final int udpServerPort,
                         @Named("worker.number") final int workerNumber,
                         @Named("worker.logs.directory") final String logsDirectory)
    {
        this.parcelRepository = checkNotNull(parcelRepository);
        this.udpServerPort = udpServerPort;
        this.logsDirectory = checkNotNull(logsDirectory);
        initializeWorkers(workerNumber);
    }

    private void initializeWorkers(final int workerNumber)
    {
        LOG.debug("Initializing workers...");
        for (int i = 0; i < workerNumber; i++) {
            final Worker worker = new Worker();
            workerPool.add(worker);
        }
        LOG.info("Workers initialized.");
    }

    public CompletableFuture<UUID> startTest(final UUID testId,
                                             final UUID parcelId,
                                             final String testName,
                                             final int loopCount,
                                             final int threadsNumber,
                                             final Map<String, String> parameters)
    {
        LOG.debug("About to start test for parcel {} ...", parcelId);
        final Parcel parcel = parcelRepository.get(parcelId);
        if (parcel != null) {
            final Worker worker = getIdleWorker();
            try {
                worker.start(parcel,
                    testId.toString(),
                    ImmutableList.of(testName),
                    loopCount,
                    threadsNumber,
                    udpServerPort,
                    logsDirectory,
                    parameters);
                LOG.debug("Test started successfully.");
                final CompletableFuture<?> testFuture = worker.onDone();
                testFuture.whenComplete((ignore, throwable) -> cleanResources(worker, testId));
                runningTests.put(testId, worker);
                return testFuture.thenApply(ignore -> testId);
            } catch (Exception e) {
                cleanResources(worker, testId);
                throw new RuntimeException(e);
            }
        } else {
            LOG.warn("Parcel {} not found in a repository.", parcelId);
            throw new ParcelNotFoundException();
        }
    }

    private Worker getIdleWorker()
    {
        final Worker worker = workerPool.poll();
        if (worker == null) {
            throw new WorkerNotAvailableException();
        }
        return worker;
    }

    private void cleanResources(final Worker worker, final UUID testId)
    {
        workerPool.add(worker);
        runningTests.remove(testId);
        LOG.debug("Resources cleaned.");
    }

    void abortTest(final UUID testId)
    {
        runningTests.computeIfPresent(testId, (handler, worker) -> {
            worker.abort();
            cleanResources(worker, testId);
            return null;
        });
    }

    public Set<UUID> getRunningTestsId()
    {
        return ImmutableMap.copyOf(runningTests).keySet();
    }

    public List<Worker> getIdleWorkers()
    {
        return ImmutableList.copyOf(workerPool);
    }
}
