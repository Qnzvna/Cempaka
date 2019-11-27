package org.cempaka.cyclone.listeners;

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
import java.util.Optional;
import org.cempaka.cyclone.listeners.payloads.EndedPayload;
import org.cempaka.cyclone.listeners.payloads.Payload;
import org.cempaka.cyclone.listeners.payloads.PayloadType;
import org.cempaka.cyclone.listeners.payloads.RunningPayload;
import org.cempaka.cyclone.listeners.payloads.StartedPayload;

class PayloadEncoder
{
    ByteBuffer encode(final Payload payload)
    {
        final PayloadType type = payload.getType();
        switch (type) {
            case STARTED:
                return ByteBuffer.wrap(new byte[]{});
            case RUNNING:
                return encodeRunning((RunningPayload) payload);
            case ENDED:
                return encodeEnded((EndedPayload) payload);
            default:
                throw new IllegalArgumentException();
        }
    }

    private ByteBuffer encodeRunning(final RunningPayload payload)
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

    private ByteBuffer encodeEnded(final EndedPayload payload)
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

    Payload decode(final PayloadType type, final String testId, final byte[] data)
    {
        switch (type) {
            case STARTED:
                return new StartedPayload(testId);
            case RUNNING:
                return decodeRunning(testId, data);
            case ENDED:
                return decodeEnded(testId, data);
            default:
                throw new IllegalArgumentException();
        }
    }

    private Payload decodeRunning(final String testId, final byte[] data)
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

    private Payload decodeEnded(final String testId, final byte[] data)
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
