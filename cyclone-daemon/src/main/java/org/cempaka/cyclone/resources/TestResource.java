package org.cempaka.cyclone.resources;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.cempaka.cyclone.beans.exceptions.ParcelNotFoundException;
import org.cempaka.cyclone.services.TestRunnerService;
import org.cempaka.cyclone.storage.repositories.TestRepository;
import org.cempaka.cyclone.tests.TestExecutionProperties;

@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/tests")
public class TestResource
{
    private final TestRepository testRepository;
    private final TestRunnerService testRunnerService;

    @Inject
    public TestResource(final TestRepository testRepository, final TestRunnerService testRunnerService)
    {
        this.testRepository = checkNotNull(testRepository);
        this.testRunnerService = checkNotNull(testRunnerService);
    }

    @GET
    public Response getTests()
    {
        return Response.ok(testRepository.getAll()).build();
    }

    @POST
    @Path("/start")
    public Response startTest(final TestExecutionProperties testExecutionProperties)
    {
        try {
            final UUID uuid = testRunnerService.startTest(testExecutionProperties);
            return Response.accepted(uuid).build();
        } catch (ParcelNotFoundException e) {
            return Response.status(Status.NOT_FOUND).entity("Parcel for test not found.").build();
        }
    }

    @POST
    @Path("/{id}/stop")
    public Response stopTest(@PathParam("id") final String testId)
    {
        testRunnerService.stopTest(UUID.fromString(testId));
        return Response.noContent().build();
    }
}
