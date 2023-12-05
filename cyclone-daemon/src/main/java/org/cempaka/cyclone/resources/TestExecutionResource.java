package org.cempaka.cyclone.resources;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;
import java.util.UUID;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.cempaka.cyclone.beans.TestState;
import org.cempaka.cyclone.storage.repositories.TestExecutionRepository;

@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/tests/executions")
public class TestExecutionResource
{
    private final TestExecutionRepository testExecutionRepository;

    @Inject
    public TestExecutionResource(final TestExecutionRepository testExecutionRepository)
    {
        this.testExecutionRepository = checkNotNull(testExecutionRepository);
    }

    @GET
    public Response getAll(@DefaultValue("50") @QueryParam("limit") final int limit,
                           @DefaultValue("0") @QueryParam("offset") final int offset)
    {
        return Response.ok().entity(testExecutionRepository.getPage(limit, offset)).build();
    }

    @GET
    @Path("/search")
    public Response search(@QueryParam("state") final Set<String> states,
                           @QueryParam("name") final Set<String> names,
                           @DefaultValue("50") @QueryParam("limit") final int limit,
                           @DefaultValue("0") @QueryParam("offset") final int offset)
    {
        return Response.ok().entity(testExecutionRepository.search(states.isEmpty() ? TestState.ALL : states,
            names,
            limit,
            offset)).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") final String id)
    {
        return Response.ok().entity(testExecutionRepository.get(UUID.fromString(id))).build();
    }

    @GET
    @Path("/keys")
    public Response getKeys()
    {
        return Response.ok().entity(testExecutionRepository.keys()).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") final String id)
    {
        testExecutionRepository.delete(UUID.fromString(id));
        return Response.noContent().build();
    }
}
