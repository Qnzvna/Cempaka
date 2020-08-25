package org.cempaka.cyclone.client;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.cempaka.cyclone.client.Endpoints.CLUSTER_NODE_CAPACITY;
import static org.cempaka.cyclone.client.Endpoints.CLUSTER_STATUS;
import static org.cempaka.cyclone.client.Endpoints.METADATA;
import static org.cempaka.cyclone.client.Endpoints.PARCEL;
import static org.cempaka.cyclone.client.Endpoints.PARCELS;
import static org.cempaka.cyclone.client.Endpoints.START_TEST;
import static org.cempaka.cyclone.client.Endpoints.STOP_TEST;
import static org.cempaka.cyclone.client.Endpoints.TESTS;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTION;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTIONS;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTIONS_KEYS;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTIONS_LOGS;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTIONS_QUERY;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTIONS_SEARCH;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTION_METRICS;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.collect.ImmutableSet;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ApacheCycloneClient extends BaseCycloneClient
{
    private final CloseableHttpClient httpClient;
    private final ResponseValidator responseValidator;

    public static Builder builder()
    {
        return new Builder();
    }

    private ApacheCycloneClient(final String apiUrl,
                                final ObjectMapper objectMapper,
                                final CloseableHttpClient httpClient,
                                final ResponseValidator responseValidator)
    {
        super(apiUrl, objectMapper);
        this.httpClient = checkNotNull(httpClient);
        this.responseValidator = checkNotNull(responseValidator);
    }

    @Override
    public Map<String, Boolean> getClusterStatus()
    {
        final HttpGet httpGet = new HttpGet(createResource(CLUSTER_STATUS));
        return runRequest(createDeserializer(new TypeReference<Map<String, Boolean>>() {}), httpGet);
    }

    @Override
    public NodeCapacity getNodeCapacity(final String node)
    {
        checkNotNull(node);
        final HttpGet httpGet = new HttpGet(createResource(MessageFormat.format(CLUSTER_NODE_CAPACITY, node)));
        return runRequest(createDeserializer(NodeCapacity.class), httpGet);
    }

    @Override
    public UUID uploadParcel(final File parcel)
    {
        checkNotNull(parcel);
        final HttpPost httpPost = new HttpPost(createResource(PARCELS));
        httpPost.setEntity(MultipartEntityBuilder.create()
            .addBinaryBody("file", parcel)
            .build());
        return runRequest(createDeserializer(UUID.class), httpPost);
    }

    @Override
    public UUID uploadParcel(final ParcelUpload parcelUpload)
    {
        checkNotNull(parcelUpload);
        final HttpPost httpPost = new HttpPost(createResource(PARCELS));
        return runRequest(parcelUpload, createDeserializer(UUID.class), httpPost);
    }

    @Override
    public void deleteParcel(final UUID parcelId)
    {
        checkNotNull(parcelId);
        final HttpDelete httpDelete = new HttpDelete(createResource(MessageFormat.format(PARCEL, parcelId)));
        runRequest(httpDelete);
    }

    @Override
    public Set<Test> getTests()
    {
        final HttpGet httpGet = new HttpGet(createResource(TESTS));
        return runRequest(createDeserializer(new TypeReference<Set<Test>>() {}), httpGet);
    }

    @Override
    public UUID startTest(final TestExecutionProperties testExecutionProperties)
    {
        checkNotNull(testExecutionProperties);
        final HttpPost httpPost = new HttpPost(createResource(START_TEST));
        return runRequest(testExecutionProperties, createDeserializer(UUID.class), httpPost);
    }

    @Override
    public void stopTest(final UUID testExecutionId)
    {
        checkNotNull(testExecutionId);
        final HttpPost httpPost = new HttpPost(createResource(MessageFormat.format(STOP_TEST, testExecutionId)));
        runRequest(httpPost);
    }

    @Override
    public TestExecutionPage getTestExecutions()
    {
        final HttpGet httpGet = new HttpGet(createResource(TEST_EXECUTIONS));
        return runRequest(createDeserializer(TestExecutionPage.class), httpGet);
    }

    @Override
    public TestExecutionPage getTestExecutionsLimitedBy(final int limit)
    {
        final String query = MessageFormat.format("limit={0}", limit);
        return getTestExecutionPage(query);
    }

    @Override
    public TestExecutionPage getTestExecutionsOffsetBy(final int offset)
    {
        final String query = MessageFormat.format("offset={0}", offset);
        return getTestExecutionPage(query);
    }

    @Override
    public TestExecutionPage getTestExecutions(final int limit, final int offset)
    {
        final String query = MessageFormat.format("limit={0}&offset={1}", limit, offset);
        return getTestExecutionPage(query);
    }

    private TestExecutionPage getTestExecutionPage(final String query)
    {
        final HttpGet httpGet = new HttpGet(createResource(MessageFormat.format(TEST_EXECUTIONS_QUERY, query)));
        return runRequest(createDeserializer(TestExecutionPage.class), httpGet);
    }

    @Override
    public Set<MetricDataPoint> getTestExecutionMetrics(final UUID testExecutionId)
    {
        checkNotNull(testExecutionId);
        final HttpGet httpGet = new HttpGet(
            createResource(MessageFormat.format(TEST_EXECUTION_METRICS, testExecutionId)));
        return runRequest(createDeserializer(new TypeReference<Set<MetricDataPoint>>() {}), httpGet);
    }

    @Override
    public TestExecutionPage searchTestExecutionsByStates(final Set<String> states)
    {
        checkNotNull(states);
        return searchTestExecutions(states, ImmutableSet.of());
    }

    @Override
    public TestExecutionPage searchTestExecutionsByNames(final Set<String> names)
    {
        checkNotNull(names);
        return searchTestExecutions(ImmutableSet.of(), names);
    }

    @Override
    public TestExecutionPage searchTestExecutions(final Set<String> states, final Set<String> names)
    {
        checkNotNull(states);
        checkNotNull(names);
        final String query = Stream.of(createQuery("state", states), createQuery("name", names))
            .filter(queryPart -> !queryPart.isEmpty())
            .collect(Collectors.joining("&"));
        final HttpGet httpGet = new HttpGet(createResource(MessageFormat.format(TEST_EXECUTIONS_SEARCH, query)));
        return runRequest(createDeserializer(TestExecutionPage.class), httpGet);
    }

    private String createQuery(final String key, final Set<String> values)
    {
        return values.stream().map(state -> key + "=" + state).collect(Collectors.joining("&"));
    }

    @Override
    public Set<TestExecution> getTestExecutions(final UUID testExecutionId)
    {
        checkNotNull(testExecutionId);
        final HttpGet httpGet = new HttpGet(createResource(MessageFormat.format(TEST_EXECUTION, testExecutionId)));
        return runRequest(createDeserializer(new TypeReference<Set<TestExecution>>() {}), httpGet);
    }

    @Override
    public void deleteTestExecution(final UUID testExecutionId)
    {
        checkNotNull(testExecutionId);
        final HttpDelete httpDelete = new HttpDelete(
            createResource(MessageFormat.format(TEST_EXECUTION, testExecutionId)));
        runRequest(httpDelete);
    }

    @Override
    public Set<UUID> getTestExecutionsIds()
    {
        final HttpGet httpGet = new HttpGet(createResource(TEST_EXECUTIONS_KEYS));
        return runRequest(createDeserializer(new TypeReference<Set<UUID>>() {}), httpGet);
    }

    @Override
    public Set<String> getTestExecutionLogMessages(final UUID testExecutionId, final Instant from)
    {
        final HttpGet httpGet = new HttpGet(
            createResource(MessageFormat.format(TEST_EXECUTIONS_LOGS, testExecutionId, from.getEpochSecond())));
        return runRequest(createDeserializer(new TypeReference<Set<String>>() {}), httpGet);
    }

    @Override
    public void uploadMetadata(final String id, final File data)
    {
        checkNotNull(id);
        checkNotNull(data);
        final HttpPut httpPut = new HttpPut(createResource(MessageFormat.format(METADATA, id)));
        httpPut.setEntity(MultipartEntityBuilder.create()
            .addBinaryBody("file", data)
            .build());
        runRequest(httpPut);
    }

    @Override
    public void uploadMetadata(final String id, final byte[] data)
    {
        checkNotNull(id);
        checkNotNull(data);
        final HttpPut httpPut = new HttpPut(createResource(MessageFormat.format(METADATA, id)));
        httpPut.setEntity(MultipartEntityBuilder.create()
            .addBinaryBody("file", data)
            .build());
        runRequest(httpPut);
    }

    @Override
    public void deleteMetadata(final String id)
    {
        checkNotNull(id);
        final HttpDelete httpDelete = new HttpDelete(createResource(MessageFormat.format(METADATA, id)));
        runRequest(httpDelete);
    }

    private void runRequest(final HttpRequestBase request)
    {
        executeRequest(data -> null, request);
    }

    private <O> O runRequest(final Function<byte[], O> deserializer,
                             final HttpRequestBase request)
    {
        checkNotNull(deserializer);
        checkNotNull(request);
        final O object = executeRequest(deserializer, request);
        if (object == null) {
            throw new DataProcessingException("Empty response not expected.");
        } else {
            return object;
        }
    }

    private @Nullable
    <I, O> O runRequest(@Nullable final I input,
                        final Function<byte[], O> deserializer,
                        final HttpEntityEnclosingRequestBase request)
    {
        checkNotNull(deserializer);
        checkNotNull(request);
        if (input != null) {
            request.setEntity(new ByteArrayEntity(serialize(input)));
            request.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        }
        return executeRequest(deserializer, request);
    }

    private @Nullable
    <O> O executeRequest(final Function<byte[], O> deserializer, final HttpRequestBase request)
    {
        try (final CloseableHttpResponse response = httpClient.execute(request)) {
            responseValidator.validate(request, response);
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                return deserializer.apply(EntityUtils.toByteArray(entity));
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    public static class Builder
    {
        private String apiUrl;
        private ObjectMapper objectMapper;
        private CloseableHttpClient httpClient;
        private ResponseValidator responseValidator;

        private Builder()
        {
        }

        public Builder withApiUrl(final String apiUrl)
        {
            this.apiUrl = checkNotNull(apiUrl);
            return this;
        }

        public Builder withHttpClient(final CloseableHttpClient httpClient)
        {
            this.httpClient = checkNotNull(httpClient);
            return this;
        }

        public Builder withObjectMapper(final ObjectMapper objectMapper)
        {
            this.objectMapper = checkNotNull(objectMapper);
            return this;
        }

        public CycloneClient build()
        {
            final ObjectMapper objectMapper = this.objectMapper == null ?
                new ObjectMapper()
                    .registerModule(new GuavaModule())
                    .registerModule(new Jdk8Module()) :
                this.objectMapper;
            final CloseableHttpClient httpClient = this.httpClient == null ?
                HttpClients.createDefault() :
                this.httpClient;
            final ResponseValidator responseValidator = this.responseValidator == null ?
                new DefaultResponseValidator(apiUrl) :
                this.responseValidator;
            return new ApacheCycloneClient(apiUrl, objectMapper, httpClient, responseValidator);
        }
    }
}
