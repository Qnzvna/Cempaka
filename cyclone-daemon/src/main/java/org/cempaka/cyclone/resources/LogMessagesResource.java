package org.cempaka.cyclone.resources;

import static org.cempaka.cyclone.core.utils.Preconditions.checkNotNull;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.cempaka.cyclone.core.log.LogMessage;
import org.cempaka.cyclone.storage.repositories.LogMessageRepository;

@Singleton
@Path("/tests/executions")
@Produces(MediaType.APPLICATION_JSON)
public class LogMessagesResource
{
    private final LogMessageRepository logMessageRepository;

    @Inject
    public LogMessagesResource(final LogMessageRepository logMessageRepository)
    {
        this.logMessageRepository = checkNotNull(logMessageRepository);
    }

    @GET
    @Path("{id}/logs")
    public Response getNewerThan(@PathParam("id") final String id,
                                 @DefaultValue("0") @QueryParam("from") final long from)
    {
        final List<String> logLines = logMessageRepository.getNewerThan(UUID.fromString(id),
            Instant.ofEpochSecond(from)).stream()
            .map(LogMessage::getLogLine)
            .collect(Collectors.toList());
        return Response.ok().entity(logLines).build();
    }
}
