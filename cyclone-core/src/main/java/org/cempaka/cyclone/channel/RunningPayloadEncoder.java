package org.cempaka.cyclone.channel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.cempaka.cyclone.channel.payloads.EndedPayload;
import org.cempaka.cyclone.channel.payloads.RunningPayload;

public class RunningPayloadEncoder implements PayloadEncoder<RunningPayload>
{
    @Override
    public ByteBuffer encode(final RunningPayload payload)
    {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (final ObjectOutputStream outputStream = new ObjectOutputStream(byteStream)) {
            final Map<String, Double> measurements = payload.getMeasurements();
            outputStream.writeInt(measurements.size());
            for (final Map.Entry<String, Double> entry : measurements.entrySet()) {
                outputStream.writeUTF(entry.getKey());
                outputStream.writeDouble(entry.getValue());
            }
            outputStream.flush();
            final byte[] data = byteStream.toByteArray();
            return ByteBuffer.wrap(data);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public RunningPayload decode(final String testId, final byte[] data)
    {
        final InputStream inputStream = new ByteArrayInputStream(data);
        final Map<String, Double> measurements = new HashMap<>();
        try (final ObjectInputStream stream = new ObjectInputStream(inputStream)) {
            final int measurementsSize = stream.readInt();
            for (int i = 0; i < measurementsSize; i++) {
                final String name = stream.readUTF();
                final double value = stream.readDouble();
                measurements.put(name, value);
            }
            return new RunningPayload(testId, measurements);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
