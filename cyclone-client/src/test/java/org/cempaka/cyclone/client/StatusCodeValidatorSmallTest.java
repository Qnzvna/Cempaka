package org.cempaka.cyclone.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

import java.io.UnsupportedEncodingException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class StatusCodeValidatorSmallTest
{
    @Mock
    private HttpRequest request;
    @Mock
    private HttpResponse response;
    @Mock
    private StatusLine statusLine;

    private ResponseValidator statusCodeValidator = StatusCodeValidator.of(new int[]{200, 202}, ignore -> true);

    @BeforeEach
    void setUp()
    {
        given(response.getStatusLine()).willReturn(statusLine);
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 202})
    void shouldValidateStatusCode(final int statusCode)
    {
        //given
        given(statusLine.getStatusCode()).willReturn(statusCode);
        //when
        statusCodeValidator.validate(request, response);
        //then
        // no exceptions
    }

    @Test
    void shouldThrowForInvalidStatusCode() throws UnsupportedEncodingException
    {
        //given
        given(statusLine.getStatusCode()).willReturn(500);
        final String message = "message";
        given(response.getEntity()).willReturn(new StringEntity(message));
        //when
        final Throwable throwable = catchThrowable(() -> statusCodeValidator.validate(request, response));
        //then
        assertThat(throwable).isInstanceOf(InvalidResponseException.class)
            .hasMessageContaining("'500'")
            .hasMessageContaining(message);
    }

    @Test
    void shouldReturnEmptyMessageForNullEntity()
    {
        //given
        given(statusLine.getStatusCode()).willReturn(500);
        //when
        final Throwable throwable = catchThrowable(() -> statusCodeValidator.validate(request, response));
        //then
        assertThat(throwable).isInstanceOf(InvalidResponseException.class).hasMessageEndingWith("Reason: ''");
    }

    @Test
    void shouldNotValidateStatusCode()
    {
        //given
        final ResponseValidator statusCodeValidator = StatusCodeValidator.of(200, ignore -> false);
        //when
        statusCodeValidator.validate(request, response);
        //then
        // no exceptions
    }
}