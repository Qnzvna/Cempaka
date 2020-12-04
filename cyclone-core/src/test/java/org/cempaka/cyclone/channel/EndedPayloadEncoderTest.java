package org.cempaka.cyclone.channel;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;
import org.cempaka.cyclone.channel.payloads.EndedPayload;
import org.junit.jupiter.api.Test;

class EndedPayloadEncoderTest
{
    private static final byte[] DATA = {-84, -19, 0, 5, 119, 4, 0, 0, 0, 0};
    private final EndedPayloadEncoder endedPayloadEncoder = new EndedPayloadEncoder();

    @Test
    void shouldEncodeExitCode()
    {
        //given
        final EndedPayload endedPayload = new EndedPayload("", 0);
        //when
        final ByteBuffer byteBuffer = endedPayloadEncoder.encode(endedPayload);
        //then
        assertThat(byteBuffer.array()).isEqualTo(DATA);
    }

    @Test
    void shouldDecodeExitCode()
    {
        //given
        //when
        final EndedPayload endedPayload = endedPayloadEncoder.decode("", DATA);
        //then
        assertThat(endedPayload.getExitCode()).isEqualTo(0);
    }
}