package org.cempaka.cyclone;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.UUID;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.cempaka.cyclone.beans.TestRunConfiguration;
import org.cempaka.cyclone.beans.TestState;

public class CycloneTestClient
{
    private static final String API_URL = "http://127.0.0.1:50000/api";

    private final HttpClient httpClient = HttpClients.createDefault();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Boolean> getStatus()
    {
        final HttpGet httpGet = new HttpGet(API_URL + "/status");
        try {
            final HttpResponse httpResponse = httpClient.execute(httpGet);
            final String stringResponse = EntityUtils.toString(httpResponse.getEntity());
            return objectMapper.readValue(stringResponse, new TypeReference<Map<String, Boolean>>() {});
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public UUID uploadParcel(final File parcel)
    {
        final HttpPost httpPost = new HttpPost(API_URL + "/parcels");
        httpPost.setEntity(MultipartEntityBuilder.create()
            .addBinaryBody("file", parcel)
            .build());
        try {
            final HttpResponse httpResponse = httpClient.execute(httpPost);
            final String stringResponse = EntityUtils.toString(httpResponse.getEntity());
            return objectMapper.readValue(stringResponse, UUID.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public UUID runTest(final TestRunConfiguration testRunConfiguration)
    {
        final HttpPost httpPost = new HttpPost(API_URL + "/tests/run");
        try {
            final String requestString = objectMapper.writeValueAsString(testRunConfiguration);
            httpPost.setEntity(new StringEntity(requestString, ContentType.APPLICATION_JSON));
            final HttpResponse httpResponse = httpClient.execute(httpPost);
            final String stringResponse = EntityUtils.toString(httpResponse.getEntity());
            return objectMapper.readValue(stringResponse, UUID.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String getTestState(final UUID testId, final String nodeId)
    {
        final HttpGet httpGet = new HttpGet(API_URL + String.format("/tests/%s/nodes/%s/state", testId, nodeId));
        try {
            final HttpResponse httpResponse = httpClient.execute(httpGet);
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
