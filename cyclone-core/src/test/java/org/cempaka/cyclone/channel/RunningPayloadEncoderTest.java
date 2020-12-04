package org.cempaka.cyclone.channel;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.cempaka.cyclone.channel.payloads.RunningPayload;
import org.junit.jupiter.api.Test;

class RunningPayloadEncoderTest
{
    private static final byte[] DATA =
        {-84, -19, 0, 5, 119, 18, 0, 0, 0, 1, 0, 4, 116, 101, 115, 116, 0, 0, 0, 0, 0, 0, 0, 0};

    private final RunningPayloadEncoder runningPayloadEncoder = new RunningPayloadEncoder();

    @Test
    void shouldEncodeExitCode()
    {
        //given
        final Map<String, Double> measurements = new HashMap<>();
        measurements.put("test", 0D);
        final RunningPayload runningPayload = new RunningPayload("", measurements);
        //when
        final ByteBuffer byteBuffer = runningPayloadEncoder.encode(runningPayload);
        //then
        assertThat(byteBuffer.array()).isEqualTo(
            DATA);
    }

    @Test
    void shouldDecodeExitCode()
    {
        //given
        //when
        final RunningPayload runningPayload = runningPayloadEncoder.decode("", DATA);
        //then
        assertThat(runningPayload.getMeasurements()).containsEntry("test", 0D);
    }
}