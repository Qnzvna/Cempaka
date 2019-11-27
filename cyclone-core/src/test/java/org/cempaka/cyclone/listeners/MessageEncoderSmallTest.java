package org.cempaka.cyclone.listeners;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import org.cempaka.cyclone.listeners.payloads.EndedPayload;
import org.cempaka.cyclone.listeners.payloads.Payload;
import org.cempaka.cyclone.listeners.payloads.RunningPayload;
import org.cempaka.cyclone.listeners.payloads.StartedPayload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MessageEncoderSmallTest
{
    private final MessageEncoder messageEncoder = new MessageEncoder();

    private final Payload payload;

    public MessageEncoderSmallTest(final Payload payload)
    {
        this.payload = payload;
    }

    @Parameters
    public static Object[][] parameters()
    {
        final String testId = UUID.randomUUID().toString();
        final HashMap<String, Double> measurements = new HashMap<>();
        measurements.put("m", 42D);
        final byte[] array = new byte[4096];
        new Random().nextBytes(array);
        final String longStackTrace = new String(array, StandardCharsets.UTF_8);
        return new Object[][]{
            new Object[]{new StartedPayload(testId)},
            new Object[]{new RunningPayload(testId, new HashMap<>())},
            new Object[]{new RunningPayload(testId, measurements)},
            new Object[]{new EndedPayload(testId, 0)},
            new Object[]{new EndedPayload(testId, -1)},
            new Object[]{new EndedPayload(testId, 5)},
        };
    }

    @Test
    public void shouldEncodeDecodeMessage()
    {
        //given
        //when
        final ByteBuffer data = messageEncoder.encode(payload);
        final Payload decoded = messageEncoder.decode(data.array());
        //then
        assertThat(decoded).isEqualToComparingFieldByFieldRecursively(payload);
    }
}