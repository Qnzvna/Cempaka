package org.cempaka.cyclone.client;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.apache.commons.codec.Charsets;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class ApacheCycloneClientSmallTest
{
    private static final String API_URL = "http://localhost:8080";

    @Mock
    private CloseableHttpClient httpClient;
    @Mock
    private CloseableHttpResponse response;
    @Mock
    private StatusLine statusLine;

    @BeforeEach
    void setUp() throws IOException
    {
        given(httpClient.execute(any())).willReturn(response);
        given(response.getStatusLine()).willReturn(statusLine);
    }

    @ParameterizedTest(name = "[{index}]")
    @ArgumentsSource(MethodsSource.class)
    void shouldFailForNonOkStatusCode(final Consumer<CycloneClient> consumer) throws JsonProcessingException
    {
        //given
        final ObjectMapper objectMapper = mock(ObjectMapper.class);
        given(objectMapper.writeValueAsBytes(any())).willReturn(new byte[]{});
        final CycloneClient cycloneClient = ApacheCycloneClient.builder()
            .withApiUrl(API_URL)
            .withHttpClient(httpClient)
            .withObjectMapper(objectMapper)
            .build();
        given(statusLine.getStatusCode()).willReturn(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        //when
        final Throwable throwable = catchThrowable(() -> consumer.accept(cycloneClient));
        //then
        assertThat(throwable).isInstanceOf(InvalidResponseException.class);
    }

    private static class MethodsSource implements ArgumentsProvider
    {
        @Override
        public Stream<? extends Arguments> provideArguments(final ExtensionContext extensionContext)
        {
            return Stream.of(
                arguments((Consumer<CycloneClient>) CycloneClient::getClusterStatus),
                arguments((Consumer<CycloneClient>) client -> client.getNodeCapacity("node")),
                arguments((Consumer<CycloneClient>) client -> client.uploadParcel(mock(File.class))),
                arguments((Consumer<CycloneClient>) client -> client.uploadParcel(mock(ParcelUpload.class))),
                arguments((Consumer<CycloneClient>) client -> client.deleteParcel(java.util.UUID.randomUUID())),
                arguments((Consumer<CycloneClient>) CycloneClient::getTests),
                arguments((Consumer<CycloneClient>) client -> client.startTest(mock(TestExecutionProperties.class))),
                arguments((Consumer<CycloneClient>) client -> client.stopTest(java.util.UUID.randomUUID())),
                arguments((Consumer<CycloneClient>) CycloneClient::getTestExecutions),
                arguments((Consumer<CycloneClient>) client -> client.getTestExecutionsLimitedBy(42)),
                arguments((Consumer<CycloneClient>) client -> client.getTestExecutionsOffsetBy(42)),
                arguments((Consumer<CycloneClient>) client -> client.getTestExecutions(42, 42)),
                arguments(
                    (Consumer<CycloneClient>) client -> client.getTestExecutionMetrics(java.util.UUID.randomUUID())),
                arguments((Consumer<CycloneClient>) client -> client.searchTestExecutionsByStates(ImmutableSet.of())),
                arguments((Consumer<CycloneClient>) client -> client.searchTestExecutionsByNames(ImmutableSet.of())),
                arguments((Consumer<CycloneClient>) client -> client
                    .searchTestExecutions(ImmutableSet.of(), ImmutableSet.of())),
                arguments((Consumer<CycloneClient>) client -> client.getTestExecutions(java.util.UUID.randomUUID())),
                arguments((Consumer<CycloneClient>) client -> client.deleteTestExecution(java.util.UUID.randomUUID())),
                arguments((Consumer<CycloneClient>) CycloneClient::getTestExecutionsIds)
            );
        }
    }
}