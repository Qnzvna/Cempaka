package org.cempaka.cyclone.protocol;

import org.cempaka.cyclone.protocol.payloads.EndedPayload;
import org.cempaka.cyclone.protocol.payloads.Payload;
import org.cempaka.cyclone.protocol.payloads.PayloadType;
import org.cempaka.cyclone.protocol.payloads.RunningPayload;
import org.cempaka.cyclone.protocol.payloads.StartedPayload;

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
            final Map<String, Long> failedExecutions = payload.getFailedExecutions();
            outputStream.writeInt(failedExecutions.size());
            for (final Map.Entry<String, Long> entry : failedExecutions.entrySet()) {
                outputStream.writeUTF(entry.getKey());
                outputStream.writeLong(entry.getValue());
            }
            final Map<String, Long> successExecutions = payload.getSucccessExecutions();
            outputStream.writeInt(successExecutions.size());
            for (final Map.Entry<String, Long> entry : successExecutions.entrySet()) {
                outputStream.writeUTF(entry.getKey());
                outputStream.writeLong(entry.getValue());
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
            final Optional<String> stackTrace = payload.getStackTrace();
            outputStream.writeBoolean(stackTrace.isPresent());
            if (stackTrace.isPresent()) {
                outputStream.writeUTF(stackTrace.get());
            }
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
        final Map<String, Long> failedExecutions = new HashMap<>();
        final Map<String, Long> successExecutions = new HashMap<>();
        try (final ObjectInputStream stream = new ObjectInputStream(inputStream)) {
            final int measurementsSize = stream.readInt();
            for (int i = 0; i < measurementsSize; i++) {
                final String name = stream.readUTF();
                final double value = stream.readDouble();
                measurements.put(name, value);
            }
            final int failedSize = stream.readInt();
            for (int i = 0; i < failedSize; i++) {
                final String name = stream.readUTF();
                final long value = stream.readLong();
                failedExecutions.put(name, value);
            }
            final int successSize = stream.readInt();
            for (int i = 0; i < successSize; i++) {
                final String name = stream.readUTF();
                final long value = stream.readLong();
                successExecutions.put(name, value);
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
            final boolean failed = stream.readBoolean();
            final String stackTrace;
            if (failed) {
                stackTrace = stream.readUTF();
            } else {
                stackTrace = null;
            }
            return new EndedPayload(testId, exitCode, stackTrace);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
