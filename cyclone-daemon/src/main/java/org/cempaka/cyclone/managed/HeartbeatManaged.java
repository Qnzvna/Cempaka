package org.cempaka.cyclone.managed;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.dropwizard.lifecycle.Managed;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.cempaka.cyclone.beans.NodeStatus;
import org.cempaka.cyclone.configurations.ClusterConfiguration;
import org.cempaka.cyclone.services.NodeStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class HeartbeatManaged implements Managed
{
    private final static Logger LOG = LoggerFactory.getLogger(HeartbeatManaged.class);

    private final ScheduledExecutorService executorService;
    private final NodeStatusService nodeStatusService;
    private final ClusterConfiguration clusterConfiguration;

    @Inject
    public HeartbeatManaged(final NodeStatusService nodeStatusService,
                            final ClusterConfiguration clusterConfiguration)
    {
        this.nodeStatusService = checkNotNull(nodeStatusService);
        this.clusterConfiguration = checkNotNull(clusterConfiguration);
        this.executorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder()
            .setNameFormat("HeartbeatManaged-%d")
            .build());
    }

    @Override
    public void start()
    {
        executorService.scheduleWithFixedDelay(() -> {
            try {
                nodeStatusService.updateStatus(NodeStatus.UP);
            } catch (Exception e) {
                LOG.error("Failed on sending heartbeat.", e);
            }
        }, 0, clusterConfiguration.getHeartbeatInterval(), TimeUnit.SECONDS);
    }

    @Override
    public void stop() throws Exception
    {
        executorService.shutdown();
        executorService.awaitTermination(clusterConfiguration.getHeartbeatManagedAwaitInterval(),
            TimeUnit.SECONDS);
        nodeStatusService.updateStatus(NodeStatus.DOWN);
    }
}
