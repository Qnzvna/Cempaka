package org.cempaka.cyclone.resources;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
