package org.cempaka.cyclone.client;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.function.Function;

abstract class BaseCycloneClient implements CycloneClient
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

    protected String createResource(final String url)
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
