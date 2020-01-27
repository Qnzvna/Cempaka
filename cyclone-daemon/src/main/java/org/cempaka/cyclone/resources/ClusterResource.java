package org.cempaka.cyclone.resources;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.cempaka.cyclone.beans.ImmutableNodeCapacity;
import org.cempaka.cyclone.services.NodeStatusService;
import org.cempaka.cyclone.workers.WorkerManager;

@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/cluster")
public class ClusterResource
{
    private final NodeStatusService nodeStatusService;
    private final WorkerManager workerManager;

    @Inject
    public ClusterResource(final NodeStatusService nodeStatusService,
                           final WorkerManager workerManager)
    {
        this.nodeStatusService = checkNotNull(nodeStatusService);
        this.workerManager = checkNotNull(workerManager);
    }

    @GET
    @Path("/status")
    public Response getClusterStatus()
    {
        return Response.ok(nodeStatusService.getNodesStatus()).build();
    }

    @GET
    @Path("/{node}/capacity")
    public Response getNodeCapacity(@NotNull @PathParam("node") final String node)
    {
        if (nodeStatusService.getNodesStatus().get(node) != null) {
            return Response.ok(ImmutableNodeCapacity.builder()
                .idleWorkers(workerManager.getIdleWorkers().size())
                .runningTests(workerManager.getRunningTestsId().size())
                .build())
                .build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
}
