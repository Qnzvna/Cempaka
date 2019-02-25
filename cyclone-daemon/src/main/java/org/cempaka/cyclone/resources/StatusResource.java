package org.cempaka.cyclone.resources;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.inject.Named;
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
    private final int workersNumber;
    private final WorkerManager workerManager;

    @Inject
    public StatusResource(@Named("worker.number") final int workersNumber, final WorkerManager workerManager)
    {
        this.workersNumber = workersNumber;
        this.workerManager = checkNotNull(workerManager);
    }

    @GET
    @Path("/node")
    public Response getNodeStatus()
    {
        final NodeStatus nodeStatus = new NodeStatus(workersNumber, workerManager.getRunningTestsId().size());
        return Response.ok(nodeStatus).build();
    }
}
