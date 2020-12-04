package org.cempaka.cyclone.resources;

import static org.cempaka.cyclone.utils.Preconditions.checkNotNull;

import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.cempaka.cyclone.beans.ImmutableMetadata;
import org.cempaka.cyclone.beans.Metadata;
import org.cempaka.cyclone.services.MetadataService;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Singleton
@Path("/metadata")
@Produces(MediaType.APPLICATION_JSON)
public class MetadataResource
{
    private final MetadataService metadataService;

    @Inject
    public MetadataResource(final MetadataService metadataService)
    {
        this.metadataService = checkNotNull(metadataService);
    }

    @GET
    public List<Metadata> getMetadata()
    {
        return metadataService.getAllWithoutValue();
    }

    @GET
    @Path("/{id}")
    public Optional<Metadata> getMetadataById(@PathParam("id") final String id)
    {
        return metadataService.get(id);
    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void addMetadata(@PathParam("id") final String id, @FormDataParam("file") final InputStream data)
    {
        final Metadata metadata = ImmutableMetadata.builder()
            .id(id)
            .value(getBytes(data))
            .build();
        metadataService.put(metadata);
    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addMetadataJson(@PathParam("id") final String id, final String value)
    {
        metadataService.put(ImmutableMetadata.builder()
            .id(id)
            .value(value.getBytes())
            .build());
    }

    @DELETE
    @Path("/{id}")
    public void deleteMetadata(@PathParam("id") final String id)
    {
        metadataService.delete(id);
    }

    // TODO extract with ParcelResource
    private byte[] getBytes(final InputStream data)
    {
        try {
            return ByteStreams.toByteArray(data);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
