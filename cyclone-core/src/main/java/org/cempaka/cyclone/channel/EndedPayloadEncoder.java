package org.cempaka.cyclone.channel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import org.cempaka.cyclone.channel.payloads.EndedPayload;

public class EndedPayloadEncoder implements PayloadEncoder<EndedPayload>
{
    @Override
    public ByteBuffer encode(final EndedPayload payload)
    {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (final ObjectOutputStream outputStream = new ObjectOutputStream(byteStream)) {
            outputStream.writeInt(payload.getExitCode());
            outputStream.flush();
            final byte[] data = byteStream.toByteArray();
            return ByteBuffer.wrap(data);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public EndedPayload decode(final String testId, final byte[] data)
    {
        final InputStream inputStream = new ByteArrayInputStream(data);
        try (final ObjectInputStream stream = new ObjectInputStream(inputStream)) {
            final int exitCode = stream.readInt();
            return new EndedPayload(testId, exitCode);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
