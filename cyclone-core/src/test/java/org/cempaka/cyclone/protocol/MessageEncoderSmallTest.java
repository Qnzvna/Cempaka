package org.cempaka.cyclone.protocol;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;
import java.util.HashMap;
import org.cempaka.cyclone.protocol.payloads.EndedPayload;
import org.cempaka.cyclone.protocol.payloads.Payload;
import org.cempaka.cyclone.protocol.payloads.RunningPayload;
import org.cempaka.cyclone.protocol.payloads.StartedPayload;
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
        final HashMap<String, Double> measurements = new HashMap<>();
        measurements.put("m", 42D);
        return new Object[][]{
            new Object[]{new StartedPayload()},
            new Object[]{new RunningPayload(new HashMap<>())},
            new Object[]{new RunningPayload(measurements)},
            new Object[]{new EndedPayload(0, null)},
            new Object[]{new EndedPayload(-1, "failure")},
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