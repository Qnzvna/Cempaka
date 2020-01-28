package org.cempaka.cyclone.services;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.cempaka.cyclone.beans.Parcel;
import org.cempaka.cyclone.beans.ParcelUpload;
import org.cempaka.cyclone.storage.ParcelIndexer;
import org.cempaka.cyclone.storage.repositories.ParcelRepository;
import org.cempaka.cyclone.storage.repositories.TestRepository;
import org.cempaka.cyclone.tests.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ParcelUploadService
{
    private static final Logger LOG = LoggerFactory.getLogger(ParcelUploadService.class);

    private static final String DOWNLOAD_FAILURE_MESSAGE = "Downloading the parcel from [%s] failed with [%s] status code. Server responded: [%s].";

    private final CloseableHttpClient httpClient;
    private final ParcelIndexer parcelIndexer;
    private final ParcelRepository parcelRepository;
    private final TestRepository testRepository;

    @Inject
    public ParcelUploadService(final CloseableHttpClient httpClient,
                               final ParcelIndexer parcelIndexer,
                               final ParcelRepository parcelRepository,
                               final TestRepository testRepository)
    {
        this.httpClient = checkNotNull(httpClient);
        this.parcelIndexer = checkNotNull(parcelIndexer);
        this.parcelRepository = checkNotNull(parcelRepository);
        this.testRepository = checkNotNull(testRepository);
    }

    public UUID uploadParcel(final InputStream data)
    {
        checkNotNull(data);
        final UUID parcelId = UUID.randomUUID();
        uploadParcel(parcelId, data);
        return parcelId;
    }

    public void uploadParcel(final UUID parcelId, final InputStream data)
    {
        checkNotNull(parcelId);
        checkNotNull(data);
        uploadParcel(parcelId, getBytes(data));
    }

    private byte[] getBytes(final InputStream data)
    {
        try {
            return ByteStreams.toByteArray(data);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public UUID uploadParcel(final ParcelUpload parcelUpload)
    {
        checkNotNull(parcelUpload);
        final UUID parcelId = UUID.randomUUID();
        uploadParcel(parcelId, parcelUpload);
        return parcelId;
    }

    private void uploadParcel(final UUID parcelId, final ParcelUpload parcelUpload)
    {
        final String location = parcelUpload.getLocation();
        LOG.debug("About to download parcel from {}.", location);
        try (final CloseableHttpResponse response = httpClient.execute(new HttpGet(location))) {
            final int statusCode = response.getStatusLine().getStatusCode();
            LOG.debug("Response status code is {}.", statusCode);
            if (statusCode == HttpStatus.SC_OK) {
                uploadParcel(parcelId, EntityUtils.toByteArray(response.getEntity()));
            } else {
                final String errorMessage = String.format(DOWNLOAD_FAILURE_MESSAGE,
                    location,
                    statusCode,
                    EntityUtils.toString(response.getEntity()));
                LOG.debug(errorMessage);
                throw new ParcelUploadException(errorMessage);
            }
        } catch (IOException e) {
            throw new ParcelUploadException(e);
        }
    }

    private void uploadParcel(final UUID parcelId, final byte[] bytes)
    {
        LOG.debug("Adding parcel for {} uuid.", parcelId);
        final Parcel parcel = Parcel.of(parcelId, bytes);
        final Set<Test> tests = parcelIndexer.index(parcel);
        parcelRepository.put(parcel);
        testRepository.putAll(tests);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Parcel {} saved.", tests);
        }
    }

    public class ParcelUploadException extends RuntimeException
    {
        public ParcelUploadException(final String message)
        {
            super(message);
        }

        public ParcelUploadException(final Throwable cause)
        {
            super(cause);
        }
    }
}
