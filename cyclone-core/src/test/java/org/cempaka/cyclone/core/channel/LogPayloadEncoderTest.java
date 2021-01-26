package org.cempaka.cyclone.core.channel;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

class LogPayloadEncoderTest
{
    private static final byte[] DATA = {-84, -19, 0, 5, 119, 6, 0, 4, 116, 101, 115, 116};
    private final LogPayloadEncoder logPayloadEncoder = new LogPayloadEncoder();

    @Test
    void shouldEncodeExitCode()
    {
        //given
        final LogPayload logPayload = new LogPayload("", "test");
        //when
        final ByteBuffer byteBuffer = logPayloadEncoder.encode(logPayload);
        //then
        assertThat(byteBuffer.array()).isEqualTo(DATA);
    }

    @Test
    void shouldDecodeExitCode()
    {
        //given
        //when
        final LogPayload endedPayload = logPayloadEncoder.decode("", DATA);
        //then
        assertThat(endedPayload.getLogLine()).isEqualTo("test");
    }
}