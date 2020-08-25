package org.cempaka.cyclone.client;

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
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTIONS_QUERY;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTIONS_SEARCH;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTION_METRICS;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

class DefaultResponseValidator extends ResponseValidator
{
    private final Set<ResponseValidator> validators;

    DefaultResponseValidator(final String apiUrl)
    {
        validators = ImmutableSet.<ResponseValidator>builder()
            .add(StatusCodeValidator.of(HttpStatus.SC_OK, UriRegexMatcher.ofFormat(apiUrl + CLUSTER_STATUS)))
            .add(StatusCodeValidator.of(HttpStatus.SC_OK, UriRegexMatcher.ofFormat(apiUrl + CLUSTER_NODE_CAPACITY)))
            .add(StatusCodeValidator.of(HttpStatus.SC_OK, UriRegexMatcher.ofFormat(apiUrl + PARCELS)))
            .add(StatusCodeValidator.of(HttpStatus.SC_NO_CONTENT, UriRegexMatcher.ofFormat(apiUrl + PARCEL)))
            .add(StatusCodeValidator.of(HttpStatus.SC_OK, UriRegexMatcher.ofFormat(apiUrl + TESTS)))
            .add(StatusCodeValidator.of(HttpStatus.SC_ACCEPTED, UriRegexMatcher.ofFormat(apiUrl + START_TEST)))
            .add(StatusCodeValidator.of(HttpStatus.SC_NO_CONTENT, UriRegexMatcher.ofFormat(apiUrl + STOP_TEST)))
            .add(StatusCodeValidator.of(HttpStatus.SC_OK, UriRegexMatcher.ofFormat(apiUrl + TEST_EXECUTIONS)))
            .add(StatusCodeValidator.of(HttpStatus.SC_OK, UriRegexMatcher.ofFormat(apiUrl + TEST_EXECUTIONS_QUERY)))
            .add(StatusCodeValidator.of(
                new int[]{HttpStatus.SC_OK, HttpStatus.SC_NO_CONTENT},
                UriRegexMatcher.ofFormat(apiUrl + TEST_EXECUTION)
            ))
            .add(StatusCodeValidator.of(HttpStatus.SC_OK, UriRegexMatcher.ofFormat(apiUrl + TEST_EXECUTION_METRICS)))
            .add(StatusCodeValidator.of(HttpStatus.SC_OK, UriRegexMatcher.ofFormat(apiUrl + TEST_EXECUTIONS_SEARCH)))
            .add(StatusCodeValidator.of(HttpStatus.SC_OK, UriRegexMatcher.ofFormat(apiUrl + TEST_EXECUTIONS_KEYS)))
            .add(StatusCodeValidator.of(HttpStatus.SC_NO_CONTENT, UriRegexMatcher.ofFormat(apiUrl + METADATA)))
            .build();
    }

    @Override
    public void validate(final HttpRequest request, final HttpResponse response)
    {
        validators.stream()
            .filter(validator -> validator.isEnabled(request))
            .forEach(validator -> validator.runValidation(response));
    }

    @Override
    protected void runValidation(final HttpResponse response)
    {
    }

    @Override
    protected boolean isEnabled(final HttpRequest request)
    {
        return true;
    }

}
