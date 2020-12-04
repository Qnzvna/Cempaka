package org.cempaka.cyclone.channel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import org.cempaka.cyclone.channel.payloads.LogPayload;

public class LogPayloadEncoder implements PayloadEncoder<LogPayload>
{
    @Override
    public ByteBuffer encode(final LogPayload payload)
    {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (final ObjectOutputStream outputStream = new ObjectOutputStream(byteStream)) {
            outputStream.writeUTF(payload.getLogLine());
            outputStream.flush();
            final byte[] data = byteStream.toByteArray();
            return ByteBuffer.wrap(data);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public LogPayload decode(final String testId, final byte[] data)
    {
        final InputStream inputStream = new ByteArrayInputStream(data);
        try (final ObjectInputStream stream = new ObjectInputStream(inputStream)) {
            return new LogPayload(testId, stream.readUTF());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
