package org.cempaka.cyclone.log;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.net.SocketException;
import java.util.UUID;
import java.util.function.BiConsumer;
import org.cempaka.cyclone.core.channel.LogPayload;
import org.cempaka.cyclone.core.channel.Payload;
import org.cempaka.cyclone.core.channel.PayloadType;
import org.cempaka.cyclone.core.channel.UdpDaemonChannel;
import org.cempaka.cyclone.core.log.LoggerFactoryConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
class CycloneLoggerMediumTest
{
    private static final Logger LOG = LoggerFactory.getLogger("cyclone");
    private static final int PORT = 5000;
    private static final String TEST_ID = UUID.randomUUID().toString();

    private final UdpDaemonChannel channel = new UdpDaemonChannel();

    @Mock
    private BiConsumer<Integer, Payload> consumer;
    @Captor
    private ArgumentCaptor<Payload> payloadArgumentCaptor;

    @BeforeAll
    static void setUpAll()
    {
        LoggerFactoryConfiguration.ENABLED = true;
        LoggerFactoryConfiguration.PORT = PORT;
        LoggerFactoryConfiguration.TEST_ID = TEST_ID;
    }

    @BeforeEach
    void setUp() throws SocketException
    {
        channel.listen(PORT);
        channel.addReadListener(consumer);
    }

    @Test
    void shouldSendLogPayload()
    {
        //given
        final String message = "Lorem ipsum";
        //when
        LOG.info(message);
        //then
        await().untilAsserted(() -> {
            verify(consumer, times(1)).accept(anyInt(), payloadArgumentCaptor.capture());
            final Payload payload = payloadArgumentCaptor.getValue();
            assertThat(payload).isInstanceOf(LogPayload.class);
            assertThat(payload.getType()).isEqualTo(PayloadType.LOG);
            assertThat(payload.getTestId()).isEqualTo(TEST_ID);
            final LogPayload logPayload = (LogPayload) payload;
            assertThat(logPayload.getLogLine()).contains(message);
        });
    }
}
