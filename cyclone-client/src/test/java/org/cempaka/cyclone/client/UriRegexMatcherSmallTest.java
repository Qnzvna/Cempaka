package org.cempaka.cyclone.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.apache.http.HttpRequest;
import org.apache.http.RequestLine;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

//TODO
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
        given(requestLine.getUri()).willReturn("http://localhost:8080/test/12345");
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
        given(requestLine.getUri()).willReturn("http://localhost:8080/test/12345");
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
        given(requestLine.getUri()).willReturn("http://localhost:8080/test/12345");
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
        final RequestMatcher matcher = UriRegexMatcher.ofRegex(format);
        given(requestLine.getUri()).willReturn("http://localhost:8080/test/12345");
        given(httpRequest.getRequestLine()).willReturn(requestLine);
        //when
        final boolean match = matcher.test(httpRequest);
        //then
        assertThat(match).isFalse();
    }
}