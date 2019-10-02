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
import org.cempaka.cyclone.beans.exceptions.ParcelNotFoundException;
import org.cempaka.cyclone.services.TestRunnerService;
import org.cempaka.cyclone.storage.data.TestRunMetricDataAccess;
import org.cempaka.cyclone.storage.data.TestRunStatusDataAccess;
import org.cempaka.cyclone.storage.repository.ParcelMetadataRepository;

@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/tests")
public class TestResource
{
    private final ParcelMetadataRepository parcelMetadataRepository;
    private final TestRunnerService testRunnerService;
    private final TestRunMetricDataAccess testRunMetricDataAccess;
    private final TestRunStatusDataAccess testRunStatusDataAccess;

    @Inject
    public TestResource(final ParcelMetadataRepository parcelMetadataRepository,
                        final TestRunnerService testRunnerService,
                        final TestRunMetricDataAccess testRunMetricDataAccess,
                        final TestRunStatusDataAccess testRunStatusDataAccess)
    {
        this.parcelMetadataRepository = checkNotNull(parcelMetadataRepository);
        this.testRunnerService = checkNotNull(testRunnerService);
        this.testRunMetricDataAccess = checkNotNull(testRunMetricDataAccess);
        this.testRunStatusDataAccess = checkNotNull(testRunStatusDataAccess);
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
    @Path("/parcels/{parcelId}/names/{testName}")
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
    @Path("/{id}/configuration")
    public Response getConfiguration(@PathParam("id") final String testId)
    {
        final TestRunConfiguration configuration = testRunStatusDataAccess.getConfiguration(testId);
        return Response.ok(configuration).build();
    }

    @GET
    @Path("/{id}/nodes/{node}/state")
    public Response getStatus(@PathParam("id") final String testId, @PathParam("node") final String nodeIdentifier)
    {
        final String state = testRunStatusDataAccess.getState(testId, nodeIdentifier);
        return Response.ok().entity(state).build();
    }

    @GET
    @Path("/{id}/metrics")
    public Response getMetrics(@PathParam("id") final String testId)
    {
        final List<TestRunMetric> events = testRunMetricDataAccess.getMetricsById(testId);
        return Response.ok().entity(events).build();
    }

    @GET
    @Path("/states/{state}")
    public Response getTestsWithState(@PathParam("state") final String state)
    {
        final Set<String> testsId = testRunStatusDataAccess.getTestsWithState(state);
        return Response.ok().entity(testsId).build();
    }
}
