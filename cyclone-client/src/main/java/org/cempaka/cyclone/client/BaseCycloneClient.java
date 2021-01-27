package org.cempaka.cyclone.client;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.cempaka.cyclone.client.Endpoints.CLUSTER_NODE_CAPACITY;
import static org.cempaka.cyclone.client.Endpoints.CLUSTER_STATUS;
import static org.cempaka.cyclone.client.Endpoints.LIMIT_QUERY;
import static org.cempaka.cyclone.client.Endpoints.METADATA;
import static org.cempaka.cyclone.client.Endpoints.OFFSET_QUERY;
import static org.cempaka.cyclone.client.Endpoints.PARCEL;
import static org.cempaka.cyclone.client.Endpoints.PARCELS;
import static org.cempaka.cyclone.client.Endpoints.START_TEST;
import static org.cempaka.cyclone.client.Endpoints.STOP_TEST;
import static org.cempaka.cyclone.client.Endpoints.TESTS;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTION;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTIONS;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTIONS_KEYS;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTIONS_QUERY;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTIONS_SEARCH;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTION_LOGS;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTION_METRICS;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BaseCycloneClient implements CycloneClient
{
    private final String apiUrl;
    private final ObjectMapper objectMapper;

    public BaseCycloneClient(final String apiUrl, final ObjectMapper objectMapper)
    {
        this.apiUrl = checkNotNull(apiUrl);
        this.objectMapper = checkNotNull(objectMapper);
    }

    protected String getApiUrl()
    {
        return apiUrl;
    }

    protected String createClusterStatusResource()
    {
        return createResource(CLUSTER_STATUS);
    }

    protected String createClusterNodeCapacityResource(final String node)
    {
        return createResource(format(CLUSTER_NODE_CAPACITY, node));
    }

    protected String createParcelsResource()
    {
        return createResource(PARCELS);
    }

    protected String createParcelResource(final UUID parcelId)
    {
        return createResource(format(PARCEL, parcelId));
    }

    protected String createTestsResource()
    {
        return createResource(TESTS);
    }

    protected String createStartTestResource()
    {
        return createResource(START_TEST);
    }

    protected String createStopTestResource(final UUID testId)
    {
        return createResource(format(STOP_TEST, testId));
    }

    protected String createTestExecutionsResource()
    {
        return createResource(TEST_EXECUTIONS);
    }

    protected String createLimitQuery(final int limit)
    {
        return format(LIMIT_QUERY, limit);
    }

    protected String createOffsetQuery(final int offset)
    {
        return format(OFFSET_QUERY, offset);
    }

    protected String createTestExecutionsQueryResource(final String query)
    {
        return createResource(format(TEST_EXECUTIONS_QUERY, query));
    }

    protected String createTestExecutionsMetricsQuery(final UUID testExecutionId)
    {
        return createResource(format(TEST_EXECUTION_METRICS, testExecutionId));
    }

    protected String createTestExecutionsSearchResource(final String query)
    {
        return createResource(format(TEST_EXECUTIONS_SEARCH, query));
    }

    protected String createTestExecutionResource(final UUID testExecutionId)
    {
        return createResource(format(TEST_EXECUTION, testExecutionId));
    }

    protected String createTestExecutionsKeysResource()
    {
        return createResource(TEST_EXECUTIONS_KEYS);
    }

    protected String createTestExecutionLogsResource(final UUID testExecution, final Instant from)
    {
        return createResource(format(TEST_EXECUTION_LOGS, testExecution, from.getEpochSecond()));
    }

    protected String createMetadataResource(final String id)
    {
        return createResource(format(METADATA, id));
    }

    protected String createQuery(final String key, final Set<String> values)
    {
        return values.stream().map(state -> key + "=" + state).collect(Collectors.joining("&"));
    }

    private String format(final String value, final Object... args)
    {
        String formattedValue = value;
        for (final Object arg : args) {
            formattedValue = formattedValue.replaceFirst("\\{[^/]*}", arg.toString());
        }
        return formattedValue;
    }

    private String createResource(final String url)
    {
        return getApiUrl() + checkNotNull(url);
    }

    protected <T> byte[] serialize(final T data)
    {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new DataProcessingException(e);
        }
    }

    protected <T> Function<byte[], T> createDeserializer(final Class<T> clazz)
    {
        return data -> deserialize(data, clazz);
    }

    protected <T> Function<byte[], T> createDeserializer(final TypeReference<T> typeReference)
    {
        return data -> deserialize(data, typeReference);
    }

    protected <T> T deserialize(final byte[] data, final TypeReference<T> typeReference)
    {
        return this.deserialize(data, objectMapper.getTypeFactory().constructType(typeReference));
    }

    protected <T> T deserialize(final byte[] data, final Class<T> clazz)
    {
        return this.deserialize(data, objectMapper.getTypeFactory().constructType(clazz));
    }

    private <T> T deserialize(final byte[] data, final JavaType type)
    {
        try {
            return objectMapper.readValue(data, type);
        } catch (IOException e) {
            throw new DataProcessingException(e);
        }
    }
}
