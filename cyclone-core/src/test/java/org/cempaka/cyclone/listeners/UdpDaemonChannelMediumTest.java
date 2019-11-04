package org.cempaka.cyclone.listeners;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.SocketException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.cempaka.cyclone.listeners.payloads.Payload;
import org.cempaka.cyclone.listeners.payloads.StartedPayload;
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
        final StartedPayload payload = new StartedPayload(UUID.randomUUID().toString());
        //when
        client.connect();
        server.connect(SERVER_PORT);
        client.write(payload, SERVER_PORT);
        Thread.sleep(1_000);
        //then
        assertThat(payloadReference.get()).isEqualToComparingFieldByFieldRecursively(payload);
    }
}