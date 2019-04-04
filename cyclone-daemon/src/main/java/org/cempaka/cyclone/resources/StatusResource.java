package org.cempaka.cyclone.resources;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;
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
@Path("/status")
public class StatusResource
{
    private final NodeStatusService nodeStatusService;

    @Inject
    public StatusResource(final NodeStatusService nodeStatusService)
    {
        this.nodeStatusService = checkNotNull(nodeStatusService);
    }

    @GET
    public Response getClusterStatus()
    {
        final Set<String> liveNodes = nodeStatusService.getLiveNodes();
        return Response.ok(liveNodes).build();
    }
}
