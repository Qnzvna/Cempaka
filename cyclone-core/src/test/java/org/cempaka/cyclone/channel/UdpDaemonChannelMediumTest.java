package org.cempaka.cyclone.channel;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.SocketException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Test;

public class UdpDaemonChannelMediumTest
{
    private static final int SERVER_PORT = 5200;

    private final UdpDaemonChannel server = new UdpDaemonChannel();
    private final UdpDaemonChannel client = new UdpDaemonChannel();

    @Test
    public void shouldSendAndReceivePackets() throws SocketException, InterruptedException
    {
        //given
        final AtomicReference<Payload> payloadReference = new AtomicReference<>();
        server.addReadListener((receivedPort, payload) -> payloadReference.set(payload));
        final String testId = UUID.randomUUID().toString();
        //when
        client.connect(SERVER_PORT);
        server.listen(SERVER_PORT);
        client.start(testId);
        Thread.sleep(1_000);
        //then
        assertThat(payloadReference.get()).isInstanceOf(StartedPayload.class);
    }
}