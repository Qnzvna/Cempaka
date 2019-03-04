package org.cempaka.cyclone.resources;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.cempaka.cyclone.beans.NodeStatus;
import org.cempaka.cyclone.worker.WorkerManager;

@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/status")
public class StatusResource
{
    private final WorkerManager workerManager;

    @Inject
    public StatusResource(final WorkerManager workerManager)
    {
        this.workerManager = checkNotNull(workerManager);
    }

    @GET
    @Path("/node")
    public Response getNodeStatus()
    {
        final int idleWorkers = workerManager.getIdleWorkers().size();
        final int runningTests = workerManager.getRunningTestsId().size();
        final NodeStatus nodeStatus = new NodeStatus(idleWorkers,
            runningTests);
        return Response.ok(nodeStatus).build();
    }
}
