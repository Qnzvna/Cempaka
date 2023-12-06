package org.cempaka.cyclone.resources;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.cempaka.cyclone.beans.exceptions.ParcelNotFoundException;
import org.cempaka.cyclone.beans.exceptions.TestNotFoundException;
import org.cempaka.cyclone.services.NodeNotAliveException;
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
        } catch (TestNotFoundException e) {
            return Response.status(Status.NOT_FOUND).entity("Test not found.").build();
        } catch (NodeNotAliveException e) {
            return Response.status(Status.SERVICE_UNAVAILABLE).entity(e.getMessage()).build();
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
