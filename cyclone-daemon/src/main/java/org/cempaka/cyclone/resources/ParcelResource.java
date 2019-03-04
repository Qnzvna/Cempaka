package org.cempaka.cyclone.resources;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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
import org.cempaka.cyclone.beans.Parcel;
import org.cempaka.cyclone.beans.ParcelMetadata;
import org.cempaka.cyclone.storage.ParcelIndexer;
import org.cempaka.cyclone.storage.ParcelMetadataRepository;
import org.cempaka.cyclone.storage.ParcelRepository;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/parcels")
public class ParcelResource
{
    private static final Logger LOG = LoggerFactory.getLogger(ParcelResource.class);

    private final ParcelRepository parcelRepository;
    private final ParcelIndexer parcelIndexer;
    private final ParcelMetadataRepository parcelMetadataRepository;

    @Inject
    public ParcelResource(final ParcelRepository parcelRepository,
                          final ParcelIndexer parcelIndexer,
                          final ParcelMetadataRepository parcelMetadataRepository)
    {
        this.parcelRepository = checkNotNull(parcelRepository);
        this.parcelIndexer = checkNotNull(parcelIndexer);
        this.parcelMetadataRepository = checkNotNull(parcelMetadataRepository);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public UUID addParcel(@FormDataParam("file") final InputStream data) throws IOException
    {
        final UUID parcelId = UUID.randomUUID();
        LOG.debug("Adding parcel for {} uuid.", parcelId);
        final Parcel parcel = Parcel.of(parcelId, ByteStreams.toByteArray(data));
        final ParcelMetadata parcelMetadata = parcelIndexer.index(parcel);
        parcelRepository.put(parcel);
        parcelMetadataRepository.put(parcelMetadata);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Parcel {} saved.", parcelMetadata);
        }
        return parcelId;
    }

    @DELETE
    @Path("/{id}")
    public void deleteParcel(@PathParam("id") final String parcelId)
    {
        parcelRepository.delete(UUID.fromString(parcelId));
    }
}
