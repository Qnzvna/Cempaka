package org.cempaka.cyclone.resources;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;
import java.util.UUID;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.cempaka.cyclone.beans.ParcelUpload;
import org.cempaka.cyclone.services.ParcelUploadService;
import org.cempaka.cyclone.storage.repositories.ParcelRepository;
import org.cempaka.cyclone.storage.repositories.TestRepository;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/parcels")
public class ParcelResource
{
    private final ParcelUploadService parcelUploadService;
    private final ParcelRepository parcelRepository;
    private final TestRepository testRepository;

    @Inject
    public ParcelResource(final ParcelUploadService parcelUploadService,
                          final ParcelRepository parcelRepository,
                          final TestRepository testRepository)
    {
        this.parcelUploadService = checkNotNull(parcelUploadService);
        this.parcelRepository = checkNotNull(parcelRepository);
        this.testRepository = checkNotNull(testRepository);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public UUID addParcel(@FormDataParam("file") final InputStream data)
    {
        return parcelUploadService.uploadParcel(data);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public UUID addParcel(final ParcelUpload parcelUpload)
    {
        return parcelUploadService.uploadParcel(parcelUpload);
    }

    @DELETE
    @Path("/{id}")
    public void deleteParcel(@PathParam("id") final String parcelId)
    {
        testRepository.delete(UUID.fromString(parcelId));
        parcelRepository.delete(UUID.fromString(parcelId));
    }
}
