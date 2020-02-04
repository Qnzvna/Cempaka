package org.cempaka.cyclone;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.cempaka.cyclone.beans.MetricDataPoint;
import org.cempaka.cyclone.beans.NodeCapacity;
import org.cempaka.cyclone.beans.ParcelUpload;
import org.cempaka.cyclone.resources.TestExecutionsPage;
import org.cempaka.cyclone.tests.Test;
import org.cempaka.cyclone.tests.TestExecution;
import org.cempaka.cyclone.tests.TestExecutionProperties;

public class CycloneTestClient
{
    private final String apiUrl;
    private final HttpClient httpClient = HttpClients.createDefault();
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new GuavaModule());

    public CycloneTestClient(final String apiUrl)
    {
        this.apiUrl = checkNotNull(apiUrl);
    }

    public List<Test> getTests()
    {
        final HttpGet httpGet = new HttpGet(apiUrl + "/tests");
        return runRequest(httpGet, new TypeReference<List<Test>>() {});
    }

    public UUID uploadParcel(final File parcel)
    {
        final HttpPost httpPost = new HttpPost(apiUrl + "/parcels");
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

    public UUID uploadParcel(final ParcelUpload parcelUpload)
    {
        final HttpPost httpPost = new HttpPost(apiUrl + "/parcels");
        return runRequest(parcelUpload, UUID.class, httpPost);
    }

    public void deleteParcel(final UUID id)
    {
        final HttpDelete httpDelete = new HttpDelete(apiUrl + String.format("/parcels/%s", id));
        runRequest(httpDelete, new TypeReference<Void>() {});
    }

    public UUID startTest(final TestExecutionProperties testExecutionProperties)
    {
        final HttpPost httpPost = new HttpPost(apiUrl + "/tests/start");
        return runRequest(testExecutionProperties, UUID.class, httpPost);
    }

    public void stopTest(final UUID testId)
    {
        final HttpPost httpPost = new HttpPost(apiUrl + String.format("/tests/%s/stop", testId));
        runRequest(null, Void.class, httpPost);
    }

    public List<TestExecution> searchTestExecutions(final Set<String> states, final Set<String> names)
    {
        final String parameters = Stream.concat(states.stream().map(state -> "state=" + state),
            names.stream().map(name -> "name=" + name))
            .collect(Collectors.joining("&"));
        final HttpGet httpGet = new HttpGet(apiUrl + "/tests/executions/search?" + parameters);
        return runRequest(httpGet, new TypeReference<TestExecutionsPage>() {}).getTestExecutions();
    }

    public List<TestExecution> getTestExecutions()
    {
        final HttpGet httpGet = new HttpGet(apiUrl + "/tests/executions");
        return runRequest(httpGet, new TypeReference<TestExecutionsPage>() {}).getTestExecutions();
    }

    public List<TestExecution> getTestExecutions(final int limit, final int offset)
    {
        final HttpGet httpGet = new HttpGet(apiUrl + "/tests/executions?limit=" + limit + "&offset=" + offset);
        return runRequest(httpGet, new TypeReference<TestExecutionsPage>() {}).getTestExecutions();
    }

    public Set<TestExecution> getTestExecutions(final UUID id)
    {
        final HttpGet httpGet = new HttpGet(apiUrl + "/tests/executions/" + id);
        return runRequest(httpGet, new TypeReference<Set<TestExecution>>() {});
    }

    public Set<UUID> getTestExecutionKeys()
    {
        final HttpGet httpGet = new HttpGet(apiUrl + "/tests/executions/keys");
        return runRequest(httpGet, new TypeReference<Set<UUID>>() {});
    }

    public void deleteTestExecution(final UUID id)
    {
        final HttpDelete httpDelete = new HttpDelete(apiUrl + "/tests/executions/" + id);
        runRequest(httpDelete, new TypeReference<Void>() {});
    }

    public Map<String, Boolean> getStatus()
    {
        final HttpGet httpGet = new HttpGet(apiUrl + "/cluster/status");
        return runRequest(httpGet, new TypeReference<Map<String, Boolean>>() {});
    }

    public NodeCapacity getNodeCapacity(final String node)
    {
        final HttpGet httpGet = new HttpGet(apiUrl + "/cluster/" + node + "/capacity");
        return runRequest(httpGet, new TypeReference<NodeCapacity>() {});
    }

    public List<MetricDataPoint> getMetrics(final UUID id)
    {
        final HttpGet httpGet = new HttpGet(apiUrl + "/tests/executions/" + id + "/metrics");
        return runRequest(httpGet, new TypeReference<List<MetricDataPoint>>() {});
    }

    private <I, O> O runRequest(final I input,
                                final Class<O> outputClass,
                                final HttpPost httpPost)
    {
        try {
            if (input != null) {
                final String requestString = objectMapper.writeValueAsString(input);
                httpPost.setEntity(new StringEntity(requestString, ContentType.APPLICATION_JSON));
            } else {
                httpPost.setEntity(new StringEntity("", ContentType.APPLICATION_JSON));
            }
            final HttpResponse httpResponse = httpClient.execute(httpPost);
            final HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                final String stringResponse = EntityUtils.toString(entity);
                return objectMapper.readValue(stringResponse, outputClass);
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private <T> T runRequest(final HttpUriRequest request, final TypeReference<T> reference)
    {
        try {
            final HttpResponse httpResponse = httpClient.execute(request);
            final HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                final String stringResponse = EntityUtils.toString(entity);
                return objectMapper.readValue(stringResponse, reference);
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
