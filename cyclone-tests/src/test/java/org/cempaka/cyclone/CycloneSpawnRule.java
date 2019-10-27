package org.cempaka.cyclone;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.core.DockerClientBuilder;
import com.google.common.util.concurrent.Uninterruptibles;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CycloneSpawnRule extends ExternalResource
{
    private static final Logger LOG = LoggerFactory.getLogger(CycloneSpawnRule.class);

    private static final String CYCLONE_IMAGE_NAME = "cempaka/cyclone:0.2-SNAPSHOT";
    private static final String POSTGRES_IMAGE_NAME = "postgres:9.5.16-alpine";
    private static final String CYCLONE_CONTAINER_NAME = "cyclone-it-";
    private static final String POSTGRES_CONTAINER_NAME = "postgres";
    private static final String NETWORK_NAME = "cyclone-it";

    private final DockerClient docker;
    private final AtomicInteger cycloneContainerNameId;
    private final List<String> containers;
    private String networkId;

    public CycloneSpawnRule()
    {
        docker = DockerClientBuilder.getInstance().build();
        cycloneContainerNameId = new AtomicInteger();
        containers = new LinkedList<>();
    }

    @Override
    protected void before()
    {
        containers.add(createPostgres());
        containers.add(createCyclone());
        createNetworkAndStartContainers();
    }

    public void spawnTo(final int to)
    {
        while (cycloneContainerNameId.get() < to) {
            final String containerId = createCyclone();
            containers.add(containerId);
            connectAndStartContainer(containerId);
        }
    }

    private String createPostgres()
    {
        return docker.createContainerCmd(POSTGRES_IMAGE_NAME)
            .withName(POSTGRES_CONTAINER_NAME)
            .withEnv("POSTGRES_USER=cyclone", "POSTGRES_PASSWORD=cyclone")
            .exec()
            .getId();
    }

    private String createCyclone()
    {
        final int id = cycloneContainerNameId.getAndIncrement();
        return docker.createContainerCmd(CYCLONE_IMAGE_NAME)
            .withName(CYCLONE_CONTAINER_NAME + id)
            .withPortBindings(new PortBinding(Binding.bindPort(50_000 + id), ExposedPort.tcp(8000)))
            .exec()
            .getId();
    }

    private void createNetworkAndStartContainers()
    {
        final CreateNetworkResponse response = docker.createNetworkCmd()
            .withName(NETWORK_NAME)
            .exec();
        networkId = response.getId();
        containers.forEach(this::connectAndStartContainer);
    }

    private void connectAndStartContainer(final String containerId)
    {
        docker.connectToNetworkCmd()
            .withNetworkId(networkId)
            .withContainerId(containerId)
            .exec();
        docker.startContainerCmd(containerId).exec();
        Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
    }

    @Override
    protected void after()
    {
        stopContainersAndRemoveNetwork();
    }

    private void stopContainersAndRemoveNetwork()
    {
        Collections.reverse(containers);
        containers.forEach(containerId -> {
            try {
                docker.stopContainerCmd(containerId).exec();
                docker.removeContainerCmd(containerId).exec();
            } catch (Exception e) {
                LOG.error("Failed to clean up container {}.", containerId, e);
            }
        });
        if (networkId != null) {
            try {
                docker.removeNetworkCmd(networkId).exec();
            } catch (Exception e) {
                LOG.error("Failed to remove network {}.", networkId, e);
            }
        }
    }
}
