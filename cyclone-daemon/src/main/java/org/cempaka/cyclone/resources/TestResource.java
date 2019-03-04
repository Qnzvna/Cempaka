package org.cempaka.cyclone.resources;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.cempaka.cyclone.TestRunMetric;
import org.cempaka.cyclone.beans.ParcelMetadata;
import org.cempaka.cyclone.beans.TestMetadata;
import org.cempaka.cyclone.beans.TestRunConfiguration;
import org.cempaka.cyclone.beans.TestRunEvent;
import org.cempaka.cyclone.beans.TestRunMetadata;
import org.cempaka.cyclone.beans.exceptions.ParcelNotFoundException;
import org.cempaka.cyclone.beans.exceptions.ProcessFailureException;
import org.cempaka.cyclone.beans.exceptions.TestFailureException;
import org.cempaka.cyclone.beans.exceptions.WorkerNotAvailableException;
import org.cempaka.cyclone.services.TestRunnerService;
import org.cempaka.cyclone.storage.ParcelMetadataRepository;
import org.cempaka.cyclone.storage.TestRunEventDataAccess;
import org.cempaka.cyclone.storage.TestRunMetadataDataAcess;
import org.cempaka.cyclone.storage.TestRunMetricDataAcess;

@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/tests")
public class TestResource
{
    private final ParcelMetadataRepository parcelMetadataRepository;
    private final TestRunnerService testRunnerService;
    private final TestRunEventDataAccess testRunEventDataAccess;
    private final TestRunMetadataDataAcess testRunMetadataDataAcess;
    private final TestRunMetricDataAcess testRunMetricDataAcess;

    @Inject
    public TestResource(final ParcelMetadataRepository parcelMetadataRepository,
                        final TestRunnerService testRunnerService,
                        final TestRunEventDataAccess testRunEventDataAccess,
                        final TestRunMetadataDataAcess testRunMetadataDataAcess,
                        final TestRunMetricDataAcess testRunMetricDataAcess)
    {
        this.parcelMetadataRepository = checkNotNull(parcelMetadataRepository);
        this.testRunnerService = checkNotNull(testRunnerService);
        this.testRunEventDataAccess = checkNotNull(testRunEventDataAccess);
        this.testRunMetadataDataAcess = checkNotNull(testRunMetadataDataAcess);
        this.testRunMetricDataAcess = checkNotNull(testRunMetricDataAcess);
    }

    @GET
    public Response getTests()
    {
        final Set<ParcelMetadata> parcelMetadata = parcelMetadataRepository.list()
            .map(parcelMetadataRepository::get)
            .collect(Collectors.toSet());
        return Response.ok(parcelMetadata).build();
    }

    @GET
    @Path("/parcel/{parcelId}/name/{testName}")
    public Response getTest(final @PathParam("parcelId") String parcelId,
                            final @PathParam("testName") String testName)
    {
        final TestMetadata metadata = Optional.ofNullable(parcelMetadataRepository.get(parcelId))
            .flatMap(parcelMetadata -> parcelMetadata.getTestsMetadata().stream()
                .filter(testMetadata -> testMetadata.getTestName().equals(testName))
                .findFirst())
            .orElseThrow(NotFoundException::new);
        return Response.ok(metadata).build();
    }

    @POST
    @Path("/run")
    public Response runTest(final TestRunConfiguration testRunConfiguration)
    {
        try {
            final UUID uuid = testRunnerService.startTest(testRunConfiguration);
            return Response.accepted(uuid).build();
        } catch (ParcelNotFoundException e) {
            return Response.status(Status.NOT_FOUND).entity("Parcel for test not found.").build();
        } catch (WorkerNotAvailableException e) {
            return Response.status(Status.CONFLICT)
                .entity("Not enough workers to run the test.")
                .build();
        } catch (ProcessFailureException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (TestFailureException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("/{id}/abort")
    public Response abortTest(@PathParam("id") final String testId)
    {
        testRunnerService.abortTest(UUID.fromString(testId));
        return Response.ok().build();
    }

    @GET
    @Path("/metadata")
    public Response getMetadata()
    {
        final List<TestRunMetadata> metadata = testRunMetadataDataAcess
            .getInitializationMetadata();
        return Response.ok(metadata).build();
    }

    @GET
    @Path("/events/{id}")
    public Response getEvents(@PathParam("id") final String testId)
    {
        final List<TestRunEvent> events = testRunEventDataAccess.getEventsById(testId);
        return Response.ok().entity(events).build();
    }

    @GET
    @Path("/metrics/{id}")
    public Response getMetrics(@PathParam("id") final String testId)
    {
        final List<TestRunMetric> events = testRunMetricDataAcess.getMetricsById(testId);
        return Response.ok().entity(events).build();
    }

    @GET
    @Path("/running")
    public Response getRunningTests()
    {
        final Set<UUID> runningTests = testRunnerService.getRunningTestsId();
        return Response.ok().entity(runningTests).build();
    }
}
