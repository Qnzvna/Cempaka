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
import org.cempaka.cyclone.services.NodeStatusService;

@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/cluster")
public class ClusterResource
{
    private final NodeStatusService nodeStatusService;

    @Inject
    public ClusterResource(final NodeStatusService nodeStatusService)
    {
        this.nodeStatusService = checkNotNull(nodeStatusService);
    }

    @GET
    @Path("/status")
    public Response getClusterStatus()
    {
        return Response.ok(nodeStatusService.getNodesStatus()).build();
    }
}
