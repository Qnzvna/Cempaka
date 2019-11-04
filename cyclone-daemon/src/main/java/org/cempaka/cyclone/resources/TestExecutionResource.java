package org.cempaka.cyclone.resources;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public Response getAll()
    {
        return Response.ok().entity(testExecutionRepository.getAll()).build();
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
