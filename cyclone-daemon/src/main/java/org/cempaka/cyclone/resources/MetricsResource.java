package org.cempaka.cyclone.resources;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Range;
import java.util.List;
import java.util.UUID;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.cempaka.cyclone.beans.MetricDataPoint;
import org.cempaka.cyclone.storage.repositories.TestMetricRepository;

@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/tests/executions")
public class MetricsResource
{
    private final TestMetricRepository testMetricRepository;

    @Inject
    public MetricsResource(final TestMetricRepository testMetricRepository)
    {
        this.testMetricRepository = checkNotNull(testMetricRepository);
    }

    @GET
    @Path("/{id}/metrics")
    public Response getMetrics(@PathParam("id") final String testId,
                               @QueryParam("from") final Long from,
                               @QueryParam("to") final Long to)
    {
        final List<MetricDataPoint> events = testMetricRepository.get(UUID.fromString(testId), getRange(from, to));
        return Response.ok().entity(events).build();
    }

    @GET
    @Path("/{id}/metrics/{name}")
    public Response getMetrics(@PathParam("id") final String testId,
                               @PathParam("name") final String name,
                               @QueryParam("from") final Long from,
                               @QueryParam("to") final Long to)
    {
        final List<MetricDataPoint> events = testMetricRepository
            .get(UUID.fromString(testId), name, getRange(from, to));
        return Response.ok().entity(events).build();
    }

    private Range<Long> getRange(final Long from, final Long to)
    {
        if (from == null && to == null) {
            return Range.all();
        } else if (from == null) {
            return Range.lessThan(to);
        } else if (to == null) {
            return Range.greaterThan(from);
        } else {
            return Range.closed(from, to);
        }
    }
}
