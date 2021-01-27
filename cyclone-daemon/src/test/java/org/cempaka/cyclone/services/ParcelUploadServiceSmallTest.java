package org.cempaka.cyclone.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.io.EmptyInputStream;
import org.cempaka.cyclone.beans.Parcel;
import org.cempaka.cyclone.beans.ParcelUpload;
import org.cempaka.cyclone.services.ParcelUploadService.ParcelUploadException;
import org.cempaka.cyclone.storage.ParcelIndexer;
import org.cempaka.cyclone.storage.repositories.ParcelRepository;
import org.cempaka.cyclone.storage.repositories.TestRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ParcelUploadServiceSmallTest
{
    @Mock
    private CloseableHttpClient httpClient;
    @Mock
    private ParcelIndexer parcelIndexer;
    @Mock
    private ParcelRepository parcelRepository;
    @Mock
    private TestRepository testRepository;

    @InjectMocks
    private ParcelUploadService parcelUploadService;

    @Before
    public void setUp()
    {
        given(parcelIndexer.index(any())).willReturn(ImmutableSet.of());
    }

    @Test
    public void shouldUploadParcelFromInputStream()
    {
        //given
        final InputStream inputStream = EmptyInputStream.INSTANCE;
        //when
        final UUID parcelId = parcelUploadService.uploadParcel(inputStream);
        //then
        assertThat(parcelId).isNotNull();
        verify(parcelIndexer, times(1)).index(any(Parcel.class));
        verify(parcelRepository, times(1)).put(any(Parcel.class));
        verify(testRepository, times(1)).putAll(anySetOf(org.cempaka.cyclone.tests.Test.class));
    }

    @Test
    public void shouldUploadParcelFromParcelUpload() throws IOException
    {
        //given
        final ParcelUpload parcelUpload = mock(ParcelUpload.class);
        final String location = "http://localhost";
        given(parcelUpload.getLocation()).willReturn(location);
        final StatusLine statusLine = mock(StatusLine.class);
        given(statusLine.getStatusCode()).willReturn(HttpStatus.SC_OK);
        final CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
        given(httpResponse.getStatusLine()).willReturn(statusLine);
        given(httpResponse.getEntity()).willReturn(new StringEntity(""));
        given(httpClient.execute(any(HttpGet.class))).willReturn(httpResponse);
        //when
        final UUID parcelId = parcelUploadService.uploadParcel(parcelUpload);
        //then
        assertThat(parcelId).isNotNull();
        verify(httpClient, times(1)).execute(argThat(
            httpUriRequest -> httpUriRequest.getURI().toString().equals(location)));
        verify(parcelIndexer, times(1)).index(any(Parcel.class));
        verify(parcelRepository, times(1)).put(any(Parcel.class));
        verify(testRepository, times(1)).putAll(anySetOf(org.cempaka.cyclone.tests.Test.class));
    }

    @Test
    public void shouldNotUploadParcelForNonOkResponse() throws IOException
    {
        //given
        final ParcelUpload parcelUpload = mock(ParcelUpload.class);
        final String location = "http://localhost";
        given(parcelUpload.getLocation()).willReturn(location);
        final StatusLine statusLine = mock(StatusLine.class);
        given(statusLine.getStatusCode()).willReturn(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        final CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
        given(httpResponse.getStatusLine()).willReturn(statusLine);
        final String errorMessage = "custom_error";
        given(httpResponse.getEntity()).willReturn(new StringEntity(errorMessage));
        given(httpClient.execute(any(HttpGet.class))).willReturn(httpResponse);
        //when
        final Throwable throwable = catchThrowable(() -> parcelUploadService.uploadParcel(parcelUpload));
        //then
        assertThat(throwable).isInstanceOf(ParcelUploadException.class).hasMessageContaining(errorMessage);
        verify(parcelIndexer, never()).index(any(Parcel.class));
        verify(parcelRepository, never()).put(any(Parcel.class));
        verify(testRepository, never()).putAll(anySetOf(org.cempaka.cyclone.tests.Test.class));
    }

    @Test
    public void shouldNotUploadParcelForIOException() throws IOException
    {
        //given
        final ParcelUpload parcelUpload = mock(ParcelUpload.class);
        final String location = "http://localhost";
        given(parcelUpload.getLocation()).willReturn(location);
        given(httpClient.execute(any(HttpGet.class))).willThrow(new IOException());
        //when
        final Throwable throwable = catchThrowable(() -> parcelUploadService.uploadParcel(parcelUpload));
        //then
        assertThat(throwable).isInstanceOf(ParcelUploadException.class);
        verify(parcelIndexer, never()).index(any(Parcel.class));
        verify(parcelRepository, never()).put(any(Parcel.class));
        verify(testRepository, never()).putAll(anySetOf(org.cempaka.cyclone.tests.Test.class));
    }
}