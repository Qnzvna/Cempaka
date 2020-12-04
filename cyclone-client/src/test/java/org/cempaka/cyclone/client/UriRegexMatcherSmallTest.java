package org.cempaka.cyclone.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTION;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTIONS;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTIONS_KEYS;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTIONS_LOGS;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTIONS_QUERY;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTIONS_SEARCH;
import static org.cempaka.cyclone.client.Endpoints.TEST_EXECUTION_METRICS;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.http.HttpRequest;
import org.apache.http.RequestLine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UriRegexMatcherSmallTest
{
    @Mock
    private HttpRequest httpRequest;
    @Mock
    private RequestLine requestLine;

    @ParameterizedTest
    @ValueSource(strings = {
        "/.*",
        "/test/12345",
        "/test/[0-9]+"
    })
    void shouldMatchRegex(final String regex)
    {
        //given
        final RequestMatcher matcher = UriRegexMatcher.ofRegex(regex);
        given(requestLine.getUri()).willReturn("/test/12345");
        given(httpRequest.getRequestLine()).willReturn(requestLine);
        //when
        final boolean match = matcher.test(httpRequest);
        //then
        assertThat(match).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "/test",
        "/test/42",
        "/test/[a-z]+"
    })
    void shouldNotMatchRegex(final String regex)
    {
        //given
        final RequestMatcher matcher = UriRegexMatcher.ofRegex(regex);
        given(requestLine.getUri()).willReturn("/test/12345");
        given(httpRequest.getRequestLine()).willReturn(requestLine);
        //when
        final boolean match = matcher.test(httpRequest);
        //then
        assertThat(match).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "/test/{param}",
        "/{0}/12345",
        "/{0}/{1}",
        "/test/12345",
    })
    void shouldMatchFormat(final String format)
    {
        //given
        final RequestMatcher matcher = UriRegexMatcher.ofFormat(format);
        given(requestLine.getUri()).willReturn("/test/12345");
        given(httpRequest.getRequestLine()).willReturn(requestLine);
        //when
        final boolean match = matcher.test(httpRequest);
        //then
        assertThat(match).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "/test",
        "/test/42",
        "/test/{0}/data"
    })
    void shouldNotMatchFormat(final String format)
    {
        //given
        final RequestMatcher matcher = UriRegexMatcher.ofFormat(format);
        given(requestLine.getUri()).willReturn("/test/12345");
        given(httpRequest.getRequestLine()).willReturn(requestLine);
        //when
        final boolean match = matcher.test(httpRequest);
        //then
        assertThat(match).isFalse();
    }

    @Test
    void shouldNotMatchUriWithAdditionalParts()
    {
        //given
        final RequestMatcher matcher = UriRegexMatcher.ofFormat("/api/tests/executions/{0}");
        given(requestLine.getUri()).willReturn("/api/tests/executions/12345/metrics");
        given(httpRequest.getRequestLine()).willReturn(requestLine);
        //when
        final boolean match = matcher.test(httpRequest);
        //then
        assertThat(match).isFalse();
    }

    @Test
    void shouldNotMatchUriWithParameters()
    {
        //given
        final RequestMatcher matcher = UriRegexMatcher.ofFormat("/api/tests/executions?{0}");
        given(requestLine.getUri()).willReturn("/api/tests/executions/12345/metrics");
        given(httpRequest.getRequestLine()).willReturn(requestLine);
        //when
        final boolean match = matcher.test(httpRequest);
        //then
        assertThat(match).isFalse();
    }

    @Test
    void shouldMatchOnlyOneFormat()
    {
        //given
        final List<RequestMatcher> formats = Stream.of(
            TEST_EXECUTIONS,
            TEST_EXECUTION,
            TEST_EXECUTION_METRICS,
            TEST_EXECUTIONS_KEYS,
            TEST_EXECUTIONS_QUERY,
            TEST_EXECUTIONS_SEARCH,
            TEST_EXECUTIONS_LOGS
        )
            .map(UriRegexMatcher::ofFormat)
            .collect(Collectors.toList());
        given(requestLine.getUri()).willReturn("/tests/executions/12345");
        given(httpRequest.getRequestLine()).willReturn(requestLine);
        //when
        final long passedFormats = formats.stream()
            .filter(requestMatcher -> requestMatcher.test(httpRequest))
            .count();
        //then
        assertThat(passedFormats).isOne();
    }
}